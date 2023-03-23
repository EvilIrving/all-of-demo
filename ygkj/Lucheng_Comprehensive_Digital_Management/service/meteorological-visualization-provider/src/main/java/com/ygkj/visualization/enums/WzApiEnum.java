package com.ygkj.visualization.enums;


import lombok.Getter;

@Getter
public enum WzApiEnum {

    PREDIT_RAINFALL_BY_AREANAME("https://sgpt.wzsl.com:8081/api/data-management/busApiInvoke/9Yfurz0ISIBYKl6IRhsHmw5+JQJMEJShdX4t3IuQf2Y=",
            "rainfallPredictGrid","dq/IKkr+aZxuRv1ATYmdoQ==","sMf/d5D+0sq6/NaKfc42YatqPHVJzjbzwSGpED+PsW8="),
    RAIN_FALL_GRID("https://sgpt.wzsl.com:8081/api/data-management/busApiInvoke/VBuS4Ib2TVoyiiEf7Vrye3GfKPrj4rtWBnu4NNgMEao=",
            "RainFallGrid","dq/IKkr+aZxuRv1ATYmdoQ==","2FVFUITkwnIGsDvcGkQeRwHAY/8BfEz0sJJmPYKOX2w=");
    ;

    private String url;

    private String tokenKey;

    private String key;

    private String secret;

    WzApiEnum(String url, String tokenKey, String key, String secret) {
        this.url = url;
        this.tokenKey = tokenKey;
        this.key = key;
        this.secret = secret;
    }
}
