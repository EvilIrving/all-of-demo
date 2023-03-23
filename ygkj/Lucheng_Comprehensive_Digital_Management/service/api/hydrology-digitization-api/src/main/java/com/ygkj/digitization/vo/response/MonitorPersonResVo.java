package com.ygkj.digitization.vo.response;

import lombok.Data;

/**
 * @author xq
 * @Description 值班人员返回实体类
 * @Date 2021/1/14
 */
@Data
public class MonitorPersonResVo {

    /**
     * 值班人id，与sys_user.id关联
     */
    private String userId;

    /**
     * 账户名称
     */
    private String username;

    /**
     * 1：值班人，2：副班领导，3：带班领导
     */
    private Integer personType;

    /**
     * 值班人/副班领导/带班领导
     */
    private String personTypeName;

    /**
     * 性别
     */
    private Integer sex;

    /**
     * 男/女
     */
    private String sexName;

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
