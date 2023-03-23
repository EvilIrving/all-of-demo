package com.ygkj.big.screen.vo.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class FlashFloodDetailsResVo implements Serializable {
    @ApiModelProperty("村名")
    private String avi;
    @ApiModelProperty("负责人姓名")
    private String chargerName;
    @ApiModelProperty("负责人电话")
    private String chargerPhone;
    @ApiModelProperty("测站编码")
    private String stationCode;
    @ApiModelProperty("测站名称")
    private String stationName;
    @ApiModelProperty("时段")
    private BigDecimal period;
    @ApiModelProperty("准备转移")
    private BigDecimal readyMove;
    @ApiModelProperty("立即转移")
    private BigDecimal immediateTransfer;
    @ApiModelProperty("户数")
    private String tho;
    @ApiModelProperty("人数")
    private String tpo;
    @ApiModelProperty("实时降雨")
    private String realTimeRainfall;
}
