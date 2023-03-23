package com.ygkj.digitization.vo.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
@ApiModel("实时工情-农村供水工程出参")
public class CwsResVo {

    @ApiModelProperty("供水工程编码")
    private String cwsCode;

    @ApiModelProperty("供水工程名称")
    private String cwsName;

    @ApiModelProperty("供水工程对应的测站编码")
    private String stationCode;

    @ApiModelProperty("经度")
    private BigDecimal cwsLong;

    @ApiModelProperty("纬度")
    private BigDecimal cwsLat;

    @ApiModelProperty("上报时间")
    private String dt;

    @ApiModelProperty("瞬时流量")
    private String waterquantity;

    @ApiModelProperty("余氯")
    private String freeChlorine;

    @ApiModelProperty("浊度")
    private String turbidity;

    @ApiModelProperty("PH值")
    private String pH;

    @ApiModelProperty("水质是否正常")
    private String status;
}
