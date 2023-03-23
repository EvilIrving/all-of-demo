package com.ygkj.lcsyn.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @description 山洪易发区影响人口信息
 * @author
 * @date 2022-05-10
 */
@Data
public class PvFloodAffectPopulation implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 村落编码
     */
    private String prevCode;

    /**
     * 行政区划编码
     */
    private String adcd;

    /**
     * 行政村
     */
    private String adminAdnm;

    /**
     * 自然村
     */
    private String naturalAdnm;

    /**
     * 防御对象
     */
    private String defenObj;

    /**
     * 防御类型
     */
    private String objtp;

    /**
     * 联系人
     */
    private String contact;

    /**
     * 联系人号码
     */
    private String contacttel;

    /**
     * 影响人数
     */
    private Integer pcount;

    /**
     * 乡镇名称
     */
    private String townName;

    /**
     * 县区名称
     */
    private String countyName;

    /**
     * 地市名称
     */
    private String cityName;

    /**
     * 同步时间
     */
    private Date tongTime;

    /**
     * 删除标记 0未删除 1已删除
     */
    private Integer delFlag;

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

    public PvFloodAffectPopulation() {
    }

}
