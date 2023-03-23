package com.ygkj.digitization.vo.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("实时报表---实时工情接口出参")
public class GateStationResVo {
    @ApiModelProperty("测站编码")
    private String code;
    @ApiModelProperty("测站名称")
    private String name;
    @ApiModelProperty("时间")
    private String tm;
    @ApiModelProperty("行政区划编码")
    private String areaCode;
    @ApiModelProperty("行政区划名称")
    private String areaName;
    @ApiModelProperty("保证水位")
    private Double guaranteeLevel;
    @ApiModelProperty("警戒水位")
    private Double warningLevel;
    @ApiModelProperty("闸上水位")
    private Double upWater;
    @ApiModelProperty("闸下水位")
    private Double downWater;
    @ApiModelProperty("过闸流量")
    private String tgtq;
    @ApiModelProperty("闸下开度")
    private String openGate;
}
