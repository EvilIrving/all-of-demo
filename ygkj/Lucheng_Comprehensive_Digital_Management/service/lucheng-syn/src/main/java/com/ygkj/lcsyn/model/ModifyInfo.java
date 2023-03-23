package com.ygkj.lcsyn.model;

import lombok.Data;

import java.util.List;

/**
 * @author lxl
 * @create 2022-03-08 10:19
 * @description 水管理平台失效记录 信息
 */
@Data
public class ModifyInfo {

    /**
     * 变更时间
     */
    private String modifyTime;

    /**
     * 组织id
     */
    private String organizationId;


    /**
     * 用户编码
     */
    private String userCode;
    /**
     * 真实姓名
     */
    private String trueName;
    /**
     * 行政区划名称
     */
    private String adName;
    /**
     * 加密邮箱
     */
    private String mail;
    /**
     * 不知什么意思  大部分为 “1”
     */
    private String isZzd;
    /**
     * 加密手机号码
     */
    private String mobile;
    /**
     * 行政区划  省略了 0 的
     */
    private String adcd;
    /**
     * 多数为 null
     */
    private String isAdmin;
    /**
     * 头像
     */
    private String avatar;

    /**
     * 加密用户名信息
     */
    private String userName;


}
