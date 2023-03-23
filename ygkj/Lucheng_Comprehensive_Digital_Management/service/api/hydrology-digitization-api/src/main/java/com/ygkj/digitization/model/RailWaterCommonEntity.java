package com.ygkj.digitization.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class RailWaterCommonEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("时间")
    private String time;
    @ApiModelProperty("数据")
    private String data;
}
