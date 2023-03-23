package com.ygkj.data.management.vo.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("数据资源分页查询参数封装")
public class BusDataTablePageQueryVo {

    @ApiModelProperty("单页限定记录数")
    private Integer pageSize;

    @ApiModelProperty("页码")
    private Integer pageNum;

    @ApiModelProperty("数据资源名称")
    private String dataName;

    @ApiModelProperty("数据资源目录id")
    private String categoryId;

    @ApiModelProperty("表名")
    private String tables;

    @ApiModelProperty("表id")
    private String tableIds;

    @ApiModelProperty("责任人")
    private String dutyPerson;

    @ApiModelProperty("所属数据库")
    private String tableSchema;
}
