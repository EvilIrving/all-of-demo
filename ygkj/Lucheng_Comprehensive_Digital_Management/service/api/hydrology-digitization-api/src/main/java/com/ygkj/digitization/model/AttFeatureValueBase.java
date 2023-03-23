package com.ygkj.digitization.model;

import java.io.Serializable;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author xq
 * @description 统一特征值表-行值表
 * @date 2021-09-07
 */
@Data
public class AttFeatureValueBase implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 列id
     */
    private String columnId;

    /**
     * 列值，都序列化成字符串
     */
    private String columnValue;

    /**
     * 行号
     */
    private Integer rowIndex;

    public AttFeatureValueBase() {
    }

}
