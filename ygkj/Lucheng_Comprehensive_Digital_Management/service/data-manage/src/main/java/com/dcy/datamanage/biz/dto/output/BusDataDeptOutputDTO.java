package com.dcy.datamanage.biz.dto.output;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class BusDataDeptOutputDTO {

    @ApiModelProperty("数据")
    private Map<String,Object> data;

    @ApiModelProperty("部门列表")
    private List<BusDeptOutputDTO> deptList;
}
