package com.ygkj.soft.database.api.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Huang.zh
 * @date 2020/9/17 17:02
 * @Description:
 */
@Data
public class BusDataSourcePersistence implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    /**
     * 数据源名称
     */
    private String dataBaseName;

    /**
     * 入库日期
     */
    private Date persistenceDate;

    /**
     * 当日入库记录总条数
     */
    private Integer rowNumber;

    /**
     * create_time
     */
    private Date createTime;

    public BusDataSourcePersistence() {
    }

    public BusDataSourcePersistence(String id, String dataBaseName, Date persistenceDate, Integer rowNumber) {
        this.id = id;
        this.dataBaseName = dataBaseName;
        this.persistenceDate = persistenceDate;
        this.rowNumber = rowNumber;
    }
}
