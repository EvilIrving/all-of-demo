package com.ygkj.flood.vo.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class WlaFloatWinWaterLevelRespVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("测站编码")
    private String stCode;

    @ApiModelProperty("测站名")
    private String stName;

    @ApiModelProperty("测站类型")
    private String stType;

    @ApiModelProperty("水文监测站经度")
    private String stLong;

    @ApiModelProperty("水文监测站纬度")
    private String stLat;

    @ApiModelProperty("汛限水位")
    private Double limitLevel;

    @ApiModelProperty("防洪高水位")
    private Double floodLevel;

    @ApiModelProperty("实时水位")
    private Double z;

    @ApiModelProperty("1超汛限")
    private String warnStatus;
}
