package com.ygkj.lcsyn.model;

import java.io.Serializable;
import lombok.Data;
import java.util.Date;
import java.util.List;

/**
 * @description 考勤数据表
 * @author lxl
 * @date 2022-07-05
 */
@Data
public class AttendanceDataBase implements Serializable {

    private static final long serialVersionUID = 1L;

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
