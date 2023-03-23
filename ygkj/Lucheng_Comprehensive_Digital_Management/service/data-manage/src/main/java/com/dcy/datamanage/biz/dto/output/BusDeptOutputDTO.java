package com.dcy.datamanage.biz.dto.output;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class BusDeptOutputDTO {

    @ApiModelProperty("主键")
    private String id;

    @ApiModelProperty("部门id")
    private String deptId;

    @ApiModelProperty("部门名称")
    private String deptName;

}
