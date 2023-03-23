package com.ygkj.platform.management.model;

import com.ygkj.model.SysPermission;
import lombok.Data;

import java.io.Serializable;

/**
 * @author Huangzh
 * @description 系统访问阈值表
 * @date 2021-09-15
 */
@Data
public class SysThreshold implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private Long id;

    /**
     * 阈值类型：登录/菜单
     */
    private String thresholdType;

    /**
     * 用户id
     */
    private String userId;

    /**
     * 部门id
     */
    private String deptId;

    /**
     * 用户名称
     */
    private String username;

    /**
     * 部门名称
     */
    private String department;

    /**
     * 菜单id
     */
    private String menuId;

    /**
     * 频率
     */
    private Integer frequency;

    /**
     * 频率单位：每月/每日
     */
    private String unit;

    /**
     * 提醒方式：浙政钉/短信/全部
     */
    private String notifyMode;

    private SysPermission menu;

    public SysThreshold() {
    }

    public SysThreshold(String thresholdType, String userId, String deptId,
                        String username, String department,
                        String menuId, Integer frequency, String unit, String notifyMode) {
        this.thresholdType = thresholdType;
        this.userId = userId;
        this.deptId = deptId;
        this.username = username;
        this.department = department;
        this.menuId = menuId;
        this.frequency = frequency;
        this.unit = unit;
        this.notifyMode = notifyMode;
    }
}

