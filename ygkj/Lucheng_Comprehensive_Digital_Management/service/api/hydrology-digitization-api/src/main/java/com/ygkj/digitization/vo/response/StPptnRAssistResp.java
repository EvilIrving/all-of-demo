package com.ygkj.digitization.vo.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class StPptnRAssistResp implements Serializable {

    @ApiModelProperty("时间")
    private String time;
    @ApiModelProperty("累计雨量数据")
    private Double data;
    @ApiModelProperty("行政区划编码")
    private String areaCode;
    @ApiModelProperty("行政区划名称")
    private String areaName;
    @ApiModelProperty("测站编码")
    private String stcd;
    @ApiModelProperty("测站名称")
    private String stnm;
    @ApiModelProperty("显示时间")
    private String timeFormat;

}
