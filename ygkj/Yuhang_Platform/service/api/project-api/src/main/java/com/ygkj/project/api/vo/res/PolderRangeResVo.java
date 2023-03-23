package com.ygkj.project.api.vo.res;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class PolderRangeResVo {

    @ApiModelProperty("工程编码")
    private String prcd;

    @ApiModelProperty("工程名称")
    private String name;

    @ApiModelProperty("圩区面积")
    private Double shapeArea;

    @ApiModelProperty("行政区划编码")
    private String adcd;

    @ApiModelProperty("行政区划名称")
    private String adnm;

    @ApiModelProperty("圩区长度")
    private Double shapeLeng;

    @ApiModelProperty("工程规模")
    private Double projectScale;

    @ApiModelProperty("工程等别")
    private Double prlv;

    @ApiModelProperty("工程范围线")
    private String rangeLine;

    @ApiModelProperty("经度")
    private String lgtd;

    @ApiModelProperty("纬度")
    private String lttd;
}
