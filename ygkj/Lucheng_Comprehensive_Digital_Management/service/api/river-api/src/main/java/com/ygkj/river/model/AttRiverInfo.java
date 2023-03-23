package com.ygkj.river.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author wgf
 * @date 2022/5/25 11:20
 * @Description:
 */
@Data
public class AttRiverInfo extends PageEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    /**
     * fid
     */
    private String fid;

    /**
     * 对象id
     */
    private String objectId;

    /**
     * 名称
     */
    private String name;

    /**
     * 编码
     */
    private String code;

    /**
     * city
     */
    private String city;

    /**
     * county
     */
    private String county;

    /**
     * length
     */
    private BigDecimal length;

    /**
     * width
     */
    private BigDecimal width;

    /**
     * sname
     */
    private String sname;

    /**
     * ename
     */
    private String ename;

    /**
     * grade
     */
    private String grade;

    /**
     * mntrb
     */
    private String mntrb;

    /**
     * trntype
     */
    private String trntype;

    /**
     * town
     */
    private String town;

    /**
     * bas
     */
    private String bas;

    /**
     * landform
     */
    private String landform;

    /**
     * function
     */
    private String function;

    /**
     * area
     */
    private String area;

    /**
     * vol
     */
    private String vol;

    /**
     * mu
     */
    private String mu;

    /**
     * imp
     */
    private String imp;

    /**
     * sdl
     */
    private String sdl;

    /**
     * edl
     */
    private String edl;

    /**
     * rchief
     */
    private String rchief;

    /**
     * remark
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
     * contdiff
     */
    private String contdiff;

    /**
     * spcl
     */
    private String spcl;

    /**
     * rcode
     */
    private String rcode;

    /**
     * 类型
     */
    private String type;

    public AttRiverInfo() {}
}
