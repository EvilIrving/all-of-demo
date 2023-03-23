package com.ygkj.project.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class PageEntity {

    @ApiModelProperty("每页展示条数")
    private int pageSize;

    @ApiModelProperty("页码")
    private int pageNum;
}
