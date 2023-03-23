package com.ygkj.data.management.vo.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author hucong
 * @description 內存信息
 * @date 2021-07-17
 */
@Data
@ApiModel("內存信息")
public class MemRespVo implements Serializable {

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
     * 内存总量
     */
    @ApiModelProperty("内存总量")
    private BigDecimal total;

    /**
     * 已用内存
     */
    @ApiModelProperty("已用内存")
    private BigDecimal used;

    /**
     * 剩余内存
     */
    @ApiModelProperty("剩余内存")
    private BigDecimal free;

    /**
     * 使用率
     */
    @ApiModelProperty("使用率")
    private BigDecimal usaged;

    public MemRespVo() {
    }

}
