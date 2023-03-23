package com.ygkj.data.management.vo.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@ApiModel("cpu信息表")
public class CpuRespVo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @ApiModelProperty("主键id")
    private String id;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    private Date createTime;

    /**
     * 核心数
     */
    @ApiModelProperty("核心数")
    private Integer cpuNum;

    /**
     * CPU总
     */
    @ApiModelProperty(value = "CPU总", hidden = true)
    private BigDecimal total;

    /**
     * cpu系统使用率
     */
    @ApiModelProperty("cpu系统使用率")
    private BigDecimal sys;

    /**
     * cpu用户使用率
     */
    @ApiModelProperty("cpu用户使用率")
    private BigDecimal used;

    /**
     * cpu当前等待率
     */
    @ApiModelProperty("cpu当前等待率")
    private BigDecimal wait;

    /**
     * cpu当前空闲率
     */
    @ApiModelProperty("cpu当前空闲率")
    private BigDecimal free;

    public CpuRespVo() {
    }

}
