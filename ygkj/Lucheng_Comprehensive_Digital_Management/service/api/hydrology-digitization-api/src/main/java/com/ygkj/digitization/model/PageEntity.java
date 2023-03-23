package com.ygkj.digitization.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("分页")
public class PageEntity {

    @ApiModelProperty("每页展示条数")
    private int pageSize;

    @ApiModelProperty("页码")
    private int pageNum;

    @ApiModelProperty(value = "是否分页", hidden = true)
    private boolean isFetch = false;
}
