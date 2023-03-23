package com.ygkj.river.model;

import java.io.Serializable;

import lombok.Data;

import java.util.Date;

/**
 * @author hucong
 * @description 水域调查数据-河道水域面数据表
 * @date 2021-07-06
 */
@Data
public class AttRvaaBase implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * fid
     */
    private String fid;

    /**
     * 河道（段）名称
     */
    private String name;

    /**
     * 河道（段）编码
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
     * 长度r
     */
    private String length;

    /**
     * 平均宽度
     */
    private String width;

    /**
     * 起点位置名称
     */
    private String sname;

    /**
     * 终点位置名称
     */
    private String ename;

    /**
     * 等级
     */
    private String grade;

    /**
     * 干支流
     */
    private String mntrb;

    /**
     * 跨界类型
     */
    private String trntype;

    /**
     * 流经乡镇（街道）
     */
    private String town;

    /**
     * 所属流域
     */
    private String bas;

    /**
     * 所属地形地貌
     */
    private String landform;

    /**
     * 主要功能
     */
    private String function;

    /**
     * 水域面积
     */
    private String area;

    /**
     * 水域容积
     */
    private String vol;

    /**
     * 管理单位
     */
    private String mu;

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
     * 起点设计水位
     */
    private String sdl;

    /**
     * 终点设计水位
     */
    private String edl;

    /**
     * 河长
     */
    private String rchief;

    /**
     * 河流码
     */
    private String rcode;

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

    public AttRvaaBase() {
    }

}

