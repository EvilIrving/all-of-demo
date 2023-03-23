package com.ygkj.gragh.vo.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class PlanProResVo {

    @ApiModelProperty("工程名称")
    private String projectName;

    @ApiModelProperty("项目计划")
    private String projectPlan;

    @ApiModelProperty("投资进度")
    private String investmentProgress;

    @ApiModelProperty("投资类型")
    private Integer investmentType;
}
