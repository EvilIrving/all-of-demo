package com.ygkj.lcsyn.model;

import lombok.Data;

import java.util.Date;

/**
 * @description 考勤人员表
 * @author lxl
 * @date 2022-07-05
 */
@Data
public class AttendancePersonnelBase {

    /**
     * 主键
     */
    private String id;

    /**
     * 公司名称
     */
    private String cname;

    /**
     * 考勤人姓名
     */
    private String name;

    /**
     * 部门名称
     */
    private String department;

    /**
     * 部门id
     */
    private String departmentId;

    /**
     * 手机号码
     */
    private String phone;

    /**
     * 头像图片地址url
     */
    private String picturePath;

    /**
     * 工号
     */
    private String jobNumber;

    /**
     * 职能
     */
    private String post;

    /**
     * 性别
     */
    private String sex;

    /**
     * 租户id
     */
    private String tenantId;

    /**
     * 更新时间
     */
    private Date updateTime;

    public AttendancePersonnelBase() {
    }

}

