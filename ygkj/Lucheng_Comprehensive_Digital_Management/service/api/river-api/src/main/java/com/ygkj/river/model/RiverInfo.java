package com.ygkj.river.model;

import java.io.Serializable;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 河道名录表
 *
 * @author huangzh 2021-03-02
 */
@Data
public class RiverInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private String id;

    /**
     * 河道名称
     */
    private String name;

    /**
     * 河流类型
     */
    private String riverType;

    /**
     * 河流等级
     */
    private String riverGrade;

    /**
     * 所属流域
     */
    private String basin;

    /**
     * 河流长度
     */
    private Double riverLength;

    /**
     * 河源位置
     */
    private String resourceLocation;

    /**
     * 河口位置
     */
    private String estuaryLocation;

    /**
     * 河长级别
     */
    private String riverManagerGrade;

    /**
     * 河长姓名
     */
    private String riverManagerName;

    /**
     * 河长职务
     */
    private String riverManagerPhone;

    /**
     * 河长职务
     */
    private String riverManagerJob;

    /**
     * 所属单位
     */
    private String department;

    /**
     * 所在行政区划代码
     */
    private String distrinctAdcd;

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

    public RiverInfo() {
    }

}

