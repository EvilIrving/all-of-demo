package com.ygkj.big.screen.vo.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 取水户统计信息
 */
@Data
public class WuWiuStatsRespVo implements Serializable {

    private static final long serialVersionUID = 1L;

    public WuWiuStatsRespVo() {
    }

    public WuWiuStatsRespVo(String text, Integer num, Double value) {
        this.text = text;
        this.num = num;
        this.value = value;
    }

    @ApiModelProperty("统计内容")
    private String text;

    @ApiModelProperty("数量")
    private Integer num;

    @ApiModelProperty("数值")
    private Double value;
}
