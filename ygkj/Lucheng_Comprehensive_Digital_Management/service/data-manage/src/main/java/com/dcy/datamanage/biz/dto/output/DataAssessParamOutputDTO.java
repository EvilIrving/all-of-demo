package com.dcy.datamanage.biz.dto.output;

import com.dcy.datamanage.api.model.DataAssessFrequency;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class DataAssessParamOutputDTO extends BusDataTableOutputDTO {

    @ApiModelProperty("归属部门列表")
    private List<DataAssessDeptSearchOutputDTO> deptList;

    @ApiModelProperty("频次")
    private DataAssessFrequency dataAssessFrequency;
}
