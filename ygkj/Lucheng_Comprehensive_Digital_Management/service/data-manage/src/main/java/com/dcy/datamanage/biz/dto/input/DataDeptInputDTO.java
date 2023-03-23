package com.dcy.datamanage.biz.dto.input;

import com.dcy.datamanage.biz.dto.output.BusDataDeptOutputDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;


@Data
public class DataDeptInputDTO extends DataEditInputDTO {

    @ApiModelProperty("数据及它的部门列表")
    private List<BusDataDeptOutputDTO> dataList;

    @ApiModelProperty("部门id")
    private String deptId;

}
