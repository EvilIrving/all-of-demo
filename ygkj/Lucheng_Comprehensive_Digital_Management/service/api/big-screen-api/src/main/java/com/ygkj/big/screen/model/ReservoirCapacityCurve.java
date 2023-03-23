package com.ygkj.big.screen.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author fml
 * @date 2021-03-30 18:45
 * @description 水库库容曲线
 */
@Data
@ApiModel("水库库容曲线")
public class ReservoirCapacityCurve implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("id")
    private Long id;


    @ApiModelProperty("测站编码")
    private String code;


    @ApiModelProperty("水位")
    private BigDecimal waterLevel;


    @ApiModelProperty("测站对应的水库库容")
    private BigDecimal capacity;
}
