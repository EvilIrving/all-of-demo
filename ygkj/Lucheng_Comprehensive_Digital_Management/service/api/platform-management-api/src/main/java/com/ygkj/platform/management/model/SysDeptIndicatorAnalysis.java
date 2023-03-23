package com.ygkj.platform.management.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Huangzh
 * @description 部门指标分析记录
 * @date 2021-09-17
 */
@Data
public class SysDeptIndicatorAnalysis implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private Long id;

    /**
     * 部门id
     */
    private String deptId;

    /**
     * 部门名称
     */
    private String department;

    /**
     * 部门总人数
     */
    private Integer totalMember;

    /**
     * 平均完成率
     */
    private Double averageRate;

    /**
     * 最高完成率
     */
    private Double maxRate;

    /**
     * 完成率最高用户
     */
    private String maxUsername;

    /**
     * 完成率最高用户id
     */
    private String maxUserId;

    /**
     * 最低完成率
     */
    private Double minRate;

    /**
     * 完成率最低用户
     */
    private String minUsername;

    /**
     * 完成率最低用户id
     */
    private String minUserId;

    /**
     * 年月
     */
    private String yearMonth;

    /**
     * 趋势
     */
    private boolean trend;

    public SysDeptIndicatorAnalysis() {
    }

}

