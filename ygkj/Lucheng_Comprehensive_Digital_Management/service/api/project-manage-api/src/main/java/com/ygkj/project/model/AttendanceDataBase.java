package com.ygkj.project.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @description 考勤数据表
 * @author lxl
 * @date 2022-07-05
 */
@Data
public class AttendanceDataBase {

    /**
     * 主键
     */
    private String id;

    /**
     * tenant_id
     */
    private String tenantId;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 创建用户
     */
    private String createUser;

    /**
     * 名称
     */
    private String name;

    /**
     * 部门名称
     */
    private String departmentName;

    /**
     * 工号
     */
    private String jobNumber;

    /**
     * 组织名称
     */
    private String corporationName;

    /**
     * cpu id
     */
    private String cpuId;

    /**
     * 设备名称
     */
    private String deviceName;

    /**
     * 温度
     */
    private String temperature;

    /**
     * 检查方式
     */
    private String checkingType;

    /**
     * 检查描述
     */
    private String checkingDesc;

    /**
     * 考勤时间
     */
    private Date attendanceTime;

    public AttendanceDataBase() {
    }

}
