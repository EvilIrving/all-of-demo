package com.ygkj.lcsyn.model;

import lombok.Data;

/**
 * @author lxl
 * @create 2022-06-17 14:15
 * @description
 */
@Data
public class IrsTokenEntity {

    /**
     * 刷新秘钥
     */
    private String refreshSecret;
    /**
     * 请求秘钥
     */
    private String requestSecret;

    /**
     * 刷新秘钥过期时间
     */
    private Long refreshSecretEndTime;

    /**
     * 请求秘钥过期时间
     */
    private Long requestSecretEndTime;


}
