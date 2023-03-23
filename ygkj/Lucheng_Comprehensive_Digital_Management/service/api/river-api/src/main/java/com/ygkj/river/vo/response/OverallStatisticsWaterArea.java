package com.ygkj.river.vo.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@ApiModel("水域面积总体统计回参")
@NoArgsConstructor
@AllArgsConstructor
public class OverallStatisticsWaterArea implements Serializable {

    @ApiModelProperty("水域面积")
    private BigDecimal waterArea;
    @ApiModelProperty("水面率")////水域面积除以温州市总面积
    private BigDecimal waterRate;

}
