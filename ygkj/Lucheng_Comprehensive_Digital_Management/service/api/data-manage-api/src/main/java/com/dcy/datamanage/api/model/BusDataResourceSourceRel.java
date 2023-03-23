package com.dcy.datamanage.api.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class BusDataResourceSourceRel {

    @ApiModelProperty("主键")
    private String id;

    @ApiModelProperty("资源目录id")
    private String dataResourceId;

    @ApiModelProperty("数据源id")
    private String dataSourceId;

    @ApiModelProperty("表名称")
    private String tableName;
}
