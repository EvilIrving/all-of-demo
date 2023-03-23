package com.dcy.datamanage.biz.dto.input;

import com.dcy.datamanage.api.model.DataAssessDeptRel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class DataAssessDeptInputDTO extends DataAssessDeptRel{

    @ApiModelProperty("表名列表")
    private List<String> tableNameList;
}
