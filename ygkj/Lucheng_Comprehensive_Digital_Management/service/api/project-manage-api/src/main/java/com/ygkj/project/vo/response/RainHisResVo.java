package com.ygkj.project.vo.response;

import com.ygkj.project.model.StPptnR;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;


@Data
@ApiModel("雨量柱状图出参")
public class RainHisResVo {

    @ApiModelProperty("降雨情况")
    List<StPptnR> list;

    @ApiModelProperty("阈值")
    private BigDecimal threshold;
}
