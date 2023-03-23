package com.ygkj.water.project.utils;

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

    public static void main(String[] args) {
        System.out.println(getDistance(28.001788, 120.707341, 28.006069, 120.715947).doubleValue());
    }
}
