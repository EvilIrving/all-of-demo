package com.ygkj.digitization.model;

import java.io.Serializable;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author xq
 * @description 水文特征表
 * @date 2021-09-07
 */
@Data
public class AttHydroFeatureBase implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 表名
     */
    private String tableName;

    /**
     * 表体
     */
    private String tableEntiy;

    /**
     * 表体数据类型
     */
    private String entityType;

    public AttHydroFeatureBase() {
    }

}
