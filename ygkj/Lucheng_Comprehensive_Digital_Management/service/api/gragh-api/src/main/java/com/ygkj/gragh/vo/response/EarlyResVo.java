package com.ygkj.gragh.vo.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class EarlyResVo {

    @ApiModelProperty("工程名称")
    private String projectName;

    @ApiModelProperty("建设内容")
    private String conContent;

    @ApiModelProperty("项目计划")
    private String projectPlan;

    @ApiModelProperty("项目节点/完成时间")
    private String deadline;

    @ApiModelProperty("所处节点进度")
    private String progressRate;
}
