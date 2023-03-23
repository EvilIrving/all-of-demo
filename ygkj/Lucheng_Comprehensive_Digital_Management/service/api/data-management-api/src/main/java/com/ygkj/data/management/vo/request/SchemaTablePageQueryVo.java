package com.ygkj.data.management.vo.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel
public class SchemaTablePageQueryVo {

    @ApiModelProperty("数据类型:基础数据/业务数据")
    private String schemas;
    @ApiModelProperty("表中文释义")
    private String name;
    @ApiModelProperty("页码")
    private Integer pageNum;
    @ApiModelProperty("每页记录数")
    private Integer pageSize;
}
