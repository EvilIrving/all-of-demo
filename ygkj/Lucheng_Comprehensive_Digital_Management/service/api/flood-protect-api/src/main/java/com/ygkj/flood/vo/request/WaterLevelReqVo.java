package com.ygkj.flood.vo.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class WaterLevelReqVo {

    @ApiModelProperty(value = "行政区划编码")
    private String areaCode;

    @ApiModelProperty(value = "所属流域编码")
    private String basinCode;

    @ApiModelProperty(value = "测站名称")
    private String stationName;

    @ApiModelProperty(value = "测站类型：PP雨量站，RR水库水位站,ZZ河道水位站,DD堰闸")
    private String stationType;

    @ApiModelProperty(value = "是否超警")
    private boolean warning;
}
