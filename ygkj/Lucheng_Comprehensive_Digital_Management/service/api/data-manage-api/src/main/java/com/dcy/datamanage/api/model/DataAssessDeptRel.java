package com.dcy.datamanage.api.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class DataAssessDeptRel {

    @ApiModelProperty("主键")
    private String id;

    @ApiModelProperty("数据源id")
    private String dataSourceId;

    @ApiModelProperty("表名")
    private String tableName;

    @ApiModelProperty("科室id")
    private String deptId;

    @ApiModelProperty("用户id")
    private String userId;
}
