package com.ygkj.data.management.vo.request;

/**
 * 数据源更新
 */

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@ApiModel
public class BusColumnDataUpdateVo {

    @ApiModelProperty("表字段记录数据id")
    @NotNull(message = "表字段记录数据id不能为空")
    private String cellDataValueId;

    @ApiModelProperty("数据源名称")
    private String dsName;

    @ApiModelProperty("当前字段值")
    private String curData;
}
