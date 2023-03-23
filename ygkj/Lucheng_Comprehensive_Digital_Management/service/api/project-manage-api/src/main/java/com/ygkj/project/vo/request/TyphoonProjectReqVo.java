package com.ygkj.project.vo.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("台风关联工程入参")
public class TyphoonProjectReqVo {

    @ApiModelProperty("类型 1全部 2关联")
    private Integer type;

    @ApiModelProperty("经度")
    private Double lng;

    @ApiModelProperty("纬度")
    private Double lat;

    @ApiModelProperty("东南")
    private Double se;

    @ApiModelProperty("西南")
    private Double sw;

    @ApiModelProperty("东北")
    private Double ne;

    @ApiModelProperty("西北")
    private Double nw;

    @ApiModelProperty("工程类型 水库 山塘 水电站 泵站 水闸 摄像机")
    private String projectType;

    @ApiModelProperty("水库工程等别")
    private Integer projGrad;

    @ApiModelProperty("是否带入工程详情")
    Boolean withProjInfo;
}
