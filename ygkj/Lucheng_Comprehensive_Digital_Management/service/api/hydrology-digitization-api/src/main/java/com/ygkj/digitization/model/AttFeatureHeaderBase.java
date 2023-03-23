package com.ygkj.digitization.model;

import java.io.Serializable;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author xq
 * @description 统一特征值表-表头定义表
 * @date 2021-09-07
 */
@Data
public class AttFeatureHeaderBase implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 列id
     */
    private String id;

    /**
     * 表名
     */
    private String tableName;

    /**
     * 列名
     */
    private String columnName;

    /**
     * 列号
     */
    private Integer columnIndex;

    public AttFeatureHeaderBase() {
    }

}
