package com.dcy.datamanage.biz.dto.input;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class DataApplySearchInputDTO {

    @ApiModelProperty(hidden = true)
    private String deptId;

    @ApiModelProperty(hidden = true)
    private String roleType;

    @ApiModelProperty(hidden = true)
    private String userId;

    @ApiModelProperty("1我发起的 2待我审批的 3我已处理的")
    private Integer type;

    @ApiModelProperty("审批状态 -1驳回 0待处理 1通过")
    private Integer applyStatus;

    @ApiModelProperty("审批类型 1.新增 2.删除 3.修改")
    private Integer applyType;
}
