package com.ygkj.digitization.vo.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class MaxWaterLevelQueryVo {
    @ApiModelProperty("年份")
    private String year;
    @ApiModelProperty("月份")
    private String month;
    @ApiModelProperty("测站类型：RR水库水位站，ZZ河道水位站,PP雨量站，DD闸坝水位站，TT潮位站，ZG地下水位站，ZQ流量站")
    private String stationType;
    @ApiModelProperty("行政区划编码")
    private String areaCode;
    @ApiModelProperty("旬月：全部/上旬/中旬/下旬")
    private String days;
}
