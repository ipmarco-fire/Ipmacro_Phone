
package com.ipmacro.iptv;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import com.ipmacro.app.Config;
import com.ipmacro.app.IptvApplication;
import com.ipmacro.ppcore.PPCore;
import com.linkin.utils.HttpUtil;
import com.ipmacro.utils.aes.AESUtil;
import com.loopj.android.http.AsyncHttpResponseHandler;

import java.net.URLEncoder;

public class GetAutokeyActivity extends Activity {
    public static final int RESULT_CODE = 20;

    IptvApplication mApp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.get_autokey);

        mApp = (IptvApplication) getApplication();
        doActivate();
    }

    private void doActivate() {
        Build bd = new Build();
        String model = bd.MODEL;
        String pwd = PPCore.getPwd();
        String activationUrl = Config.ACTIVATION_URL + "?customerId="
                + getResources().getString(R.string.customer_id)
                + "&machineCode=" + pwd + "&os="
                + android.os.Build.VERSION.RELEASE + "&models="
                + model.replaceAll(" ", "");

        HttpUtil.get(activationUrl, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String result) {
                Boolean loginOk = PPCore.login(result);
                if (loginOk) {
                    Toast.makeText(GetAutokeyActivity.this, R.string.activate_success, Toast.LENGTH_LONG).show();
                    mApp.setAutokey(result);
                    doLogin();
                } else {
                    String tip = getResources().getString(R.string.activate_fail) + ":" + result;
                    Toast.makeText(GetAutokeyActivity.this, tip, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Throwable arg0) {
                Toast.makeText(GetAutokeyActivity.this, R.string.activate_fial_connect_error, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFinish() {
            }
        });
    }

    // 注册
    private void doLogin() {
        int sn = PPCore.getSN();
        int life = PPCore.getLife();// life = 0;

        String str = "";
        try {
            str = URLEncoder.encode(AESUtil.encrypt("sn=" + sn
                    + "&seconds=" + life));
        } catch (Exception e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        String url = getResources().getString(R.string.weburl) + "channel/phone/activatePhoneAccount.htm?str=" + str;
        HttpUtil.get(url, new AsyncHttpResponseHandler() {
            public void onFinish() {
                setResult(RESULT_CODE);
                finish();
            };
        });
    }
}
