package com.dcy.datamanage.biz.dto.input;

import com.dcy.datamanage.api.model.BusDataTable;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class BusDataTableSearchInputDTO extends BusDataTable {

    @ApiModelProperty("注释信息")
    private String remarks;

}
