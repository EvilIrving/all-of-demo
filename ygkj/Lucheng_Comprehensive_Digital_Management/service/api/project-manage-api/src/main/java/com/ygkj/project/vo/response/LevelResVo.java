package com.ygkj.project.vo.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class LevelResVo {

    @ApiModelProperty("降雨量/水位")
    private BigDecimal value;

    @ApiModelProperty("时间")
    private String time;
}
