package com.ygkj.data.management.vo.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler"})
public class BusDataTableDetailResVo {

    /**
     * id
     */
    private String id;

    /**
     * 数据清单名称
     */
    private String dataName;

    /**
     * 数据表名
     */
    private String dataTable;

    /**
     * 责任部门
     */
    private String dutyDepartment;

    /**
     * 责任部门id
     */
    private String dutyDepartmentId;

    /**
     * 责任人名称
     */
    private String dutyPerson;

    /**
     * 表行政区划字段
     */
    private String areaColumn;

    /**
     * 表流域字段
     */
    private String basinColumn;

    /**
     * 数据描述
     */
    private String description;

    /**
     * 数据质量校验字段
     */
    private String dataQualityColumns;

    /**
     * 数据资源目录id，用于分类
     */
    private String categoryId;

    /**
     * 数据资源目录
     */
    private String category;

//    List<BusDataRange> columns;

    List<BusTableColumnRuleResVo> rules;

    /**
     * 申请次数
     */
    private Integer applyNumber;

    private Integer updateFrequency;

    private String tableSchema;

    private String searchColumn;

    private String gridDisplayColumn;

    private String formDisplayColumn;

    /**
     * 主键字段名
     */
    private String primaryColumn;

    /**
     * 概要字段，用于概要一条行记录得字段，如水库名称、测站名称
     */
    private String outlineColumn;

    /**
     * api转化标记
     */
    private boolean converted;
}
