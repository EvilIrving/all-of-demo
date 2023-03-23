package com.ygkj.gragh.vo.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class BuildProResVo {

    @ApiModelProperty("工程编码")
    private String projectCode;

    @ApiModelProperty("工程名称")
    private String projectName;

    @ApiModelProperty("建设标准")
    private String conStandards;

    @ApiModelProperty("建设内容")
    private String conContent;

    @ApiModelProperty("建设计划")
    private String conPlan;

    @ApiModelProperty("建设质量")
    private String conQuality;

    @ApiModelProperty("总投资")
    private BigDecimal investment;

    @ApiModelProperty("已投资")
    private BigDecimal invested;

    @ApiModelProperty("进度")
    private double investmentRate;
}
