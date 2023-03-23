package com.ygkj.project.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class BhDict {
    /**
     * 表名
     */
    @ApiModelProperty("表名")
    private String tableName;

    /**
     * 列名
     */
    @ApiModelProperty("列名")
    private String colName;

    /**
     * 编码
     */
    @ApiModelProperty("编码")
    private String code;

    /**
     * 值
     */
    @ApiModelProperty("值")
    private String val;
}
