package com.dcy.datamanage.api.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class BusDataSet {

    @ApiModelProperty("主键")
    private String id;

    @ApiModelProperty("数据集名称")
    private String dataSetName;

    @ApiModelProperty("数据集类型 1.sql 2.http接口")
    private Integer dataSetType;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("创建id")
    private String createId;

    @ApiModelProperty("是否删除 1-已删除 0-未删除")
    private Integer delFlag;

    @ApiModelProperty("科室id")
    private String deptId;
}
