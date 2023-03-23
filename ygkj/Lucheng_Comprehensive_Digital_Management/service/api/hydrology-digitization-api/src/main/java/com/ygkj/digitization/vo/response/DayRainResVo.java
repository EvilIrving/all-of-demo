package com.ygkj.digitization.vo.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class DayRainResVo {

    @ApiModelProperty("日期")
    private String date;

    @ApiModelProperty("降雨量")
    private Double drp;
}
