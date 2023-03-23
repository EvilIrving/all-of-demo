package com.ygkj.river.vo.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@ApiModel("水域面积柱状图回参")
@NoArgsConstructor
@AllArgsConstructor
public class HistogramWaterArea implements Serializable {

    @ApiModelProperty("类型 1:河道水域面 2:其他水域水域面 3:人工水道水域面 4:山塘水域面 5:水库水域面 6:总面积")
    private String type;
    @ApiModelProperty("水域面积")
    private BigDecimal waterArea;

}
