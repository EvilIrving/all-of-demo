package com.ygkj.project.vo.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ManageInfoResVo {

    @ApiModelProperty("安全鉴定结论")
    private String safetyConclusion;

    @ApiModelProperty("是否加固")
    private String reinforce;

    @ApiModelProperty("险情处置 0无险情 1已处置 2未处置")
    private Integer greatDanger;

    @ApiModelProperty("防汛预案 0未上传 1已上传")
    private Integer floodPlan;

    @ApiModelProperty("防汛预案 0未演练 1已演练")
    private Integer floodDrill;
}
