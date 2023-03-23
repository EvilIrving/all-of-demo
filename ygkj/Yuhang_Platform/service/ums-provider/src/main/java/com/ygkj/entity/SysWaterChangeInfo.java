package com.ygkj.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @description 水域变化信息
 * @author fml
 * @date 2020-09-10
 */
@Data
public class SysWaterChangeInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
    * id
    */
    private String id;

    /**
    * 水域id
    */
    private String waterId;

    /**
    * 水域面积（单位：m²）
    */
    private Double waterArea;

    /**
    * 库容量（单位：m³）
    */
    private Double capacity;

    /**
    * 河底高程（m）
    */
    private Double waterHeight;

    /**
    * 水体用途
    */
    private String waterUse;

    /**
    * create_time
    */
    private Date createTime;

    /**
    * update_time
    */
    private Date updateTime;

    /**
     * 是否删除(1未删除；0已删除)
     */
    private Integer isDeleted;

}
