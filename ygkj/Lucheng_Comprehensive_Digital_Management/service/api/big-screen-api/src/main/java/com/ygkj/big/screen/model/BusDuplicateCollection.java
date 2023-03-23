package com.ygkj.big.screen.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @author hucong
 * @description 复盘收藏
 * @date 2021-06-16
 */
@Data
public class BusDuplicateCollection implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    private String id;

    /**
     * 开始时间
     */
    private String startTime;

    /**
     * 结束实际按
     */
    private String endTime;

    /**
     * 台风id
     */
    private String typhoonId;

    /**
     * 时间间隔
     */
    private String timeInterval;

    /**
     * 类型 1：台风 2： 自定义
     */
    private String type;

    /**
     * 台风名称
     */
    private String typhoonName;

    public BusDuplicateCollection() {
    }
}