package com.ygkj.big.screen.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class HealthCodeColor {

    /**
     * id
     */
    private String id;

    @ApiModelProperty("工程编码")
    private String prcd;

    @ApiModelProperty("类别")
    private String category;

    @ApiModelProperty("最小值")
    private Integer minVal;

    @ApiModelProperty("最大值")
    private Integer maxVal;
}
