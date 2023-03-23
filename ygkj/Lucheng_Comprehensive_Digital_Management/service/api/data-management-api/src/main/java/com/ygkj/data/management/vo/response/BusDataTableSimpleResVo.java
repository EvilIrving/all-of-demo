package com.ygkj.data.management.vo.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler"})
@ApiModel("数据资源对象返回封装")
public class BusDataTableSimpleResVo {

    /**
     * id
     */
    private String id;

    /**
     * 数据清单名称
     */
    @ApiModelProperty("数据清单名称")
    private String dataName;

    /**
     * 数据表名
     */
    @ApiModelProperty("数据表名")
    private String dataTable;

    /**
     * 责任部门
     */
    @ApiModelProperty("责任部门")
    private String dutyDepartment;

    /**
     * 责任部门id
     */
    @ApiModelProperty("责任部门id")
    private String dutyDepartmentId;

    /**
     * 责任人名称
     */
    @ApiModelProperty("责任人名称")
    private String dutyPerson;
    /**
     * 数据资源目录
     */
    @ApiModelProperty("数据资源目录")
    private String category;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    private Date createTime;

    /**
     * 申请次数
     */
    @ApiModelProperty("申请次数")
    private Integer applyNumber;

    /**
     * 数据质量统计字段
     */
    private String dataQualityColumns;

    private String updateFrequency;

    private String rows;

    private String gridDisplayColumn;

    private String formDisplayColumn;

    /**
     * 拥有操作该表权限的角色名称
     */
    @ApiModelProperty("拥有操作该表权限的角色名称")
    private String roleName;

    /**
     * 数据列条数
     */
    @ApiModelProperty("数据列条数")
    private Integer columnsCnt;

    /**
     * 主键列名
     */
    @ApiModelProperty("主键列名")
    private String primaryColumn;

    /**
     * 概要字段，用于概要一条行记录得字段，如水库名称、测站名称
     */
    @ApiModelProperty("概要字段，用于概要一条行记录得字段，如水库名称、测站名称")
    private String outlineColumn;

    @ApiModelProperty("库名")
    private String tableSchema;
    @ApiModelProperty("表数据行数")
    private Integer tableRows;

    /**
     * api转换标记
     */
    private boolean converted;
}
