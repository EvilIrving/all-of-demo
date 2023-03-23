package com.dcy.datamanage.biz.dto.input;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class BusDataResourceSearchInputDTO {

    @ApiModelProperty("父id")
    private String parentId;

    @ApiModelProperty("是否带子集 true带子集(树形) false不带子集")
    private boolean tree;
}
