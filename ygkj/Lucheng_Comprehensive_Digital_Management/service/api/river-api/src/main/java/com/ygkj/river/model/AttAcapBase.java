package com.ygkj.river.model;

import java.io.Serializable;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author hucong
 * @description 水域调查数据-人工水道临水线点数据表
 * @date 2021-07-06
 */
@Data
public class AttAcapBase implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * fid
     */
    private String fid;

    /**
     * 类型
     */
    private String type;

    /**
     * 人工水道名称
     */
    private String name;

    /**
     * 人工水道编码
     */
    private String code;

    /**
     * 经度
     */
    private String lon;

    /**
     * 纬度
     */
    private String lat;

    /**
     * 高程
     */
    private String elevation;

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

    public AttAcapBase() {
    }

}

