package com.ygkj.digitization.vo.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class RelationStatisticQueryVo {
    @ApiModelProperty("测站编码")
    private String stationCodes;
    @ApiModelProperty("开始时间 yyyy-MM-dd")
    private String start;
    @ApiModelProperty("结束时间 yyyy-MM-dd")
    private String end;
    @ApiModelProperty("统计类型：水位/雨量/水质/流量")
    private String statisticType;
    @ApiModelProperty("水位测站类型：河道水位站:ZZ 水库水位站:RR")
    private String stationType;
}
