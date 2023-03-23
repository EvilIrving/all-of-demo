package com.ygkj.utils;

import java.math.BigDecimal;

public class DistanceUtil {

    private static double EARTH_RADIUS = 6378.137;

    private static double rad(double d) {
        return d * Math.PI / 180.0;
    }

    public static BigDecimal getDistance(double lat1, double lng1, double lat2,
                                         double lng2) {
        double radLat1 = rad(lat1);
        double radLat2 = rad(lat2);
        double a = radLat1 - radLat2;
        double b = rad(lng1) - rad(lng2);
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2)
                + Math.cos(radLat1) * Math.cos(radLat2)
                * Math.pow(Math.sin(b / 2), 2)));
        s = s * EARTH_RADIUS;
        s = Math.round(s * 10000d) / 10000d;
        BigDecimal bd = new BigDecimal(s);
        bd = bd.setScale(2, BigDecimal.ROUND_HALF_UP);    //保留小数点后两位
        return bd;
    }

    /**
     * 根据起始点,角度和距离计算经纬度
     *
     * @param angle     角度 正北方向顺时针旋转
     * @param startLong 起始经度
     * @param startLat  起始纬度
     * @param distance  距离 单位为公里
     * @return
     */
    public static double[] calLocationByDistanceAndLocationAndDirection(double angle, double startLong, double startLat, double distance) {
        double[] result = new double[2];
        //将距离转换成经度的计算公式
        double δ = distance / EARTH_RADIUS;
        angle = Math.toRadians(angle);
        startLong = Math.toRadians(startLong);
        startLat = Math.toRadians(startLat);
        double lat = Math.asin(Math.sin(startLat) * Math.cos(δ) + Math.cos(startLat) * Math.sin(δ) * Math.cos(angle));
        double lon = startLong + Math.atan2(Math.sin(angle) * Math.sin(δ) * Math.cos(startLat), Math.cos(δ) - Math.sin(startLat) * Math.sin(lat));
        // 转为正常的10进制经纬度
        lon = Math.toDegrees(lon);
        lat = Math.toDegrees(lat);
        result[0] = lon;
        result[1] = lat;
        return result;
    }

    public static void main(String[] args) {
//        System.out.println(getDistance(28.001788,120.707341,28.006069,120.715947).doubleValue());
    }
}
