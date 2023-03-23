package com.ygkj.platform.management.vo.response;

import lombok.Data;

@Data
public class SysThresholdResponseVo {

    private long id;

    /**
     * 阈值类型：登录/菜单
     */
    private String thresholdType;

    /**
     * 用户名称
     */
    private String username;

    /**
     * 部门名称
     */
    private String department;

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
}
