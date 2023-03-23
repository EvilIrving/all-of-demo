package com.ygkj.lcsyn.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DingDingConfig {

    public static String APP_KEY;

    public static String APP_SECRET;

    public static String APP_SYMBOL;

    @Value("${watermanage.appkey}")
    public void setWaterManageAppKey(String appkey) {
        if (null != appkey && !"".equals(appkey.trim())) {
            APP_KEY = appkey;
        }

    }

    @Value("${watermanage.appsymbol}")
    public void setWaterManageAppSymbol(String appsymbol) {
        if (null != appsymbol && !"".equals(appsymbol.trim())) {
            APP_SYMBOL = appsymbol;
        }
    }

    @Value("${watermanage.appsecret}")
    public void setWaterManageAppSecret(String appSecret) {
        if (null != appSecret && !"".equals(appSecret.trim())) {
            APP_SECRET = appSecret;
        }

    }
}
