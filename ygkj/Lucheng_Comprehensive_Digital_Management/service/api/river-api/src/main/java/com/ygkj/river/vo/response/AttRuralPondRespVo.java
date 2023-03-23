package com.ygkj.river.vo.response;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * @author hucong
 * @description 农村池塘表
 * @date 2021-08-12
 */
@Data
@ApiModel("农村池塘表")
@NoArgsConstructor
public class AttRuralPondRespVo implements Serializable {

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
     * 池塘名称
     */
    @ApiModelProperty("池塘名称")
    private String poudName;

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
     * 月度工作简述
     */
    @ApiModelProperty("月度工作简述")
    private String monthDescription;

    /**
     * 任务状态
     */
    @ApiModelProperty("任务状态")
    private String taskStatus;

    /**
     * 经度
     */
    @ApiModelProperty("经度")
    private String lng;

    /**
     * 纬度
     */
    @ApiModelProperty("纬度")
    private String lat;

}
