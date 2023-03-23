package com.ygkj.big.screen.vo.response;

import lombok.Data;

import java.io.Serializable;

/**
 * @author hucong
 * @version 1.0
 * @date 2021/6/18 20:35
 */
@Data
public class Villages implements Serializable {

    private String lng;
    private String lat;
    private String warnGradeID;//5 立即转移 4 准备转移

    private String warnTypeID;
    private String warnStatusID;
    private String adcd;
    private String adnm;
    private String desc;
    //影响村落的人口
    private Integer effectPersonNumber;
    /**
     * 超过阈值数据
     */
    private double thresholdData;
    /**
     * 超过阈值数据时间
     */
    private String thresholdTime;

    //预报时间
    private String forecastTime;
    //预报降雨量
    private String forecastRainfall;
    //超预警指标
    private String overWarnIndex;
    //预警时间
    private String warnTime;
}
