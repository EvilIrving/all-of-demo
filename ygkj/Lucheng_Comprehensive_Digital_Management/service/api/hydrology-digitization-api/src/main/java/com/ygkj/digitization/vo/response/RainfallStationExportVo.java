package com.ygkj.digitization.vo.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("实时雨量导出返回数据")
public class RainfallStationExportVo {

    @ApiModelProperty("测站名称")
    private String stName;

    @ApiModelProperty("行政区划")
    private String areaName;

    @ApiModelProperty("测站编码")
    private String stCode;

    @ApiModelProperty("行政区划编码")
    private String areaCode;

    @ApiModelProperty("01时")
    public String hour01;

    @ApiModelProperty("02时")
    public String hour02;

    @ApiModelProperty("03时")
    public String hour03;

    @ApiModelProperty("04时")
    public String hour04;

    @ApiModelProperty("05时")
    public String hour05;

    @ApiModelProperty("06时")
    public String hour06;

    @ApiModelProperty("07时")
    public String hour07;

    @ApiModelProperty("08时")
    public String hour08;
    @ApiModelProperty("09时")
    public String hour09;

    @ApiModelProperty("10时")
    public String hour10;

    @ApiModelProperty("11时")
    public String hour11;

    @ApiModelProperty("12时")
    public String hour12;

    @ApiModelProperty("13时")
    public String hour13;

    @ApiModelProperty("14时")
    public String hour14;

    @ApiModelProperty("15时")
    public String hour15;

    @ApiModelProperty("16时")
    public String hour16;
    @ApiModelProperty("17时")
    public String hour17;
    @ApiModelProperty("18时")
    public String hour18;
    @ApiModelProperty("19时")
    public String hour19;
    @ApiModelProperty("20时")
    public String hour20;
    @ApiModelProperty("21时")
    public String hour21;
    @ApiModelProperty("22时")
    public String hour22;
    @ApiModelProperty("23时")
    public String hour23;
    @ApiModelProperty("24时")
    public String hour24;

}
