
package com.ipmacro.iptv;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ipmacro.CCore;
import com.ipmacro.app.Config;
import com.ipmacro.app.IptvApplication;
import com.ipmacro.model.LoginInfo;
import com.ipmacro.model.Plan;
import com.ipmacro.parser.LoginInfoParser;
import com.ipmacro.parser.PlanParser;
import com.ipmacro.ppcore.PPCore;
import com.linkin.utils.HttpUtil;
import com.linkin.utils.PackageUtil;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LoginActivity extends Activity {
    private static final int REQUEST_CODE = 1;

    IptvApplication mApp;
    CCore cCore = new CCore();

    LinearLayout layoutLoading;
    TextView txtLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        mApp = (IptvApplication) getApplication();
        initView();

        String autokey = mApp.getAutokey();
        if (autokey == null) {
            Intent intent = new Intent();
            intent.setClass(LoginActivity.this, GetAutokeyActivity.class);
            startActivityForResult(intent, REQUEST_CODE);
        } else {
            checkExpire();
        }
    }

    private void initView() {
        layoutLoading = (LinearLayout) findViewById(R.id.layout_loading);
        txtLoading = (TextView) findViewById(R.id.txt_loading);
    }

    // 检测有效期
    private void checkExpire() {
        if (PPCore.getExpire() < 0) {
            AlertDialog.Builder builder = new Builder(LoginActivity.this);
            builder.setMessage(R.string.expire_tip);
            builder.setTitle(R.string.dialog_title);
            builder.setPositiveButton(R.string.dialog_sure, new OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    LoginActivity.this.finish();
                }
            });
            builder.create().show();
        } else {
            checkValidity();
        }
    }

    // 检测套餐
    private void checkValidity() {
        layoutLoading.setVisibility(View.VISIBLE);
        String str = cCore.getCheckExpiredInfo(PPCore.getSN());
        str = URLEncoder.encode(str);
        String url = getResources().getString(R.string.weburl) + Config.CHECK_VALIDITY_URL
                + "?str=" + str;
        HttpUtil.get(url, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(JSONObject response) {
                try {
                    mApp.loginInfo = LoginInfoParser.parser(response);
                    checkUpgrade(mApp.loginInfo);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Throwable error) {
                AlertDialog.Builder builder = new Builder(LoginActivity.this);
                builder.setMessage(R.string.connect_server_fail);
                builder.setTitle(R.string.dialog_title);
                builder.setPositiveButton(R.string.dialog_retry, new OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        checkValidity();
                    }
                });
                builder.setNegativeButton(R.string.dialog_quit, new OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        LoginActivity.this.finish();
                    }
                });
                builder.create().show();
            }

            @Override
            public void onFinish() {
                layoutLoading.setVisibility(View.GONE);
            }
        });
    }

    // 判断升级
    private void checkUpgrade(LoginInfo loginInfo) {
        boolean bo = loginInfo.phoneUpdate.version > PackageUtil.getVersionCode(this);

        if (bo) {
            AlertDialog.Builder builder = new Builder(LoginActivity.this);
            builder.setMessage(R.string.new_version);
            builder.setTitle(R.string.dialog_title);
            builder.setPositiveButton(R.string.dialog_upgrade, new OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    doUpgrade();
                }
            });
            if (!loginInfo.phoneUpdate.isForcedUpdate) {
                builder.setNegativeButton(R.string.dialog_cancel, new OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        checkPlanExpire();
                    }
                });
            }
            builder.create().show();
        } else {
            checkPlanExpire();
        }
    }

    private void doUpgrade() {
        checkPlanExpire();
    }

    /*
     * 如果没有过期,显示手机套餐所有频道
     */
    private void checkPlanExpire() {
        Boolean bo = cCore.getIsExpired(mApp.loginInfo.str, PPCore.getSN()); // true:没有过期
        if (bo) {
            initData();
        } else {
            txtLoading.setText(R.string.init);
            layoutLoading.setVisibility(View.VISIBLE);
            String url = getResources().getString(R.string.weburl) + Config.PHONE_PLAN_LIST + "?sn=" + PPCore.getSN();
            Log.i(Config.TAG,url);
            HttpUtil.get(url, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(JSONObject response) {
                    String liveChannelIds = "";
                    try {
                        List<Plan> list = PlanParser.parserPlan(response);
                        List<String> strList = new ArrayList<String>();
                        Map<String,Boolean> map = new HashMap<String,Boolean>();
                        for(Plan plan:list){
                            String[] arr = plan.getLiveChannelIds().split(",");
                            for(String s:arr){
                                if(!map.containsKey(s)){
                                    strList.add(s);
                                    map.put(s, true);
                                }
                            }
                        }
                        for(int i = 0;i<strList.size();i++){
                            if(i > 0){
                                liveChannelIds += ","; 
                            }
                            liveChannelIds += strList.get(i);
                        }
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    mApp.loginInfo.planChannelIds = liveChannelIds;
                    initData();
                }
                public void onFailure(Throwable error) {
                    mApp.loginInfo.planChannelIds = "";
                    initData();
                };
                @Override
                public void onFinish() {
                    layoutLoading.setVisibility(View.GONE);
                }
            });
        }
    }
    
    private void initData(){
        txtLoading.setText(R.string.init);
        layoutLoading.setVisibility(View.VISIBLE);
        String url = getResources().getString(R.string.weburl) + getResources().getString(R.string.channel_url);
        HttpUtil.get(url, new AsyncHttpResponseHandler(){
            @Override
            public void onSuccess(String content) {
                if(content.indexOf("{")>-1){
                    SharedPreferences settings = getSharedPreferences(Config.SHARED_NAME, 0);
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putString(Config.LIVE_JSON_DATA, content);
                    editor.commit();
                }
                gotoMainActivity();
            }
            @Override
            public void onFailure(Throwable error) {
                gotoMainActivity();
            }
            @Override
            public void onFinish() {
                layoutLoading.setVisibility(View.GONE);
            }
        });
    }
    
    private void gotoMainActivity(){
        Intent intent = new Intent();
        intent.setClass(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == REQUEST_CODE && resultCode == GetAutokeyActivity.RESULT_CODE) {
            checkExpire();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
