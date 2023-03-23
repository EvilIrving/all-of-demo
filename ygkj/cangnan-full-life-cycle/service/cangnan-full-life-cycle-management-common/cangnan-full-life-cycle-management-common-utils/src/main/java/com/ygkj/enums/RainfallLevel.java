package com.ygkj.enums;

/**
 * @author xq
 * @Description 防汛形势研判-降水分析-雨量统计  中的饼图
 * @Date 2021/1/18
 */

public enum RainfallLevel {
    NO_RAIN(Double.MIN_VALUE,0.0D,"无降水"),
    LOWER10(0.0D,10.0D,"<10"),
    BETWEEN10AND25(10.0D,25.0D,"10~25"),
    BETWEEN25AND50(25.0D,50.0D,"25~50"),
    BETWEEN50AND100(50.0D,100.0D,"50~100"),
    BETWEEN100AND250(100.0D,250.0D,"100~250"),
    HIGHER250(250.0D,Double.MAX_VALUE,">=250");

    Double drpMin;
    Double drpMax;
    String description;

    RainfallLevel(Double drpMin, Double drpMax, String description) {
        this.drpMin = drpMin;
        this.drpMax = drpMax;
        this.description = description;
    }

    public static RainfallLevel ensureRainfallLevel(Double target){
        if (target==null){
            return NO_RAIN;
        }else{
            RainfallLevel result=NO_RAIN;
            for (RainfallLevel value : RainfallLevel.values()) {
                if (target>=value.drpMin&&target<value.drpMax){
                    result=NO_RAIN;
                    break;
                }
            }
            return result;
        }
    }


}
