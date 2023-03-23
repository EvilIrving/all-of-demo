package com.ygkj.big.screen.vo.response;

import com.ygkj.big.screen.model.WiuUseMonth;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 取水户取水量
 */
@Data
public class WuWaterUseRespVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("年用水总量")
    private Double yearUse;

    @ApiModelProperty("月用水量")
    private List<WiuUseMonth> wiuUseMonths;
}
