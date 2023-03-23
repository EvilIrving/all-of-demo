package com.ygkj.data.management.vo.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@ApiModel
@Data
public class StationRuleHistoryRequestVo {

    @ApiModelProperty("起始时间")
    private String start;
    @ApiModelProperty("结束时间")
    private String end;
    @ApiModelProperty("测站编码")
    @NotBlank(message = "请指定一个测站！")
    private String stationCode;
    @ApiModelProperty("测站类型")
    @NotBlank(message = "请指定当前测站类型！")
    private String stationType;
}
