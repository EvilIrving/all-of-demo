package com.ygkj.soft.database.api.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author Huang.zh
 * @date 2020/9/8 10:07
 * @Description: 库表字段实体
 */
@Data
@ApiModel("bus_data_column")
public class BusDataColumn implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @ApiModelProperty("id")
    private String id;

    /**
     * 所属表格id
     */
    @ApiModelProperty("所属表格id")
    private String tableId;

    /**
     * name
     */
    @ApiModelProperty("name")
    private String name;

    /**
     * 是否为空
     */
    @ApiModelProperty("是否为空")
    private boolean nullable;

    /**
     * column_type
     */
    @ApiModelProperty("column_type")
    private String columnType;

    /**
     * 默认值
     */
    @ApiModelProperty("默认值")
    private String columnDefault;

    /**
     * 注释值
     */
    @ApiModelProperty("注释值")
    private String remarks;

    /**
     * 可编辑
     */
    @ApiModelProperty("可编辑")
    private Boolean editable;

    /**
     * 字段长度
     */
    @ApiModelProperty("字段长度")
    private Long columnLength;
    /**
     * 对应表单控件
     */
    @ApiModelProperty("对应表单控件")
    private String formComponent;

    @ApiModelProperty("排序字段")
    private Integer orderColumn;

    public BusDataColumn() {
    }

    public BusDataColumn(String tableId) {
        this.tableId = tableId;
    }
}

