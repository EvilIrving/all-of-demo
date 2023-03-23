package com.dcy.datamanage.biz.dto.output;

import com.dcy.datamanage.api.model.BusDataTable;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class BusDataTableOutputDTO extends BusDataTable {

    @ApiModelProperty("注释信息")
    private String remarks;

    @ApiModelProperty("资源目录id")
    private String dataResourceId;

    @ApiModelProperty("资源目录名称")
    private String resourceName;
}
