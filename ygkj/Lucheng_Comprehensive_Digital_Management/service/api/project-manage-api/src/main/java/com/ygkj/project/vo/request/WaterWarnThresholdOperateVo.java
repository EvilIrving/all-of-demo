package com.ygkj.project.vo.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class WaterWarnThresholdOperateVo {

    @ApiModelProperty("测站编码,多个以逗号分隔")
    private String stCodes;

    @ApiModelProperty("测站类型")
    private String stType;

    @ApiModelProperty("阈值类型")
    private String thresholdType;
}
