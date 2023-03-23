package com.ygkj.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class SysOpenUserBind implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户id
     */
    private String userid;

    /**
     * 平台对应的openid
     */
    private String openid;

    /**
     * 平台类型 1-钉钉
     */
    private String plattype;
}
