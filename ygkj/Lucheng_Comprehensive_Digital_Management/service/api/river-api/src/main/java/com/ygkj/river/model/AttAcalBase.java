package com.ygkj.river.model;

import java.io.Serializable;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author hucong
 * @description 水域调查数据-人工水道临水线数据表
 * @date 2021-07-06
 */
@Data
public class AttAcalBase implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * fid
     */
    private String fid;

    /**
     * 人工水道编码
     */
    private String code;

    /**
     * 左右岸
     */
    private String side;

    /**
     * geometry
     */
    private String geometry;

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

    public AttAcalBase() {
    }

}