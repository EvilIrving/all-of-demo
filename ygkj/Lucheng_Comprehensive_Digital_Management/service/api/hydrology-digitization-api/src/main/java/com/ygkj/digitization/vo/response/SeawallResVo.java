package com.ygkj.digitization.vo.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
@ApiModel("海塘列表出参")
public class SeawallResVo extends ProjectResVo {

    @ApiModelProperty("海塘长度")
    private BigDecimal seawallLength;

    @ApiModelProperty("线")
    private BigDecimal points;
}
