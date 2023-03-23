package com.ygkj.lcsyn.enums;

import lombok.Getter;

/**
 * @author lxl
 * @create 2022-05-27 9:31
 * @description
 */
@Getter
public enum SysUserEnum {

    SALT("盐值","04c538b8088645a4b52e"),
    PASSWORD("初始密码","7b0df7ee87bde51164561be5d6960825"),
    USER_TYPE("默认用户类型","1");


    private String key;

    private String value;

    SysUserEnum(String key, String value) {
        this.key = key;
        this.value = value;
    }
}
