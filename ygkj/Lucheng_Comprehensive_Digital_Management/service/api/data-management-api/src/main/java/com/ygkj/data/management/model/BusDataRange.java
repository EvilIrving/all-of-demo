package com.ygkj.data.management.model;

import lombok.Data;

import java.io.Serializable;

/**
 * 数据资源字段范围
 *
 * @author huang.zh 2021-03-18
 */
@Data
public class BusDataRange implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private Long id;

    /**
     * 字段名
     */
    private String column;

    /**
     * 最小值
     */
    private String min;

    /**
     * 最大值
     */
    private String max;

    /**
     * 备注
     */
    private String remark;

    /**
     * 数据资源id
     */
    private String tableId;

    public BusDataRange() {
    }

}
