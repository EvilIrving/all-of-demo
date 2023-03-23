package com.dcxx.framework.watermanagesdk.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource({"classpath:water-manage-sdk.properties"})
public class AppConfig {

    public final static String WATERMANAGE_AUTH_SUBURL="/api/ssologin/validateTicket";

    public static String WATERMANAGE_APPKEY;
    public static String WATERMANAGE_APPSECRET;
    public static String WATERMANAGE_AUTH_BASEURL;
    public static String WATERMANAGE_AUTH_URL=WATERMANAGE_AUTH_BASEURL+WATERMANAGE_AUTH_SUBURL;



    @Value("${watermanage.appkey}")
    public void setWatermanageAppkey(String watermanageAppkey) {
        WATERMANAGE_APPKEY = watermanageAppkey;
    }

    @Value("${watermanage.appsecret}")
    public void setWatermanageAppsecret(String watermanageAppsecret) {
        WATERMANAGE_APPSECRET = watermanageAppsecret;
    }

    @Value("${watermanage.auth.baseurl}")
    public void setWatermanageAuthBaseurl(String watermanageAuthBaseurl) {
        WATERMANAGE_AUTH_BASEURL = watermanageAuthBaseurl;
        WATERMANAGE_AUTH_URL=WATERMANAGE_AUTH_BASEURL+WATERMANAGE_AUTH_SUBURL;
    }
    public AppConfig() {
    }
}
