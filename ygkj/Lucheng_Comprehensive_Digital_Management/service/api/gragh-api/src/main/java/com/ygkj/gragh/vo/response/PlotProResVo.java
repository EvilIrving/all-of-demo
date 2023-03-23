package com.ygkj.gragh.vo.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class PlotProResVo {

    @ApiModelProperty("工程名称")
    private String projectName;

    @ApiModelProperty("投资估算（万元）")
    private BigDecimal estimateInvestment;

    @ApiModelProperty("解决问题")
    private String solveProblem;
}
