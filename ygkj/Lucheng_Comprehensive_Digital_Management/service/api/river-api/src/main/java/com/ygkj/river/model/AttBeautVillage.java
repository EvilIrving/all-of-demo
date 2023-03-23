package com.ygkj.river.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @author hucong
 * @description 水美乡镇表
 * @date 2021-08-12
 */
@Data
public class AttBeautVillage implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    private String id;

    /**
     * 所在市
     */
    private String city;

    /**
     * 所在县区
     */
    private String county;

    /**
     * 各市县形象进度(%)
     */
    private String progress;

    /**
     * 项目名称
     */
    private String projectName;

    /**
     * 工程状态
     */
    private String projectStatus;

    /**
     * 河段名称
     */
    private String sectionName;

    /**
     * 起点位置
     */
    private String startLocation;

    /**
     * 终点位置
     */
    private String endLocation;

    /**
     * 项目概况
     */
    private String projectOverview;

    /**
     * 计划投资
     */
    private String planInvestment;

    /**
     * 完成投资
     */
    private String completeInvestment;

    /**
     * 投资比例（%）
     */
    private String investmentProportion;

    /**
     * 总体形象进度（%）
     */
    private String overallProgress;

    /**
     * 任务状态
     */
    private String taskStatus;

    public AttBeautVillage() {
    }

}
