package com.ygkj.project.model;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author xq
 * @description 水库洪水预报记录表
 * @date 2021-07-04
 */
@Data
@ApiModel("水库洪水预报记录表")
public class RsvrFloodPredict implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 水库编码
     */
    @ApiModelProperty("水库编码")
    private String resCode;

    /**
     * forecast_time
     */
    @ApiModelProperty("forecast_time")
    private Date forecastTime;

    /**
     * actual_water
     */
    @ApiModelProperty("actual_water")
    private String actualWater;

    /**
     * rain
     */
    @ApiModelProperty("rain")
    private String rain;

    /**
     * out_flow
     */
    @ApiModelProperty("out_flow")
    private String outFlow;

    /**
     * limit_level
     */
    @ApiModelProperty("limit_level")
    private String limitLevel;

    /**
     * water
     */
    @ApiModelProperty("water")
    private String water;

    /**
     * flow
     */
    @ApiModelProperty("flow")
    private String flow;

    public RsvrFloodPredict() {
    }

}
