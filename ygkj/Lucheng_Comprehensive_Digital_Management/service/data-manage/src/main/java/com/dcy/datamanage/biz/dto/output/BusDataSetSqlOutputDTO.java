package com.dcy.datamanage.biz.dto.output;

import com.dcy.datamanage.api.model.BusDataSetSql;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class BusDataSetSqlOutputDTO extends BusDataSetSql {

    @ApiModelProperty("数据源名称")
    private String dataBaseName;
}
