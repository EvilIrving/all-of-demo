package com.ygkj.river.vo.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author hucong
 * @description 美丽河湖表
 * @date 2021-08-12
 */
@Data
@ApiModel("美丽河湖表")
@NoArgsConstructor
public class AttBeautLakesRespVo implements Serializable {

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
     * 所在县区r
     */
    @ApiModelProperty("所在县区r")
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
     * 流域水系
     */
    @ApiModelProperty("流域水系")
    private String riverSystem;

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
     * 河长名称
     */
    @ApiModelProperty("河长名称")
    private String riverManagerName;

    /**
     * 河长职务
     */
    @ApiModelProperty("河长职务")
    private String riverManagerPosition;

    /**
     * 河湖类型
     */
    @ApiModelProperty("河湖类型")
    private String lakeType;

    /**
     * 项目概况
     */
    @ApiModelProperty("项目概况")
    private String projectOverview;

    /**
     * 计划投资
     */
    @ApiModelProperty("计划投资")
    private String planInvest;

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
     * 计划规模
     */
    @ApiModelProperty("计划规模")
    private String planScale;

    /**
     * 完成规模
     */
    @ApiModelProperty("完成规模")
    private String completeScale;

    /**
     * 规模比例（%）
     */
    @ApiModelProperty("规模比例（%）")
    private String scaleRatio;

    /**
     * 总体形象进度(%)
     */
    @ApiModelProperty("总体形象进度(%)")
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