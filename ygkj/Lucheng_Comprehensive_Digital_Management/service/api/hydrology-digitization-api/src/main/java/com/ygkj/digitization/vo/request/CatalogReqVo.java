package com.ygkj.digitization.vo.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class CatalogReqVo {

    @ApiModelProperty("主键")
    private String id;

    @ApiModelProperty("所属父目录id")
    private String parentId;

    @ApiModelProperty("名称")
    private String name;

    @ApiModelProperty(value = "排序号", hidden = true)
    private int sort;

    private String createBy;
}
