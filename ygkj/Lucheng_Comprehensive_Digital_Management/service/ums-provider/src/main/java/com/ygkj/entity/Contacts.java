package com.ygkj.entity;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author xq
 * @Description 通讯录实体类
 * @Date 2021/1/13
 */
@Data
@ToString
public class Contacts implements Serializable {

    /**
     * 用户
     */
    private String userId;

    /**
     * 姓名
     */
    private String username;

    /**
     * 性别
     */
    private Integer sex;

    /**
     * 移动电话
     */
    private String phone;

    /**
     * 部门id
     */
    private String deptId;

    /**
     * 部门名称
     */
    private String deptName;

    /**
     * 电子邮件
     */
    private String email;

    /**
     * 行政区划编码
     */
    private String adcd;

    /**
     * 行政区划名称
     */
    private String adnm;

    /**
     * 职务
     */
    private String job;


}
