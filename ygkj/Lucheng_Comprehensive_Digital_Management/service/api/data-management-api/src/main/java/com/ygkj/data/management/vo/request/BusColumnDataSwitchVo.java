package com.ygkj.data.management.vo.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 列的数据源切换
 */
@Data
@ApiModel
public class BusColumnDataSwitchVo {

    @ApiModelProperty("表字段记录数据id，新增数据源的时候不需要")
    @NotNull(message = "表字段记录数据id不能为空")
    private String cellDataValueId;

    @NotNull(message = "列id不能为空")
    @ApiModelProperty("列id")
    private String colId;

    @NotNull(message = "行id不能为空")
    @ApiModelProperty("行id，即数据id")
    private String rowId;

    @ApiModelProperty("数据源id")
    private String dsId;

    @ApiModelProperty("数据源名称，新增源的时候指定")
    private String dsName;

    @ApiModelProperty("当前字段值，新增源的时候指定")
    private String curData;
}
