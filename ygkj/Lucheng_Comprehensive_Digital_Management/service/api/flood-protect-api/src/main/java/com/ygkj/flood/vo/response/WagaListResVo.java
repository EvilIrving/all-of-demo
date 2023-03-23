package com.ygkj.flood.vo.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
@ApiModel("水闸列表出参")
public class WagaListResVo {

    @ApiModelProperty("水闸编码")
    private String wagaCode;

    @ApiModelProperty(value = "测站编码", hidden = true)
    private String stCode;


    /**
     * 保证水位
     */
    @ApiModelProperty(value = "保证水位")
    private Double guaranteeLevel;

    /**
     * 警戒水位
     */
    @ApiModelProperty(value = "警戒水位")
    private Double warningLevel;

    @ApiModelProperty("水闸名称")
    private String wagaName;

    @ApiModelProperty("经度")
    private BigDecimal startLong;

    @ApiModelProperty("纬度")
    private BigDecimal startLat;

    @ApiModelProperty("时间")
    private String tm;

    @ApiModelProperty("闸上水位")
    private Double upz;

    @ApiModelProperty("闸下水位")
    private Double dwz;

    @ApiModelProperty("闸门开度")
    private String ovs;

    @ApiModelProperty("最大过闸流量")
    private String mxgtq;

    @ApiModelProperty("单宽流量")
    private Double tgtq;

}
