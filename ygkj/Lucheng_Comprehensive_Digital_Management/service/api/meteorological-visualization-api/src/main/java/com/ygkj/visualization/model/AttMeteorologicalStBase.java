package com.ygkj.visualization.model;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Huangzh
 * @description 气象站基本信息
 * @date 2021-08-02
 */
@Data
public class AttMeteorologicalStBase implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private Integer id;

    /**
     * 气象站名称
     */
    private String stationName;

    /**
     * 气象站编码
     */
    private String stationCode;

    /**
     * 区县名称
     */
    private String areaName;

    /**
     * 乡镇街道
     */
    private String street;

    /**
     * 地址
     */
    private String address;

    /**
     * 是否气温站
     */
    private String temperature;

    /**
     * 是否气压站
     */
    private String press;

    /**
     * 是否雨量站
     */
    private String rainfall;

    /**
     * 是否风向站
     */
    private String windDirection;

    /**
     * 是否风速站
     */
    private String windSpeed;

    /**
     * 是否湿度站
     */
    private String humidity;

    private Double longitude;

    private Double latitude;

    /**
     * 气象监测数据实体
     */
    private StMeR meteorological;

    /**
     * 风力级别，判断meteorological成员变量的风速数值得到
     */
    private String windLevel;

    /**
     * 风向，根据meteorological成员变量的风向数值得到
     */
    private String windDirect;

    private Map<Long, String> windDirectMap;

    private Map<Long, String> windDirectOffsetMap;

    private void init() {
        windDirectMap = new HashMap<>();
        windDirectOffsetMap = new HashMap<>();
        windDirectMap.put(90l, "东风");
        windDirectMap.put(0l, "北风");
        windDirectMap.put(180l, "南风");
        windDirectMap.put(270l, "西风");
        windDirectOffsetMap.put(0l, "东北风");
        windDirectOffsetMap.put(3l, "西北风");
        windDirectOffsetMap.put(2l, "西南风");
        windDirectOffsetMap.put(1l, "东南风");
    }

    public AttMeteorologicalStBase() {
        init();
    }

    public AttMeteorologicalStBase(StMeR meteorological) {
        this.meteorological = meteorological;
        init();
        obtainWindDirect();
    }

    public void setMeteorological(StMeR meteorological) {
        this.meteorological = meteorological;
        obtainWindDirect();
    }

    private void obtainWindDirect() {
        if (null != this.meteorological) {
            Double direct = meteorological.getWindDirect();
            if (direct % 90 == 0) {
                windDirect = windDirectMap.getOrDefault(direct.longValue(), StringUtils.EMPTY);
            } else {
                windDirect = windDirectOffsetMap.getOrDefault((long) (direct / 90), StringUtils.EMPTY);
            }
        }
    }
}


