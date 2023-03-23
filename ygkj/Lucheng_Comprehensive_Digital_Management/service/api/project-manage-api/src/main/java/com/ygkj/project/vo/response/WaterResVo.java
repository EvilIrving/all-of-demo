package com.ygkj.project.vo.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
@ApiModel("水位列表出参")
public class WaterResVo {

    @ApiModelProperty("工程编码")
    private String projectCode;

    @ApiModelProperty("工程名称")
    private String projectName;

    @ApiModelProperty("工程类型")
    private String projectType;

    @ApiModelProperty("时间")
    private String tm;

    @ApiModelProperty("水位")
    private BigDecimal waterLevel;

    @ApiModelProperty("警戒水位/汛限水位")
    private BigDecimal warningLevel;

    @ApiModelProperty("经度")
    private Double longitude;

    @ApiModelProperty("纬度")
    private Double latitude;

    @ApiModelProperty("测站编码")
    private String stCode;

    @ApiModelProperty("测站类型")
    private String stType;
}
