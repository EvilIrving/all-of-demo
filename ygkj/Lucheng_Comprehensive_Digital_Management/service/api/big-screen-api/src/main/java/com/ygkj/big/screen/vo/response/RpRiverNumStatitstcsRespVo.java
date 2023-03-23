package com.ygkj.big.screen.vo.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author fml
 * @date 2021-03-22 17:43
 * @description 河道数量统计
 */
@Data
public class RpRiverNumStatitstcsRespVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("河道等级(干流、1级、2级、3级、4级、5级及以下)")
    private String riverLevel;

    @ApiModelProperty("数量")
    private Integer num;

    @ApiModelProperty("占比")
    private Double rate;
}
