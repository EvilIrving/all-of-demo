package com.ygkj.digitization.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 水文监测站基础信息表
 *
 * @author huangzh 2021-01-12
 */
@Data
public class AttStBase implements Serializable {

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
     * 防洪高水位
     */
    private Double floodLevel;

    /**
     * 工程编码
     */
    private String projCode;

    /**
     * 行政区划编码
     */
    private String areaCode;
    /**
     * 行政区划名称
     */
    private String areaName;
    /**
     * 雨量值
     */
    private Double drp;
    /**
     * 雨量间隔时间记录
     */
    private String drpTime;

    private Boolean isFocus;

    private Boolean display;

    private Boolean isReservoirSurfaceRainStation;

    private String isSurfaceRainStation;

    private List<RailWaterCommonEntity> railCommonEntityList;

    /**
     * 潮位
     */
    private Double tdz;

    /**
     * 蓝色警戒水位
     */
    private Double blueLevel;

    /**
     * 黄色警戒水位
     */
    private Double yellowLevel;

    /**
     * 橙色警戒水位
     */
    private Double orangeLevel;

    /**
     * 红色警戒水位
     */
    private Double redLevel;

    public AttStBase() {
    }

}

