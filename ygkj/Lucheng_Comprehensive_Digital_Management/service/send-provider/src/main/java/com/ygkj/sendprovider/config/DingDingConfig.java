package com.ygkj.sendprovider.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DingDingConfig {

    public static String URL_BASE = "https://sgpt.zjwater.com/service";
    /**
     * 获取token
     */
    public static String URL_TOKEN = URL_BASE + "/v1/token/getAccessToken";

    /**
     * 根据手机号发送工作通知
     */
    public static String URL_SEND_BY_PHONE = URL_BASE + "/v2/api/102/zzdMessage/pushTextMessageByMobile";

    /**
     * 新增待办
     */
    public static String URL_ADD_WORK = URL_BASE + "/v2/api/102/userWork/addWork";

    /**
     * 结束待办
     */
    public static String URL_END_WORK = URL_BASE + "/v2/api/102/userWork/endWork";

    /**
     * 取消待办
     */
    public static String URL_CANCEL_WORK = URL_BASE + "/v2/api/102/userWork/cancelWork";

    /**
     * 处理待办
     */
    public static String URL_DEAL_WORK = URL_BASE + "/v2/api/102/userWork/dealWork";

    /**
     * 结束同任务用户待办
     */
    public static String URL_END_WORK_BY_TASK_ID = URL_BASE + "/v2/api/102/userWork/endWorkByTaskId";

    /**
     * 根据手机号获取用户信息
     */
    public static String URL_USER_BY_PHONE = URL_BASE + "/v2/api/5046/user/getUserInfoByMobile";

    /**
     * 根据手机号发送浙政钉文本消息
     */
    public static String URL_SEND_DING_MSG = URL_BASE + "/v2/api/102/zzdMessage/pushTextMessageByMobile";



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
