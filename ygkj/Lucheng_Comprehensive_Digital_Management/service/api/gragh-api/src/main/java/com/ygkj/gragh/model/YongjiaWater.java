package com.ygkj.gragh.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Huangzh
 * @description 永嘉水情表
 * @date 2021-04-12
 */
@Data
@ApiModel("永嘉水情表")
public class YongjiaWater implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * stcdt
     */
    @ApiModelProperty("stcdt")
    private String stcdt;

    /**
     * ymdhm
     */
    @ApiModelProperty("ymdhm")
    private Date ymdhm;

    /**
     * zi
     */
    @ApiModelProperty("zi")
    private Double zi;

    /**
     * qi
     */
    @ApiModelProperty("qi")
    private String qi;

    /**
     * v
     */
    @ApiModelProperty("v")
    private String v;

    /**
     * qog
     */
    @ApiModelProperty("qog")
    private String qog;

    /**
     * zichar
     */
    @ApiModelProperty("zichar")
    private String zichar;

    /**
     * zitend
     */
    @ApiModelProperty("zitend")
    private String zitend;

    /**
     * qmes
     */
    @ApiModelProperty("qmes")
    private String qmes;

    public YongjiaWater() {
    }

}

