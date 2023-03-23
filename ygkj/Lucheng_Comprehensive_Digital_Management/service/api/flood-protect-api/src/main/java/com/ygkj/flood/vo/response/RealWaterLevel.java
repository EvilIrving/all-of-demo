package com.ygkj.flood.vo.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author xq
 * @Date 2021/3/16
 */
@Data
@ApiModel("实时水位")
public class RealWaterLevel {

    @ApiModelProperty("测站编码")
    String stCode;

    @ApiModelProperty("测站类型")
    String stType;

    @ApiModelProperty("测站名称")
    String stName;

    @ApiModelProperty("保证水位")
    Double guaranteeLevel;

    @ApiModelProperty("警戒水位")
    Double warningLevel;

    @ApiModelProperty("实时水位")
    Double realLevel;

    @ApiModelProperty("经度")
    BigDecimal longitude;

    @ApiModelProperty("纬度")
    BigDecimal latitude;

    @ApiModelProperty("超警戒值")
    Double overWarnLevel;

    @ApiModelProperty("是否超警戒")
    boolean warning = false;
}
