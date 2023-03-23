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
 * @description 风暴潮预报值表
 * @date 2021-09-22
 */
@Data
@ApiModel("风暴潮预报值表")
public class AttTideFcstValBase implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 预报id
     */
    @ApiModelProperty("预报id")
    private String fcstId;

    /**
     * 潮位站编码
     */
    @ApiModelProperty("潮位站编码")
    private String stCode;

    /**
     * 时刻
     */
    @ApiModelProperty("时刻")
    private Date tm;

    /**
     * 潮位
     */
    @ApiModelProperty("潮位")
    private BigDecimal tdz;

    public AttTideFcstValBase() {
    }

}
