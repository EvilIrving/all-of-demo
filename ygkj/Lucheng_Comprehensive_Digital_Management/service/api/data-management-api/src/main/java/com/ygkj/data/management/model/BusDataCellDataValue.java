package com.ygkj.data.management.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author wd
 * @description 表字段记录值表
 * @date 2021-06-30
 */
@Data
@ApiModel("表字段记录值表")
public class BusDataCellDataValue implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @ApiModelProperty("id")
    private String id;

    /**
     * 列id
     */
    @ApiModelProperty("列id")
    private String colId;

    /**
     * 行id，，即dataid，行记录id
     */
    @ApiModelProperty("行id，即dataId，行记录id")
    private String rowId;

    /**
     * 当前值
     */
    @ApiModelProperty("当前值")
    private String curData;

    /**
     * 是否有效 0-有效 1-无效
     */
    @ApiModelProperty("是否有效 0-有效 1-无效")
    private boolean isEffective;

    /**
     * 审批id
     */
    @ApiModelProperty("审批id")
    private String approveId;

    /**
     * 数据源id
     */
    @ApiModelProperty("数据源id")
    private String dsId;


    @ApiModelProperty("创建时间")
    private Date createTime;

    public BusDataCellDataValue() {
    }

    public BusDataCellDataValue(String id, String colId, String rowId, String curData, boolean isEffective, String approveId, String dsId, Date createTime) {
        this.id = id;
        this.colId = colId;
        this.rowId = rowId;
        this.curData = curData;
        this.isEffective = isEffective;
        this.approveId = approveId;
        this.dsId = dsId;
        this.createTime = createTime;
    }
}
