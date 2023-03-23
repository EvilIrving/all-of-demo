package com.ygkj.big.screen.vo.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 取水曲线信息
 */
@Data
public class WuWiuUseCurveRespVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("月份")
    private String month;

    @ApiModelProperty("用水量")
    private Double totalUse;
}
