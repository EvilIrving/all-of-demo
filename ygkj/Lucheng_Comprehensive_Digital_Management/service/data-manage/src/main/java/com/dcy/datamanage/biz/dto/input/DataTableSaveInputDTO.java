package com.dcy.datamanage.biz.dto.input;

import com.dcy.datamanage.api.model.BusDataTable;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class DataTableSaveInputDTO {

    @ApiModelProperty("数据源id")
    private String dataSourceId;

    @ApiModelProperty("别名列表")
    private List<BusDataTable> list;
}
