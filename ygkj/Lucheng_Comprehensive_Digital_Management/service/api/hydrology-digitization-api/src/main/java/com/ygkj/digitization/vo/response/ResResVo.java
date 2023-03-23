package com.ygkj.digitization.vo.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@ApiModel("水库列表出参")
@Data
public class ResResVo extends ProjectResVo {

    @ApiModelProperty("总库容")
    private BigDecimal totCat;

    @ApiModelProperty("经度")
    private BigDecimal lowLeftLong;

    @ApiModelProperty("纬度")
    private BigDecimal lowLeftLat;
}
