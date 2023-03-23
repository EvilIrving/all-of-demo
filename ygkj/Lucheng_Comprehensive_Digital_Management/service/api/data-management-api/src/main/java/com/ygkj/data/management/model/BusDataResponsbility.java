package com.ygkj.data.management.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author huangzh
 * @description 具体每条数据的责任人
 * @date 2021-03-22
 */
@Data
@ApiModel("具体每条数据的责任人")
public class BusDataResponsbility implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @ApiModelProperty("id")
    private String id;

    /**
     * 库名
     */
    @ApiModelProperty("库名")
    private String schemaName;

    /**
     * 表名
     */
    @ApiModelProperty("表名")
    private String tableName;

    /**
     * 主键字段名
     */
    @ApiModelProperty("主键字段名")
    private String primaryKey;

    /**
     * 主键字段值
     */
    @ApiModelProperty("主键字段值")
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

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    private Date createTime;

    /**
     * 创建人
     */
    @ApiModelProperty("创建人")
    private String createId;

    /**
     * 修改时间
     */
    @ApiModelProperty("修改时间")
    private Date updateTime;

    /**
     * 修改人
     */
    @ApiModelProperty("修改人")
    private String updateId;

    /**
     * 是否删除 0-未删除 1-已删除
     */
    @ApiModelProperty("是否删除 0-未删除 1-已删除")
    private int delFlag;

    public BusDataResponsbility() {
    }
}
