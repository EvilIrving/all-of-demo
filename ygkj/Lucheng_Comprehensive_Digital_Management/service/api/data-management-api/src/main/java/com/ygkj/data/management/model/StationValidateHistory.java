package com.ygkj.data.management.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Huangzh
 * @description 不规则测站实时值校验历史
 * @date 2021-07-21
 */
@Data
public class StationValidateHistory implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private String id;

    /**
     * 测站编码
     */
    private String stationCode;

    /**
     * 测站类型
     */
    private String stationType;

    /**
     * 不规则字段名
     */
    private String columnName;

    /**
     * 不规则字段中文名
     */
    private String columnComment;

    /**
     * 不规则字段值
     */
    private Double illegalValue;

    private Date illegalTime;

    public StationValidateHistory() {
    }

    public StationValidateHistory(String stationCode, String stationType, String columnName, String columnComment, Double illegalValue, Date illegalTime) {
        this.stationCode = stationCode;
        this.stationType = stationType;
        this.columnName = columnName;
        this.columnComment = columnComment;
        this.illegalValue = illegalValue;
        this.illegalTime = illegalTime;
    }
}


