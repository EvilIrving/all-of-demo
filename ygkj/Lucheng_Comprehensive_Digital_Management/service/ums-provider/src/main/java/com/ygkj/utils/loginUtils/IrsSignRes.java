package com.ygkj.utils.loginUtils;

import lombok.Data;

/**
 * @author jie.chen
 * @date 2022-03-30 15:28
 */
@Data
public class IrsSignRes {
    private String accessKey;
    private String signature;
    private String algorithm;
    private String dateTime;
}
