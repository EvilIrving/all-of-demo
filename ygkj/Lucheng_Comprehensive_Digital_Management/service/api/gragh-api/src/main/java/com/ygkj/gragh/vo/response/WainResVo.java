package com.ygkj.gragh.vo.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class WainResVo {

    @ApiModelProperty("取水口编码")
    private String wainCode;

    @ApiModelProperty("取水口名称")
    private String wainName;

    @ApiModelProperty("取水口经度")
    private BigDecimal wainLong;

    @ApiModelProperty("取水口纬度")
    private BigDecimal wainLat;
}
