package com.ygkj.river.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author hucong
 * @description 水域调查数据-堤防数据表
 * @date 2021-07-07
 */
@Data
public class AttPdklBase implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * fid
     */
    private String fid;

    /**
     * 堤防名称
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
     * 长度
     */
    private String length;

    /**
     * 起点高程
     */
    private String selev;

    /**
     * 终点高程
     */
    private String eelev;

    /**
     * 类型
     */
    private String type;

    /**
     * 设计标准
     */
    private String ds;

    /**
     * 起点经度
     */
    private String slon;

    /**
     * 起点纬度
     */
    private String slat;

    /**
     * 终点经度
     */
    private String elon;

    /**
     * 终点纬度
     */
    private String elat;

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

    public AttPdklBase() {
    }

}