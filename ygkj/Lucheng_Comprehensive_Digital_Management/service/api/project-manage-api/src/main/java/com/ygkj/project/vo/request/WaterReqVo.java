package com.ygkj.project.vo.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class WaterReqVo {

    @ApiModelProperty("行政区划")
    private String areaCode;

    @ApiModelProperty("工程类型")
    private String projectType;

    @ApiModelProperty("工程名称")
    private String projectName;

    @ApiModelProperty(value = "工程类型", hidden = true)
    private List<String> typeList;
}
