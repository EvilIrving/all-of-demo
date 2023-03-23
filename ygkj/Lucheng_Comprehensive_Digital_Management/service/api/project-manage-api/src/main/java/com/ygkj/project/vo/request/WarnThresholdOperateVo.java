package com.ygkj.project.vo.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class WarnThresholdOperateVo {

    @ApiModelProperty("测站编码,多个以逗号分隔")
    private String stCodes;

    @ApiModelProperty("降雨历时")
    private String rainfallPeriod;

    @ApiModelProperty("设定的阈值")
    private Double threshold;

}
