package com.ygkj.big.screen.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 水文监测站基础信息表
 */
@Data
public class AttStBase implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("水文监测站代码")
    private String stCode;


    @ApiModelProperty("水文监测站名称")
    private String stName;


    @ApiModelProperty("水文监测站类型（rr水库水位站，pp雨量站，dd闸坝水位站，tt潮位站，zg地下水位站，zq流量站，zz河道）")
    private String stType;


    @ApiModelProperty("水文监测站经度")
    private Double stLong;


    @ApiModelProperty("水文监测站纬度")
    private Double stLat;


    @ApiModelProperty("站址")
    private String stLoc;


    @ApiModelProperty("设站年月")
    private String stYearMon;


    @ApiModelProperty("始报年月")
    private String begRepoYearMon;


    @ApiModelProperty("岸别")
    private String bank;


    @ApiModelProperty("监测站方位")
    private Integer stDir;


    @ApiModelProperty("集雨面积")
    private Double catArea;


    @ApiModelProperty("检测项目")
    private String moniItem;


    @ApiModelProperty("基准基面名称")
    private String dtmName;


    @ApiModelProperty("基面高差")
    private Double dtmElev;


    @ApiModelProperty("站别")
    private String types;


    @ApiModelProperty("性质")
    private String attr;


    @ApiModelProperty("管理人员(手机)")
    private String staff;


    @ApiModelProperty("距离河口")
    private String estuaryDistance;


    @ApiModelProperty("观测场周边环境")
    private String obsEnv;


    @ApiModelProperty("测验河段及附近河流情况")
    private String riverState;


    @ApiModelProperty("备注")
    private String note;


    @ApiModelProperty("记录生效时间")
    private String effString;


    @ApiModelProperty("记录生效时间")
    private String exprString;


    @ApiModelProperty("汛限水位")
    private Double limitLevel;


    @ApiModelProperty("防洪高水位")
    private Double floodLevel;


    @ApiModelProperty("保证水位")
    private Double guaranteeLevel;


    @ApiModelProperty("警戒水位")
    private Double warningLevel;


    @ApiModelProperty("蓝色警戒水位")
    private Double blueLevel;


    @ApiModelProperty("黄色警戒水位")
    private Double yellowLevel;


    @ApiModelProperty("橙色警戒水位")
    private Double orangeLevel;


    @ApiModelProperty("红色警戒水位")
    private Double redLevel;


    @ApiModelProperty("工程编码")
    private String projCode;


    @ApiModelProperty("行政区划编码")
    private String areaCode;

    /**
     * 行政区划名称
     */
    @ApiModelProperty("行政区划名称")
    private String areaName;


    @ApiModelProperty("流域")
    private String bas;


    @ApiModelProperty("关联海塘的备注")
    private String seawallRemark;


    @ApiModelProperty("历史最高水位")
    private Double highestLevel;


    @ApiModelProperty("历史最高水位发生时间")
    private String appearTime;

    /**
     * 历史最低水位，潮位
     */
    private BigDecimal recordLow;

    /**
     * 历史最低水位，潮位发生时间
     */
    private Date lowTime;

    @ApiModelProperty("历史最大雨量")
    private Double highestRain;


    @ApiModelProperty("历史最大雨量时间")
    private String highestRainTime;


    @ApiModelProperty("大中型水库/小型水库")
    private String resGrade;

    /**
     * 记录生效时间
     */
    private Date effDate;

    /**
     * 记录生效时间
     */
    private Date exprDate;

    private String status;

    private double rainfall;

    private double waterLevel;

    //闸上水位
    private double upz;
    //闸下水位
    private double dwz;
    //开度
    private String openGate;
    //最大过闸流量
    private String flow;
    //监测时间
    private Date time;

    /**
     * 流量
     */
    private String ll;

    private double tdz;

    private boolean warning;

    private boolean isCollected;

    /**
     * 闸门开度阈值
     */
    private Double gateOpenThreshold;

    /**
     * 超警戒值
     */
    private Double overWarnLevel;

    private boolean display;

    /**
     * 机组1开关情况
     */
    private String num1;

    /**
     * 机组2开关情况
     */
    private String num2;

    /**
     * 机组3开关情况
     */
    private String num3;

    /**
     * 机组4开关情况
     */
    private String num4;

    /**
     * 机组5开关情况
     */
    private String num5;

    /**
     * 进水口水位
     */
    private Double jskZ;

    /**
     * 进水池水位
     */
    private Double jscZ;

    /**
     * 出水池水位
     */
    private Double cscZ;

    /**
     * 和上个时刻对比,0持平，1上升，-1下降
     */
    private Integer compareLastTime = 0;

    /**
     *
     */
    private String tideWarnType;

    public AttStBase() {
    }

}
