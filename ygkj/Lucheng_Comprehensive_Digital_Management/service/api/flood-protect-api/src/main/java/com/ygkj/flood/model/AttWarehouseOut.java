package com.ygkj.flood.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @description 防汛仓库出库记录表
 * @author lxl
 * @date 2022-05-19
 */
@Data
@ApiModel("防汛仓库出库记录表")
public class AttWarehouseOut implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @ApiModelProperty("id")
    private String id;

    /**
     * 仓库id
     */
    @ApiModelProperty("仓库id")
    private String warehouseId;


    @ApiModelProperty("出库类型（1-正常出库 2-报废出库）")
    private Integer type;

    @ApiModelProperty("单价")
    private BigDecimal univalent;

    /**
     * 物资名称
     */
    @ApiModelProperty("物资名称")
    private String materialName;

    /**
     * 数量
     */
    @ApiModelProperty("数量")
    private Integer num;

    @ApiModelProperty("物资价值")
    private BigDecimal amount;

    @ApiModelProperty("规格")
    private String standard;

    /**
     * 入库时间
     */
    @ApiModelProperty("出库时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date tm;

    /**
     * 记录人
     */
    @ApiModelProperty("记录人")
    private String recorder;

    /**
     * 删除标记 0未删除 1已删除
     */
    @ApiModelProperty("删除标记 0未删除 1已删除")
    private Integer delFlag;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    private Date createTime;

    /**
     * 创建人
     */
    @ApiModelProperty("创建人")
    private String createId;

    /**
     * 修改时间
     */
    @ApiModelProperty("修改时间")
    private Date updateTime;

    /**
     * 修改人
     */
    @ApiModelProperty("修改人")
    private String updateId;

    public AttWarehouseOut() {
    }

}

