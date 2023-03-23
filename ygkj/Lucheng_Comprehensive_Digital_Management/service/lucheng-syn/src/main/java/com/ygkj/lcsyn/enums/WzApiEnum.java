package com.ygkj.lcsyn.enums;


import lombok.Getter;

@Getter
public enum WzApiEnum {

    WAS("https://sgpt.wzsl.com:8081/api/data-management/busApiInvoke/9dtXLRC8jWbA4P1vVknmEnLQenbl5cG9QFROVOf8HWI=",
            "was","dq/IKkr+aZxuRv1ATYmdoQ==","7jZE69HpX3V7TJZgFM+iMJRhdLdZhCUvXB7CijfOKzY="),

    FSCT("https://sgpt.wzsl.com:8081/api/data-management/busApiInvoke/XtHgqtCla58b85kEWiCEkkMUdX1erkvjDW51wcxVTEg=?releaseType=2",
            "fsct","dq/IKkr+aZxuRv1ATYmdoQ==","5nQR2PdJYvp1qQGkPlYjmOak0ma96x04AHy0s3/xL1A="),

    FSCT_VAL("https://sgpt.wzsl.com:8081/api/data-management/busApiInvoke/CGwwYbCBi3kRQoFKyGTHA8rLOy0tEu8JAZrvd4lKk8c=",
            "fsctVal","dq/IKkr+aZxuRv1ATYmdoQ==","XfdLGWSEkKx4Fe3sEiKc0hnOe/8XqEiW4oY7KcQxkFw=");

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
