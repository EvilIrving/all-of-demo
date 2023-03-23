package com.ygkj.big.screen.vo.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 雨量分析
 */
@Data
public class RpRainfallRespVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("测站编码")
    private String stCode;
    @ApiModelProperty("测站名")
    private String stName;
    @ApiModelProperty("流域")
    private String bas;
    @ApiModelProperty("行政区划")
    private String areaCode;
    @ApiModelProperty("行政区划名")
    private String adnm;
    @ApiModelProperty("1h雨量")
    private Double oneHour;
    @ApiModelProperty("3h雨量")
    private Double threeHour;
    @ApiModelProperty("6h雨量")
    private Double sixHour;
    @ApiModelProperty("24h雨量")
    private Double oneDay;
    @ApiModelProperty("72h雨量")
    private Double threeDay;
}
