package com.ygkj.gragh.vo.response;

import com.ygkj.gragh.model.WiuUse;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class WiuUseResVo extends WiuUse {

    @ApiModelProperty("当年计划量")
    private BigDecimal watPlan;
}
