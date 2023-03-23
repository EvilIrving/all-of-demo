package com.dcy.datamanage.biz.dto.input;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class DataSearchInputDTO {

    @ApiModelProperty("数据资源id")
    private String dataSourceId;

    @ApiModelProperty("表名称")
    private String tableName;
}
