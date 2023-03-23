package com.ygkj.digitization.vo.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@ApiModel("实时报表---实时水位接口出参")
public class WaterLevelStationResVo {

    @ApiModelProperty("测站编码")
    private String stCode;
    @ApiModelProperty("测站名称")
    private String stName;
    @ApiModelProperty("行政区划编码")
    private String areaCode;
    @ApiModelProperty("行政区划名称")
    private String areaName;

    private String projectCode;
    @ApiModelProperty("当前水位")
    private double waterLevels;
    @ApiModelProperty("梅汛限水位")
    private BigDecimal meiFloodLimitWaterLevel;
    @ApiModelProperty("台汛限水位")
    private BigDecimal yphoonFloodLimitWaterLevel;
    @ApiModelProperty("防洪高水位")
    private BigDecimal uppLevFlco;
    @ApiModelProperty("正常水位")
    private BigDecimal normWatLev;
    @ApiModelProperty("集雨面积")
    private BigDecimal watShedArea;
    @ApiModelProperty("保证水位")
    private Double guaranteeLevel;
    @ApiModelProperty("警戒水位")
    private Double warningLevel;
    @ApiModelProperty("时间")
    private String tm;
}
