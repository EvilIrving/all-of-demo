package com.ygkj.gragh.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Huangzh
 * @description 永嘉雨量
 * @date 2021-04-12
 */
@Data
@ApiModel("永嘉雨量")
public class YongjiaRain implements Serializable {

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
     * dtrm
     */
    @ApiModelProperty("dtrn")
    private String dtrn;

    /**
     * rntm
     */
    @ApiModelProperty("rntm")
    private String rntm;

    /**
     * dyrn
     */
    @ApiModelProperty("dyrn")
    private String dyrn;

    /**
     * wth
     */
    @ApiModelProperty("wth")
    private String wth;

    /**
     * rntype
     */
    @ApiModelProperty("rntype")
    private String rntype;

    public YongjiaRain() {
    }

}

