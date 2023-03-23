package com.ygkj.big.screen.vo.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author fml
 * @date 2021-04-01 17:43
 * @description 数据统计返回结果
 */
@Data
public class DataStatisticsInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("标题")
    private String txt;

    @ApiModelProperty("时间")
    private String tm;

    @ApiModelProperty("数量")
    private Integer num;

    @ApiModelProperty("数值")
    private Double doubleValue;

    @ApiModelProperty("比率")
    private Double rate;
}
