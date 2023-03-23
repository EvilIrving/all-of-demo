package com.ygkj.river.model;

import java.io.Serializable;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author hucong
 * @description 水域调查数据-桥梁数据表
 * @date 2021-07-07
 */
@Data
public class AttPbrlBase implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * fid
     */
    private String fid;

    /**
     * 桥梁名称
     */
    private String name;

    /**
     * 所在水域名称
     */
    private String wname;

    /**
     * 所在水域编码
     */
    private String wcode;

    /**
     * 所在市
     */
    private String city;

    /**
     * 所在县（市、区）
     */
    private String county;

    /**
     * 所在乡镇（街道）
     */
    private String town;

    /**
     * 经度
     */
    private String lon;

    /**
     * 纬度
     */
    private String lat;

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

    public AttPbrlBase() {
    }

}

