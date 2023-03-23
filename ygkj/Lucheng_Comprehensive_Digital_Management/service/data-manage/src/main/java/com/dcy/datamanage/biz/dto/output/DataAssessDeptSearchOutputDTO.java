package com.dcy.datamanage.biz.dto.output;

import com.dcy.datamanage.api.model.DataAssessDeptRel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class DataAssessDeptSearchOutputDTO extends DataAssessDeptRel {

    @ApiModelProperty("部门名称")
    private String deptName;

    @ApiModelProperty("责任人名称")
    private String userName;

}
