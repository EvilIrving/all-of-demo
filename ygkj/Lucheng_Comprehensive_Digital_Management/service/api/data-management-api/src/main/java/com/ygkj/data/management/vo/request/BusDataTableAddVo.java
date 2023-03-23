package com.ygkj.data.management.vo.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class BusDataTableAddVo {

    /**
     * id
     */
    @ApiModelProperty("id")
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
     * 责任人名称
     */
    @ApiModelProperty("责任人名称")
    private String dutyPerson;

    /**
     * 表行政区划字段
     */
    @ApiModelProperty("表行政区划字段")
    private String areaColumn;

    /**
     * 表流域字段
     */
    @ApiModelProperty("表流域字段")
    private String basinColumn;

    /**
     * 数据描述
     */
    @ApiModelProperty("数据描述")
    private String description;

    /**
     * 数据质量校验字段
     */
    @ApiModelProperty("数据质量校验字段")
    private String dataQualityColumns;

    /**
     * 数据资源目录id，用于分类
     */
    @ApiModelProperty("数据资源目录id")
    private String categoryId;

    /**
     * 数据资源目录
     */
    @ApiModelProperty("数据资源目录")
    private String category;

    //    @ApiModelProperty("字段规则数组")
//    List<BusDataRange> columns;
    @ApiModelProperty("字段规则数组")
    List<BusTableColumnRuleAddVo> rules;

    @ApiModelProperty("数据更新频次")
    private Integer updateFrequency;

    @ApiModelProperty("数据类型：基础数据/业务数据")
    private String schema;

    @ApiModelProperty("自定义搜索字段")
    private String searchColumn;

    @ApiModelProperty("自定义列表展示字段")
    private String gridDisplayColumn;

    @ApiModelProperty("自定义表单展示字段")
    private String formDisplayColumn;

    /**
     * 主键列名
     */
    @ApiModelProperty("主键列名")
    private String primaryColumn;

    /**
     * 概要字段，用于概要一条行记录得字段，如水库名称、测站名称
     */
    @ApiModelProperty("概要字段")
    private String outlineColumn;
}
