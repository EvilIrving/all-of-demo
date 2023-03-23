package com.ygkj.flood.vo.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class WlaHisHighestWaterLevelRespVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("测站编码")
    private String stCode;

    @ApiModelProperty("测站名")
    private String stName;

    @ApiModelProperty("测站类型")
    private String stType;

    @ApiModelProperty("历史最高水位")
    private Double highestLevel;

    @ApiModelProperty("历史最高水位发生时间")
    private String appearTime;

    @ApiModelProperty("经度")
    private Double stLong;

    @ApiModelProperty("纬度")
    private Double stLat;
}
