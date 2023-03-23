package com.ygkj.gragh.model;

import java.io.Serializable;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author xq
 * @description 闸站工程基础信息表
 * @date 2021-04-25
 */
@Data
public class AttGateBase implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 闸站代码
     */
    private String gateCode;

    /**
     * 闸站名称
     */
    private String gateName;

    /**
     * 经度
     */
    private BigDecimal longitude;

    /**
     * 维度
     */
    private BigDecimal latitude;

    /**
     * 所在市
     */
    private String locCity;

    /**
     * 所在县
     */
    private String locCounty;

    /**
     * 所在街道
     */
    private String locStreet;

    /**
     * 工程规模
     */
    private String engScal;

    /**
     * 单台装机流量
     */
    private BigDecimal singleInsFlow;

    /**
     * 单台装机功率
     */
    private BigDecimal singleInsPow;

    /**
     * 最大过闸流量
     */
    private BigDecimal maxFlow;

    /**
     * 记录生效时间
     */
    private Date effDate;

    /**
     * 记录失效时间
     */
    private Date exprDate;

    /**
     * 行政区划代码
     */
    private String areaCode;

    /**
     * 所属流域
     */
    private String bas;

    /**
     * 所在河流
     */
    private String river;

    private String managementLevel;

    public AttGateBase() {
    }

}
