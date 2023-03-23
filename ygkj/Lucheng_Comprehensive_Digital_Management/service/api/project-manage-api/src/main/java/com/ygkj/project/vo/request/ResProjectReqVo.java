package com.ygkj.project.vo.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("关联工程入参")
public class ResProjectReqVo {

    @ApiModelProperty("测站编码")
    private String stCode;

    @ApiModelProperty("距离(km)")
    private double distance;

    @ApiModelProperty("工程名称")
    private String projectName;

    @ApiModelProperty("工程类型 水库 山塘 水电站 泵站 水闸 摄像机")
    private String projectType;

    @ApiModelProperty("经度")
    private Double stLong;

    @ApiModelProperty("纬度")
    private Double stLat;

    @ApiModelProperty("工程等别")
    private Integer projGrad;

}
