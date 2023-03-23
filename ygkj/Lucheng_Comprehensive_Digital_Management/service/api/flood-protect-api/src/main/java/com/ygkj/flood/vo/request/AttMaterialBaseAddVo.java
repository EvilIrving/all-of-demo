package com.ygkj.flood.vo.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class AttMaterialBaseAddVo {

    /**
     * id
     */
    @ApiModelProperty("id,修改时用")
    private String id;

    /**
     * 物资类型id
     */
    @ApiModelProperty("物资类型id")
    @NotNull(message = "物资类型id不允许为空")
    private String typeId;

    /**
     * 物资类型
     */
    @ApiModelProperty("物资类型名称")
    private String type;

    /**
     * 物资名称
     */
    @ApiModelProperty("物资名称")
    private String name;

    /**
     * 仓库id
     */
    @ApiModelProperty("仓库id")
    @NotNull(message = "仓库id不允许为空")
    private String warehouseId;

    /**
     * 数量
     */
    @ApiModelProperty("数量")
    private String num;

    /**
     * 单位
     */
    @ApiModelProperty("单位")
    private String measure;

    /**
     * 价格
     */
    @ApiModelProperty("价格")
    private String price;
}
