package com.ygkj.flood.vo.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("大中型水库蓄水率出参")
public class AllStorageResVo {

    @ApiModelProperty("蓄水率")
    private String rate;

    @ApiModelProperty("库容容量")
    private Double totCap;

    @ApiModelProperty("蓄水总量")
    private Double currentCap;

    @ApiModelProperty("可蓄水总量")
    private Double residueCap;
}
