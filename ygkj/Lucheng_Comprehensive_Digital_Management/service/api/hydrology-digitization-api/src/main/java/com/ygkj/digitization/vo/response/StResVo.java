package com.ygkj.digitization.vo.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
@ApiModel("测站列表出参")
public class StResVo {

    @ApiModelProperty("测站编码")
    private String stCode;

    @ApiModelProperty("测站名称")
    private String stType;

    @ApiModelProperty("测站名称")
    private String stName;

    @ApiModelProperty("流域")
    private String bas;

    @ApiModelProperty("行政区划")
    private String adcd;

    @ApiModelProperty("行政区划名称")
    private String adnm;

    @ApiModelProperty("经度")
    private BigDecimal stLong;

    @ApiModelProperty("纬度")
    private BigDecimal stLat;
}
