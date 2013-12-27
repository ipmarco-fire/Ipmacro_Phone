
package com.ipmacro.iptv.fragment;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.ipmacro.adapter.PlanAdapter;
import com.ipmacro.app.Config;
import com.ipmacro.iptv.R;
import com.ipmacro.iptv.RechargeActivity;
import com.ipmacro.model.Plan;
import com.ipmacro.ppcore.PPCore;
import com.ipmacro.utils.aes.AESUtil;
import com.linkin.utils.HttpUtil;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class InfoFragment extends Fragment {

    View parent;

    static InfoFragment mFragment;

    public static synchronized InfoFragment getInstance() {
        if (mFragment == null) {
            mFragment = new InfoFragment();
        }
        return mFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parent = inflater.inflate(R.layout.info, null);

        TextView txtSn = (TextView) parent.findViewById(R.id.txt_sn);
        txtSn.setText(PPCore.getSN() + "");
        TextView txtVersion = (TextView) parent.findViewById(R.id.txt_version);
        try {
            txtVersion.setText(getVersionName());
            initData();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Button btnRecharge = (Button) parent.findViewById(R.id.btn_recharge);
        btnRecharge.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), RechargeActivity.class);
                startActivity(intent);
            }
        });
        return parent;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e(Config.TAG,"onResume");
    }
    
    private void initData() throws Exception {
        String str = URLEncoder.encode(AESUtil.encrypt("sn=" + PPCore.getSN()));
        String url = getResources().getString(R.string.weburl) + "/channel/phone/viewPlan.htm?str=" + str;
        Log.i(Config.TAG,url);
        HttpUtil.get(url, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(JSONObject response) {
                try {
                    afterInitData(response);
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Throwable error) {
                super.onFailure(error);
            }
            @Override
            public void onFinish() {
                super.onFinish();
            }
        });
    }
    
    
    private void afterInitData(JSONObject response) throws JSONException {
        int balance = response.getInt("balance");
        showBalance(balance);
        
        JSONArray planList = response.getJSONArray("planList");
        List<Plan> list = new ArrayList<Plan>();
        for(int i = 0;i<planList.length();i++){
            JSONObject obj = planList.getJSONObject(i);
            String name = obj.getString("planName");
            double price = obj.getDouble("price");
            String dateOfExpiry = obj.getString("dateOfExpiry");
            
            Plan p = new Plan();
            p.setDateOfExpiry(dateOfExpiry);
            p.setName(name);
            p.setPrice(price);
            
            list.add(p);
        }
        
        if(list.size() > 0){
            ListView listview = (ListView) parent.findViewById(R.id.listview);
            listview.setVisibility(View.VISIBLE);
            PlanAdapter adapter = new PlanAdapter(getActivity(),list);
            listview.setAdapter(adapter);
        }else{
            TextView txtNoplanTip = (TextView) parent.findViewById(R.id.txt_noplan_tip);
            txtNoplanTip.setText(R.string.no_plan);
            txtNoplanTip.setVisibility(View.VISIBLE);
        }
    }
    private void showBalance(int balance){
        TextView txtBalance = (TextView) parent.findViewById(R.id.txt_balance);
        String tip = getResources().getString(R.string.balance)+" : ";
        if(balance<=0){
            tip += "<font color=\"#ff0000\">"+balance+"</fong>";
        }else{
            tip += balance;
        }
        txtBalance.setText(Html.fromHtml(tip));
    }
    
    private String getVersionName() throws Exception {
        // 获取packagemanager的实例
        PackageManager packageManager = getActivity().getPackageManager();
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageInfo packInfo = packageManager.getPackageInfo(getActivity().getPackageName(), 0);
        String version = packInfo.versionName;
        return version;
    }
}
