package com.ygkj.flood.model;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class StBase implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 水文监测站代码
     */
    private String stCode;

    /**
     * 水文监测站名称
     */
    private String stName;

    /**
     * 水文监测站类型
     */
    private String stType;

    /**
     * 水文监测站经度
     */
    private BigDecimal stLong;

    /**
     * 水文监测站维度
     */
    private BigDecimal stLat;

    /**
     * 站址
     */
    private String stLoc;

    /**
     * 设站年月
     */
    private Date stYearMon;

    /**
     * 始报年月
     */
    private Date begRepoYearMon;

    /**
     * 岸别
     */
    private String bank;

    /**
     * 监测站方位
     */
    private Integer stDir;

    /**
     * 集雨面积
     */
    private BigDecimal catArea;

    /**
     * 检测项目
     */
    private String moniItem;

    /**
     * 基准基面名称
     */
    private String dtmName;

    /**
     * 基面高差
     */
    private BigDecimal dtmElev;

    /**
     * 站别
     */
    private String types;

    /**
     * 性质
     */
    private String attr;

    /**
     * 管理人员(手机)
     */
    private String staff;

    /**
     * 距离河口
     */
    private String estuaryDistance;

    /**
     * 观测场周边环境
     */
    private String obsEnv;

    /**
     * 测验河段及附近河流情况
     */
    private String riverState;

    /**
     * 备注
     */
    private String note;

    /**
     * 记录生效时间
     */
    private Date effDate;

    /**
     * 记录生效时间
     */
    private Date exprDate;

    /**
     * 汛限水位
     */
    private Double limitLevel;

    /**
     * 保证水位
     */
    private Double guaranteeLevel;

    /**
     * 警戒水位
     */
    private Double warningLevel;

    /**
     * 工程编码
     */
    private String projCode;

    private String status;

    private String bas;

    private double rainfall;

    private double waterLevel;

    private String areaCode;

    private String areaName;

    //闸上水位 UPZ
    private double upz;
    //闸下水位 DWZ
    private double dwz;
    //开度  OVS
    private String openGate;
    //最大过闸流量 MXGTQ
    private String flow;
    //监测时间 TM
    private Date time;
    //水库名称
    private String resName;
    //梅汛期限制水位
    private BigDecimal meiFloodLimitWaterLevel;

    //超预警值
    private Double overWarnLevel;

    private boolean warning;

    // 超保证
    private boolean overGuarantee;
}
