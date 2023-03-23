package com.ygkj.river.vo.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@ApiModel("水域变化C位统计回参")
@NoArgsConstructor
@AllArgsConstructor
public class PositionStatisticsWaterArea implements Serializable {

    @ApiModelProperty("水域面积")
    private BigDecimal waterArea;

    @ApiModelProperty("占用面积（㎡）")
    private BigDecimal occupiedArea;

    @ApiModelProperty("补偿面积（㎡）")
    private BigDecimal compensationArea;

    @ApiModelProperty("水面率")////水域面积除以温州市总面积
    private BigDecimal waterRate;
}
