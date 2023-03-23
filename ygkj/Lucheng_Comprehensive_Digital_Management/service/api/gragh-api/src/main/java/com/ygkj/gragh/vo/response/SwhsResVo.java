package com.ygkj.gragh.vo.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class SwhsResVo {

    @ApiModelProperty("水源地编码")
    private String swhsCode;

    @ApiModelProperty("水源地名称")
    private String swhsName;

    @ApiModelProperty("水源地经度")
    private BigDecimal swhsLong;

    @ApiModelProperty("水源地纬度")
    private BigDecimal swhsLat;

    @ApiModelProperty("取水水源类型")
    private String wainWasoType;

    @ApiModelProperty("最大日供水能力")
    private String dateMaxWat;

    @ApiModelProperty("服务人口")
    private BigDecimal desAnnWasuPop;
}
