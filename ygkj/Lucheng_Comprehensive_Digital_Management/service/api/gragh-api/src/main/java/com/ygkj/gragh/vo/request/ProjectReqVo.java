package com.ygkj.gragh.vo.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ProjectReqVo {

    @ApiModelProperty("类型 1行政区划 2流域")
    private int type;

    @ApiModelProperty("行政区划代码/流域代码")
    private String areaCode;

    @ApiModelProperty("工程名称")
    private String projectName;

    @ApiModelProperty("工程类型 1在建 2规划 3计划 4谋划 5前期")
    private Integer projectType;

    @ApiModelProperty(hidden = true)
    private String tableName;

}
