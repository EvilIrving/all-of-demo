package com.ygkj.gragh.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author Huangzh
 * @description 水文监测站基础信息表
 * @date 2020-12-08
 */
@Data
public class AttStBase implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 水文监测站代码
     */
    @TableId(type = IdType.INPUT)
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

    private String longitude;

    private String latitude;

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
     * 防洪高水位
     */
    private Double floodLevel;

    /**
     * 警戒水位
     */
    private Double warningLevel;

    /**
     * 工程编码
     */
    private String projCode;

    private String status;

    private double rainfall;

    private double waterLevel;

    private String areaCode;

    /**
     * 流域
     */
    private String bas;

    /**
     * 最高水位潮位
     */
    private BigDecimal highestLevel;

    /**
     * 最高水位潮位出现时间
     */
    private Date appearTime;

    /**
     * 历史最低水位，潮位
     */
    private BigDecimal recordLow;

    /**
     * 历史最低水位，潮位发生时间
     */
    private Date lowTime;

    private String resGrade;

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

    private boolean display;

    /**
     * 闸门开度阈值
     */
    private Double gateOpenThreshold;

    /**
     * 土壤墒情
     */
    private StSoliR stSoliR;

    /**
     * 超警戒值
     */
    private Double overWarnLevel;


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
    private Integer compareLast = 0;

    private Boolean online = Boolean.TRUE;

    /**
     * 行政区划名称
     */
    private String adName;

    public AttStBase() {
    }

}
