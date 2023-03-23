package com.dcy.datamanage.api.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class BusDataSetSql {

    @ApiModelProperty("主键")
    private String id;

    @ApiModelProperty("数据集id")
    private String dataSetId;

    @ApiModelProperty("数据源id")
    private String dataSourceId;

    @ApiModelProperty("sql语句")
    private String sqlText;
}
