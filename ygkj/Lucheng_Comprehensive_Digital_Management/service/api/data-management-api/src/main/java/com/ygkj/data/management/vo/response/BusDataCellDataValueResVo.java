package com.ygkj.data.management.vo.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author wd
 * @description 表字段记录值表
 * @date 2021-06-30
 */
@Data
@ApiModel("表字段记录值表")
@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler"})
public class BusDataCellDataValueResVo implements Serializable {

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
     * 列名
     */
    @ApiModelProperty("列名")
    private String colName;

    /**
     * 列说明
     */
    @ApiModelProperty("列说明")
    private String colDesc;

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

    /**
     * 数据源名称
     */
    @ApiModelProperty("数据源名称")
    private String dsName;

    /**
     * 数据源类型
     */
    @ApiModelProperty("数据源类型")
    private boolean isAuto;

    /**
     * 提交人
     */
    @ApiModelProperty("提交人")
    private String submitPerson;

    /**
     * 提交时间
     */
    @ApiModelProperty("数据创建时间")
    private String createTime;

    @ApiModelProperty("当前数据源选中情况")
    private boolean dsSelected;

    public BusDataCellDataValueResVo() {
    }

}
