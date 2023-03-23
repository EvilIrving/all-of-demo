package com.ygkj.flood.vo.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author xq
 * @Description
 * @Date 2021/5/8
 */
@Data
@ApiModel("历史积水与预测积水曲线返回值")
public class FloodPointCurveResVo {

    @ApiModelProperty("历史积水曲线")
    List<ChartResVo<Date, BigDecimal>> historicCurve;

    @ApiModelProperty("预测积水曲线")
    List<ChartResVo<Date, BigDecimal>> forecastCurve;
}
