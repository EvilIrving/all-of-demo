package com.dcy.datamanage.biz.dto.output;

import com.dcy.datamanage.api.model.BusDataSetResField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class BusDataSetPreviewOutputDTO {

    @ApiModelProperty("返回字段列表")
    private List<BusDataSetResField> fieldList;

    @ApiModelProperty("返回值")
    private Object res;
}
