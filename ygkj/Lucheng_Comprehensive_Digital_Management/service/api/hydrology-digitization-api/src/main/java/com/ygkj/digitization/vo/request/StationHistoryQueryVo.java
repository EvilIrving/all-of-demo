package com.ygkj.digitization.vo.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class StationHistoryQueryVo {

    @ApiModelProperty("测站编码")
    private String stationCode;
    @ApiModelProperty("开始时间 yyyy-MM-dd")
    private String start;
    @ApiModelProperty("结束时间 yyyy-MM-dd")
    private String end;
    @ApiModelProperty("测站类型:RR水库水位站，ZZ河道水位站,PP雨量站，DD闸坝水位站，TT潮位站，ZG地下水位站，ZQ流量站")
    private String stationType;
}
