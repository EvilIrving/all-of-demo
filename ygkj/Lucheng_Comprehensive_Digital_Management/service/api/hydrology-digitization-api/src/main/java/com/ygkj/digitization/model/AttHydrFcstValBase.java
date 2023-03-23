package com.ygkj.digitization.model;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author xq
 * @description 水文预报值表
 * @date 2021-09-22
 */
@Data
@ApiModel("水文预报值表")
public class AttHydrFcstValBase implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 预报id
     */
    @ApiModelProperty("预报id")
    private String fcstId;

    /**
     * 时刻
     */
    @ApiModelProperty("时刻")
    private Date tm;

    /**
     * 测站编码
     */
    @ApiModelProperty("测站编码")
    private String stCode;

    /**
     * 水位
     */
    @ApiModelProperty("水位")
    private BigDecimal level;

    /**
     * 流量
     */
    @ApiModelProperty("流量")
    private BigDecimal flow;

    public AttHydrFcstValBase() {
    }

}

