package com.ygkj.project.vo.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class CapacityWarnThresholdOperateVo {

    @ApiModelProperty("测站编码,多个以逗号分隔")
    private String stCodes;

    @ApiModelProperty("平均蓄水量")
    private Double avgCapacity;
}
