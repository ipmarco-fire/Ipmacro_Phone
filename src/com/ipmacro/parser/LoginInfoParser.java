package com.ipmacro.parser;

import com.ipmacro.model.LoginInfo;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginInfoParser {
    public  static LoginInfo parser(JSONObject root) throws JSONException{
        LoginInfo loginInfo = new LoginInfo();
        loginInfo.str = root.getString("str");
        loginInfo.liveChannelIds = root.getString("liveChannelIds");
        
        
//        JSONObject phoneUpdate = root.getJSONObject("phoneUpdate");
//        loginInfo.phoneUpdate.isForcedUpdate = phoneUpdate.getBoolean("isForcedUpdate");
//        loginInfo.phoneUpdate.path = phoneUpdate.getString("path");
//        loginInfo.phoneUpdate.version = phoneUpdate.getInt("ver");
//        loginInfo.phoneUpdate.versionName = phoneUpdate.getString("versionName");
        
        return loginInfo;
    }
}
