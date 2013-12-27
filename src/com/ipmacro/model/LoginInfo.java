
package com.ipmacro.model;

public class LoginInfo {
    public String str;
    public PhoneUpdate phoneUpdate = new PhoneUpdate();
    public String liveChannelIds;
    public String planChannelIds;

    public class PhoneUpdate {
        public Boolean isForcedUpdate;
        public String path;
        public int version;
        public String versionName;
    }
}
