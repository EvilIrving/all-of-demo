package com.ygkj.platform.management.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Huangzh
 * @description 用户指标完成情况记录表
 * @date 2021-09-17
 */
@Data
public class SysUserIndicatorAnalysis implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private Long id;

    /**
     * 用户id
     */
    private String userId;

    /**
     * 用户名称
     */
    private String username;

    /**
     * 部门id
     */
    private String deptId;

    /**
     * 部门
     */
    private String department;

    /**
     * 指标id
     */
    private Long thresholdId;

    /**
     * 完成次数
     */
    private Integer finishNumber;

    /**
     * 总共次数
     */
    private Integer requestNumber;

    /**
     * 阈值类型：本月/本日
     */
    private String thresholdType;

    /**
     * 日指标分析日期
     */
    private Date analyzeDate;

    /**
     * 月指标所属年月
     */
    private String analyzeYearMonth;

    /**
     * 完成情况：已完成/未完成
     */
    private String status;

    public SysUserIndicatorAnalysis() {
    }

}

