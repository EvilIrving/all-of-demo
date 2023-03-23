package com.ygkj.flood.model;


import java.io.Serializable;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author Huangzh
 * @description att_adcd_base
 * @date 2020-12-08
 */
@Data
public class AttAdcdBase implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 行政区划编码
     */
    private String adcd;

    /**
     * 行政区划名称
     */
    private String adnm;

    /**
     * 父级行政区划
     */
    private String parentAdcd;

    /**
     * 行政级别
     */
    private Integer level;

    /**
     * 城市的行政编码
     */
    private String cityAdcd;

    /**
     * 城市的名称
     */
    private String cityAdnm;

    /**
     * 县（区）行政编码
     */
    private String countryAdcd;

    /**
     * 县（区）名称
     */
    private String countryAdnm;

    /**
     * 乡镇编码
     */
    private String townAdcd;

    /**
     * 乡镇名称
     */
    private String townAdnm;

    /**
     * 经度
     */
    private String lgtd;

    /**
     * 纬度
     */
    private String lttd;

    /**
     * 地图级别
     */
    private String mapLevel;

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

    public AttAdcdBase() {
    }

}


