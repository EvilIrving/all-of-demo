package com.ygkj.river.model;

import java.io.Serializable;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author hucong
 * @description 水域调查数据-其他水域水域面数据表
 * @date 2021-07-06
 */
@Data
public class AttOwaaBase implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * fid
     */
    private String fid;

    /**
     * 其他水域名称
     */
    private String name;

    /**
     * 其他水域编码
     */
    private String code;

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
     * 跨界类型
     */
    private String trntype;

    /**
     * 所属流域
     */
    private String bas;

    /**
     * 所属地形地貌
     */
    private String landform;

    /**
     * 水域面积
     */
    private String area;

    /**
     * 平均水深
     */
    private String averdep;

    /**
     * 水域容积
     */
    private String vol;

    /**
     * 类型
     */
    private String type;

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

    public AttOwaaBase() {
    }

}