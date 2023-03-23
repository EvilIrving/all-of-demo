package com.ygkj.river.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author hucong
 * @description 水域调查数据-水库水域面数据表
 * @date 2021-07-06
 */
@Data
public class AttRsaaBase implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * fid
     */
    private String fid;

    /**
     * 水库名称
     */
    private String name;

    /**
     * 水库编码
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
     * 类型
     */
    private String type;

    /**
     * 集雨面积
     */
    private String rcarea;

    /**
     * 总库容
     */
    private String tcr;

    /**
     * 兴利库容
     */
    private String ucr;

    /**
     * 设计洪水位
     */
    private String npl;

    /**
     * 移民水位
     */
    private String iml;

    /**
     * 设计洪水位
     */
    private String dfl;

    /**
     * 校核洪水位
     */
    private String mfl;

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
     * 坝顶高程
     */
    private String ce;

    /**
     * 主要功能
     */
    private String function;

    /**
     * 是否重要水域
     */
    private String imp;

    /**
     * 建成时间
     */
    private String bldtm;

    /**
     * 管理单位
     */
    private String mu;

    /**
     * 所在乡镇（街道）
     */
    private String town;

    /**
     * 湖长
     */
    private String lchief;

    /**
     * 备注
     */
    private String remark;

    /**
     * shape_leng
     */
    private String shapeLeng;

    /**
     * shape_area
     */
    private String shapeArea;

    /**
     * 与三调差异
     */
    private String contdiff;

    /**
     * 是否属于特殊区域
     */
    private String spcl;

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

    public AttRsaaBase() {
    }

}