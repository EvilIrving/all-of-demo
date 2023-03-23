package com.ygkj.river.vo.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author hucong
 * @description 水美乡镇表
 * @date 2021-08-12
 */
@Data
@ApiModel("水美乡镇表")
@NoArgsConstructor
public class AttBeautVillageRespVo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @ApiModelProperty("主键id")
    private String id;

    /**
     * 所在市
     */
    @ApiModelProperty("所在市")
    private String city;

    /**
     * 所在县区
     */
    @ApiModelProperty("所在县区")
    private String county;

    /**
     * 各市县形象进度(%)
     */
    @ApiModelProperty("各市县形象进度(%)")
    private String progress;

    /**
     * 项目名称
     */
    @ApiModelProperty("项目名称")
    private String projectName;

    /**
     * 工程状态
     */
    @ApiModelProperty("工程状态")
    private String projectStatus;

    /**
     * 河段名称
     */
    @ApiModelProperty("河段名称")
    private String sectionName;

    /**
     * 起点位置
     */
    @ApiModelProperty("起点位置")
    private String startLocation;

    /**
     * 终点位置
     */
    @ApiModelProperty("终点位置")
    private String endLocation;

    /**
     * 项目概况
     */
    @ApiModelProperty("项目概况")
    private String projectOverview;

    /**
     * 计划投资
     */
    @ApiModelProperty("计划投资")
    private String planInvestment;

    /**
     * 完成投资
     */
    @ApiModelProperty("完成投资")
    private String completeInvestment;

    /**
     * 投资比例（%）
     */
    @ApiModelProperty("投资比例（%）")
    private String investmentProportion;

    /**
     * 总体形象进度（%）
     */
    @ApiModelProperty("总体形象进度（%）")
    private String overallProgress;

    /**
     * 任务状态
     */
    @ApiModelProperty("任务状态")
    private String taskStatus;

    /**
     * 起点经度
     */
    @ApiModelProperty("起点经度")
    private String startLng;

    /**
     * 起点纬度
     */
    @ApiModelProperty("起点纬度")
    private String startLat;

}