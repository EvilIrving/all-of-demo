package com.ygkj.river.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @author hucong
 * @description 中小河流表
 * @date 2021-08-12
 */
@Data
public class AttSmallMediumRiver implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 序号
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
     * 省级年度任务
     */
    private String provincialAnnualTask;

    /**
     * 中央年度任务
     */
    private String centralAnnualMission;

    /**
     * 项目类别
     */
    private String projectCategory;

    /**
     * 项目概况
     */
    private String projectOverview;

    /**
     * 子项目名称
     */
    private String subprojectName;

    /**
     * 工程状态
     */
    private String projectStatus;

    /**
     * 流域水系
     */
    private String riverWaterSystem;

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
     * 省级任务
     */
    private String provinceTask;

    /**
     * 中央任务
     */
    private String centralTask;

    /**
     * 民生实事任务
     */
    private String practicalTasks;

    /**
     * 项目总投资
     */
    private String totalProjectInvestment;

    /**
     * 完成总投资
     */
    private String completeProjectInvestment;

    /**
     * 投资比例（%）
     */
    private String investmentProportion;

    /**
     * 完成省级资金
     */
    private String completeProvincialFund;

    /**
     * 完成中央资金
     */
    private String completeCentralFund;

    /**
     * 完成地方安排资金
     */
    private String completeLocalgovernmentFund;

    /**
     * 总体形象进度（%）
     */
    private String overallProgress;

    /**
     * 任务状态
     */
    private String taskStatus;

    public AttSmallMediumRiver() {
    }

}