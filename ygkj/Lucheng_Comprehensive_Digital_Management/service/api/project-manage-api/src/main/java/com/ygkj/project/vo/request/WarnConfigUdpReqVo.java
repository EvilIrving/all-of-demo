package com.ygkj.project.vo.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class WarnConfigUdpReqVo {

    @ApiModelProperty("配置id")
    private String configId;

    @ApiModelProperty("测站编码")
    private String stCode;

    @ApiModelProperty("降雨历时")
    private String rainfallPeriod;

    @ApiModelProperty("阈值")
    private Double threshold;

    @ApiModelProperty("站点类型")
    private String stType;

    @ApiModelProperty("阈值类型")
    private Double thresholdType;

    @ApiModelProperty("报警条件")
    private String warnCondition;

    @ApiModelProperty("流量阈值")
    private Double flowThreshold;
}
