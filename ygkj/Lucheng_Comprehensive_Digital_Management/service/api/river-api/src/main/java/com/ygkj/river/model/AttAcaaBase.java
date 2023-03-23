package com.ygkj.river.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author hucong
 * @description 水域调查数据-人工水道水域面数据表
 * @date 2021-07-06
 */
@Data
public class AttAcaaBase implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * fid
     */
    private String fid;

    /**
     * 人工水道名称
     */
    private String name;

    /**
     * 人工水道编码
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
     * 起点位置名称
     */
    private String sname;

    /**
     * 终点位置名称
     */
    private String enmae;

    /**
     * 长度
     */
    private String length;

    /**
     * 宽度
     */
    private String width;

    /**
     * 水域面积
     */
    private String area;

    /**
     * 水域容积
     */
    private String vol;

    /**
     * 所属地形地貌
     */
    private String landform;

    /**
     * 所在乡镇（街道）
     */
    private String town;

    /**
     * 所属灌区
     */
    private String ia;

    /**
     * 类别
     */
    private String type;

    /**
     * 是否重要水域
     */
    private String imp;

    /**
     * 与三调差异
     */
    private String contdiff;

    /**
     * 是否属于特殊区域
     */
    private String spcl;

    /**
     * 备注
     */
    private String remark;

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

    public AttAcaaBase() {
    }

}

