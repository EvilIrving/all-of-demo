package com.dcy.datamanage.api.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class DataAssessResult {

    @ApiModelProperty("主键")
    private String id;

    @ApiModelProperty("数据源id")
    private String dataSourceId;

    @ApiModelProperty("表名")
    private String tableName;

    @ApiModelProperty("得分")
    private Integer score;
}
