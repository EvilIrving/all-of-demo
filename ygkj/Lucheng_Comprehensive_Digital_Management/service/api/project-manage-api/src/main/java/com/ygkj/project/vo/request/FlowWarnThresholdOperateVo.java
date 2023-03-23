package com.ygkj.project.vo.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class FlowWarnThresholdOperateVo {

    @ApiModelProperty("测站编码,多个以逗号分隔")
    private String stCodes;

    @ApiModelProperty("报警条件")
    private String warnCondition;

    @ApiModelProperty("流量阈值m³/s")
    private Double flowThreshold;
}
