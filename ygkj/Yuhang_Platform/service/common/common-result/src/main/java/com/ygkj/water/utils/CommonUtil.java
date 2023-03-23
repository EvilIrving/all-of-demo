package com.ygkj.water.utils;


import java.util.Calendar;
import java.util.Date;

/**
 * @author hucong
 * @version 1.0
 * @date 2020/12/15 10:28
 * 常用工具类
 */
public class CommonUtil {

    /**
     * 地球半径,单位 km
     */
    private static final double EARTH_RADIUS = 6378.137;

    /**
     * 对日期进行 年 月 日 时 分 秒 的加减
     * @param curDate			要计算的日期
     * @param plusYear			年数		(+/-)正负亦可
     * @param plusMonth			月数		(+/-)正负亦可
     * @param plusDay			天数		(+/-)正负亦可
     * @param plusHour			小时数	(+/-)正负亦可
     * @param plusMinute		分数		(+/-)正负亦可
     * @param plusSecond		秒数		(+/-)正负亦可
     * @return	计算后的日期
     */
    public static Date getNextDay(Date curDate, Integer plusYear, Integer plusMonth, Integer plusDay, Integer plusHour, Integer plusMinute, Integer plusSecond){
        Calendar c = Calendar.getInstance();
        c.setTime(curDate==null?new Date():curDate);
        if(plusYear!=null){
            c.set(Calendar.YEAR, c.get(Calendar.YEAR)+plusYear);
        }
        if(plusMonth!=null){
            c.set(Calendar.MONTH, c.get(Calendar.MONTH)+plusMonth);
        }
        if(plusDay!=null){
            c.set(Calendar.DAY_OF_YEAR, c.get(Calendar.DAY_OF_YEAR)+plusDay);
        }
        if(plusHour!=null){
            c.set(Calendar.HOUR_OF_DAY, c.get(Calendar.HOUR_OF_DAY)+plusHour);
        }
        if(plusMinute!=null){
            c.set(Calendar.MINUTE, c.get(Calendar.MINUTE)+plusMinute);
        }
        if(plusSecond!=null){
            c.set(Calendar.SECOND, c.get(Calendar.SECOND)+plusSecond);
        }
        return c.getTime();
    }


    /**
     * 根据经纬度，计算两点间的距离
     *
     * @param longitude1 第一个点的经度
     * @param latitude1  第一个点的纬度
     * @param longitude2 第二个点的经度
     * @param latitude2  第二个点的纬度
     * @return 返回距离 单位千米
     */
    public static double getDistance(double longitude1, double latitude1, double longitude2, double latitude2) {
        // 纬度
        double lat1 = Math.toRadians(latitude1);
        double lat2 = Math.toRadians(latitude2);
        // 经度
        double lng1 = Math.toRadians(longitude1);
        double lng2 = Math.toRadians(longitude2);
        // 纬度之差
        double a = lat1 - lat2;
        // 经度之差
        double b = lng1 - lng2;
        // 计算两点距离的公式
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2) +
                Math.cos(lat1) * Math.cos(lat2) * Math.pow(Math.sin(b / 2), 2)));
        // 弧长乘地球半径, 返回单位: 千米
        s =  s * EARTH_RADIUS;
        return s;
    }

    /**
     * 忽略大小写比较两个字符串</b>
     * ignoreCaseEquals()
     * @param str1
     * @param str2
     * @return
     */
    public static boolean ignoreCaseEquals(String str1,String str2){
        return str1 == null ? str2 == null :str1.equalsIgnoreCase(str2);
    }


}
