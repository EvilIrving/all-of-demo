package com.ygkj.project.vo.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel("工程行政区划统计")
public class ProjectAreaStatic implements Serializable {

    @ApiModelProperty("行政区划名称")
    private String areaName;
    @ApiModelProperty("行政区划编码")
    private String areaCode;
    @ApiModelProperty("数量")
    private Integer num;

}
