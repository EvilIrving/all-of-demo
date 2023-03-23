package com.ygkj.constants;


public interface ZlbConstants {

    //bcdsg.zj.gov.cn政务外网地址
    //ibcdsg.zj.gov.cn互联网地址
    /**
     * 浙里办单点登录 ticketId换token的地址
     */
//    String ACCESS_TOKEN_URL = "https://bcdsg.zj.gov.cn:8443/restapi/prod/IC33000020220329000007/uc/sso/access_token";
    String ACCESS_TOKEN_URL = "https://ibcdsg.zj.gov.cn:8443/restapi/prod/IC33000020220329000007/uc/sso/access_token";
    /**
     * 浙里办单点登录 token获取用户信息地址
     */
//    String GET_USER_INFO_URL = "https://bcdsg.zj.gov.cn:8443/restapi/prod/IC33000020220329000008/uc/sso/getUserInfo";
    String GET_USER_INFO_URL = "https://ibcdsg.zj.gov.cn:8443/restapi/prod/IC33000020220329000008/uc/sso/getUserInfo";

    /**
     * 政务服务网个人单点登录 ticketId换token的地址
     */
//    String ZWW_ACCESS_TOKEN_URL = "https://bcdsg.zj.gov.cn:8443/restapi/prod/IC33000020220228000002/sso/servlet/simpleauth";
    String ZWW_ACCESS_TOKEN_URL = "https://ibcdsg.zj.gov.cn:8443/restapi/prod/IC33000020220228000002/sso/servlet/simpleauth";

    /**
     * 政务服务网个人单点登录 token获取用户信息地址
     */
//    String ZWW_GET_USER_INFO_URL = "https://bcdsg.zj.gov.cn:8443/restapi/prod/IC33000020220228000004/sso/servlet/simpleauth";
    String ZWW_GET_USER_INFO_URL = "https://ibcdsg.zj.gov.cn:8443/restapi/prod/IC33000020220228000004/sso/servlet/simpleauth";

    /**
     * IRS请求携带的请求头
     */
    String X_BG_HMAC_ACCESS_KEY = "X-BG-HMAC-ACCESS-KEY";
    String X_BG_HMAC_SIGNATURE = "X-BG-HMAC-SIGNATURE";
    String X_BG_HMAC_ALGORITHM = "X-BG-HMAC-ALGORITHM";
    String X_BG_DATE_TIME = "X-BG-DATE-TIME";
    String CONTENT_TYPE= "Content-Type";

    /**
     * 请求数据格式
     */
    String X_WWW_FORM_URLENCODED = "application/x-www-form-urlencoded";
    String JSON = "application/json";

    /**
     * IRS签名算法
     */
    String DEFAULT_HMAC_SIGNATURE = "hmac-sha256";

    /**
     * 应用ID
     */
    String APP_ID = "2002276620";
    /**
     * 微信端固定值为weixin
     */
    String WEIXIN_ENDPOINT_TYPE = "weixin";


    /**
     * IRS 申请组件生成的AK
     */
    String IRS_AK = "BCDSGA_35a21689670e02112cc3608cf7acc04b";
    /**
     * IRS 申请组件生成的SK
     */
    String IRS_SK = "BCDSGS_357ea808f7d1fde353ba4aeeb2a1ed4c";


    String TOKEN_SESSION_KEY = "sessionAccessToken";
    String USER_INFO_KEY = "sessionUserInfo";

}
