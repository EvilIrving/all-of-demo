package com.ygkj.big.screen.vo.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class HystDropPoint {

    @ApiModelProperty("水电站名称")
    private String hystName;

    /**
     * 电站编码
     */
    @ApiModelProperty("电站编码")
    private String hystCode;


    @ApiModelProperty("水电站经度")
    private Double hystLong;


    @ApiModelProperty("水电站纬度")
    private Double hystLat;


    @ApiModelProperty("水电站所在位置")
    private String hystLoc;


    @ApiModelProperty("水电站类型")
    private String hystType;

    @ApiModelProperty("装机流量")
    private String insCap;

    /**
     * 设备是否在线
     */
    @ApiModelProperty("设备是否在线")
    private String equipmentOnline;

}
