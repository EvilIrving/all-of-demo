package com.dcy.datamanage.api.model;

import lombok.Data;

import java.util.Date;

@Data
public class DataAssessExtendMsg {

    /**
     * 主键
     */
    private String id;

    /**
     * 数据源id
     */
    private String dataSourceId;

    /**
     * 表名
     */
    private String tableName;

    /**
     * 表上次更新时间
     */
    private Date updateTime;
}
