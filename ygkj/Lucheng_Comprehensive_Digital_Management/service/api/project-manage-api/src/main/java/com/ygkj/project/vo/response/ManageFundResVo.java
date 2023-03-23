package com.ygkj.project.vo.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ManageFundResVo {

    @ApiModelProperty("经费数额")
    private BigDecimal foudTotal;

    @ApiModelProperty("经费落实")
    private BigDecimal foudFulfil;

    @ApiModelProperty("季度")
    private String quarter;
}
