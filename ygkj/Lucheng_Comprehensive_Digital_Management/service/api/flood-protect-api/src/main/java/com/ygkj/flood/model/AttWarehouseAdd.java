package com.ygkj.flood.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @description 防汛仓库入库表
 * @author lxl
 * @date 2022-05-19
 */
@Data
@ApiModel("防汛仓库入库表")
public class AttWarehouseAdd implements Serializable {

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

    /**
     * 物资名称
     */
    @ApiModelProperty("物资名称")
    private String materialName;

    /**
     * 入库类型 （采购入库/归还入库）
     */
    @ApiModelProperty("入库类型 （1-采购入库/ 2-归还入库）")
    private String type;

    /**
     * 数量
     */
    @ApiModelProperty("数量")
    private Integer num;

    @ApiModelProperty("规格")
    private String standard;

    @ApiModelProperty("单价")
    private BigDecimal univalent;


    @ApiModelProperty("总价")
    private BigDecimal amount;

    /**
     * 入库时间
     */
    @ApiModelProperty("入库时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date tm;

    /**
     * 记录人
     */
    @ApiModelProperty("记录人")
    private String recorder;

    /**
     * 附件ids
     */
    @ApiModelProperty("附件ids")
    private String fileIds;

    @TableField(exist = false)
    private List<Object> fileList;

    /**
     * 备注
     */
    @ApiModelProperty("备注")
    private String remark;

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

    public AttWarehouseAdd() {
    }

}
