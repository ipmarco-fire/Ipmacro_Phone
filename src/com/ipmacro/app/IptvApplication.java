package com.ipmacro.app;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.ipmacro.model.ChannelType;
import com.ipmacro.model.LoginInfo;
import com.ipmacro.ppcore.PPCore;
import com.linkin.app.LinkinApplication;

import java.util.List;

public class IptvApplication extends LinkinApplication{
    public static final String AUTOKEY = "autokey";
    public LoginInfo loginInfo;
    public List<ChannelType> typeList;
    @Override
    public void onCreate() {
        super.onCreate();
        PPCore.init(getApplicationContext());
        login();
    }

    public boolean login() {
        String autokey = getAutokey();
        if (autokey != null && !autokey.equals("")) {
            PPCore.login(autokey);
        }
        return false;
    }

    public String getAutokey() {
        SharedPreferences prefs = getSharedPreferences(Config.SHARED_NAME, 0);
        String autokey = prefs.getString(AUTOKEY, null);
        return autokey;
    }

    public boolean setAutokey(String autokey) {
        boolean bo = PPCore.login(autokey);
        if (bo) {
            SharedPreferences prefs = getSharedPreferences(Config.SHARED_NAME, 0);
            Editor editor = prefs.edit();
            editor.putString(AUTOKEY, autokey);
            editor.commit();
        }
        return bo;
    }
}
