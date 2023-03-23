package com.ygkj.entity;

import lombok.Data;

@Data
public class DingDingUser {

    /**
     * id,采用用户编码+部门id
     */
    private String id;

    /**
     * 用户编码
     */
    private String userCode;

    /**
     * 用户名称
     */
    private String userName;

    /**
     * 真实姓名
     */
    private String trueName;

    /**
     * 手机号码
     */
    private String mobile;

    /**
     * 修改时间
     */
    private String modifyTime;

    /**
     * 排序
     */
    private Integer orderId;

    /**
     * 岗位
     */
    private String jobs;

    /**
     * 职务
     */
    private String position;

    /**
     * 部门id
     */
    private String deptId;
}
