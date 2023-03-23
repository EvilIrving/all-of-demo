package com.ygkj.big.screen.vo.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 水质测站相关水质信息
 */
@Data
public class RpCwsStasRealDataInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("水质测站编码")
    private String stationCode;

    @ApiModelProperty("ph")
    private String pH;

    @ApiModelProperty("浊度")
    private String turbidity;

    @ApiModelProperty("dt")
    private Date dt;

    @ApiModelProperty("余氯")
    private String freeChlorine;

    @ApiModelProperty("1正常、0不正常")
    private String status = "1";
}
