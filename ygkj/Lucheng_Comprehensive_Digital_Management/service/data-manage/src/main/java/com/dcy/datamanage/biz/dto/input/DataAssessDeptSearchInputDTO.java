package com.dcy.datamanage.biz.dto.input;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class DataAssessDeptSearchInputDTO {

    @ApiModelProperty("数据源id")
    private String dataSourceId;

    @ApiModelProperty("表名")
    private String tableName;

    @ApiModelProperty("表名列表")
    private List<String> tableNameList;
}
