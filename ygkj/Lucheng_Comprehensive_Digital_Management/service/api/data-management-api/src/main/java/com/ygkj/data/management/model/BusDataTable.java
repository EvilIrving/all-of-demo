package com.ygkj.data.management.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 数据源清单记录
 *
 * @author huang.zh 2021-03-18
 */
@Data
public class BusDataTable implements Serializable {

    private static final long serialVersionUID = 1L;

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

    /**
     * 申请次数
     */
    private Integer applyNumber;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 创建人
     */
    private String createId;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 修改人
     */
    private String updateId;

    /**
     * 是否删除 0-未删除 1-已删除
     */
    private boolean delFlag;

    private Integer updateFrequency;

    private String tableSchema;

    private String searchColumn;

    private String gridDisplayColumn;

    private String formDisplayColumn;

    /**
     * 主键列名
     */
    private String primaryColumn;

    /**
     * 概要字段，用于概要一条行记录得字段，如水库名称、测站名称
     */
    private String outlineColumn;

    /**
     * 表的行数量，定期更新
     */
    private Integer tableRows;

    public BusDataTable() {
    }

}

