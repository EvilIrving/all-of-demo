package com.dcy.datamanage.biz.dto.input;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@Data
public class DataAssessPageInputDTO {

    @ApiModelProperty("数据源id")
    private String dataSourceId;


    @ApiModelProperty("表格名称")
    private String tableName;

}
