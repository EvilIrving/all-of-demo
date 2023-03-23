package com.dcy.datamanage.api.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel("列别名信息")
public class BusDataColumn implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("id")
    private String id;

    @ApiModelProperty("数据源id")
    private String dataSourceId;

    @ApiModelProperty("所属表格名称")
    private String tableName;

    @ApiModelProperty("列名")
    private String columnName;

    @ApiModelProperty("列别名")
    private String customColumnName;

}

