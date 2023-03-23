package com.ygkj.lcsyn.model;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class StStbprpB {

    /**
     * 测站编码
     */
    private String stcd;

    /**
     * 测站名称
     */
    private String stnm;

    /**
     * 河流名称
     */
    private String rvnm;

    /**
     * 水系名称
     */
    private String hnnm;

    /**
     * 流域名称
     */
    private String bsnm;

    /**
     * lgtd
     */
    private BigDecimal lgtd;

    /**
     * lttd
     */
    private BigDecimal lttd;

    /**
     * stlc
     */
    private String stlc;

    /**
     * addvcd
     */
    private String addvcd;

    /**
     * 基面名称
     */
    private String dtmnm;

    /**
     * 基面高程
     */
    private BigDecimal dtmel;

    /**
     * 修正基面值
     */
    private BigDecimal dtpr;

    /**
     * sttp
     */
    private String sttp;

    /**
     * frgrd
     */
    private String frgrd;

    /**
     * esstym
     */
    private String esstym;

    /**
     * bgfrym
     */
    private String bgfrym;

    /**
     * 隶属行业单位
     */
    private String atcunit;

    /**
     * 信息管理单位
     */
    private String admauth;

    /**
     * 交换管理单位
     */
    private String locality;

    /**
     * stbk
     */
    private String stbk;

    /**
     * stazt
     */
    private Integer stazt;

    /**
     * dstrvm
     */
    private BigDecimal dstrvm;

    /**
     * 集水面积
     */
    private BigDecimal drna;

    /**
     * phcd
     */
    private String phcd;

    /**
     * usfl
     */
    private String usfl;

    /**
     * comments
     */
    private String comments;

    /**
     * 遥测站号
     */
    private Date moditime;

    /**
     * sortid
     */
    private Integer sortid;

    /**
     * sortid_all
     */
    private Integer sortidAll;

    /**
     * sortid_river
     */
    private Integer sortidRiver;

    /**
     * type
     */
    private Integer type;

    /**
     * tstcd
     */
    private String tstcd;

    /**
     * gstcd
     */
    private String gstcd;

    /**
     * hour
     */
    private Integer hour;

    /**
     * isauto
     */
    private int isauto;
}
