package com.ygkj.data.management.vo.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@ApiModel
public class BusDataResponsbilityAddVo {
    /**
     * 库名
     */
    @ApiModelProperty("数据类型:基础数据/业务数据")
    @NotBlank(message = "请指定数据类型！")
    private String schemaName;

    /**
     * 表名
     */
    @ApiModelProperty("表名")
    @NotBlank(message = "请指定表名！")
    private String tableName;

    @ApiModelProperty("主键的值，即第一个字段的值")
    @NotBlank(message = "请指定字段值！")
    private String primaryValue;

    /**
     * 责任人姓名
     */
    @ApiModelProperty("责任人姓名")
    private String responsible;

    /**
     * 责任人电话
     */
    @ApiModelProperty("责任人电话")
    private String responsiblePhone;

    /**
     * 责任人部门
     */
    @ApiModelProperty("责任人部门")
    private String department;

    /**
     * 责任人职位
     */
    @ApiModelProperty("责任人职位")
    private String job;

    /**
     * 行政区划
     */
    @ApiModelProperty("行政区划")
    private String areaName;

}
