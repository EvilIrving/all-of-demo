package com.ygkj.visualization.model;


import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Huangzh
 * @description 气象站实时监测数据表
 * @date 2021-08-02
 */
@Data
public class StMeR implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 气象站编码
     */
    private String mgstcd;

    /**
     * 观测时间
     */
    private Date tm;

    /**
     * 风速
     */
    private Double windVelocity;

    /**
     * 相对湿度
     */
    private Double relhumidity;

    /**
     * 风向
     */
    private Double windDirect;

    /**
     * 降水量
     */
    private Double precipition;

    /**
     * station_press
     */
    private Double stationPress;

    /**
     * 滴水温度
     */
    private Double drybultemp;

    /**
     * 蒸汽压
     */
    private Double vapourPress;

    private String direct;

    public StMeR() {
    }

    public StMeR(String mgstcd, Date tm,
                 Double windVelocity, Double relhumidity,
                 Double windDirect, Double precipition,
                 Double stationPress, Double drybultemp, Double vapourPress) {
        this.mgstcd = mgstcd;
        this.tm = tm;
        this.windVelocity = windVelocity;
        this.relhumidity = relhumidity;
        this.windDirect = windDirect;
        this.precipition = precipition;
        this.stationPress = stationPress;
        this.drybultemp = drybultemp;
        this.vapourPress = vapourPress;
    }

    public void setWindVelocity(Double windVelocity) {
        this.windVelocity = windVelocity / 10;
    }

    public void setPrecipition(Double precipition) {
        this.precipition = precipition / 10;
    }

    public void setWindDirect(Double windDirect) {
        this.windDirect = windDirect;
        if (windDirect % 90 == 0) {
            if (windDirect == 0) {
                direct = "北风";
            } else if (windDirect == 90) {
                direct = "东风";
            } else if (windDirect == 180) {
                direct = "南风";
            } else if (windDirect == 270) {
                direct = "西风";
            }
        } else {
            int index = windDirect.intValue() / 90;
            if (index == 0) {
                direct = "东北风";
            } else if (index == 1) {
                direct = "东南风";
            } else if (index == 2) {
                direct = "西南风";
            } else if (index == 3) {
                direct = "西北风";
            }
        }
    }
}

