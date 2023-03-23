package com.ygkj.water.utils;

import java.math.BigDecimal;

/**
 * @author: fml
 * @Date: 2020/9/23 01:09
 * @Description:
 */
public class DoubleUtils {

    private static final int DEF_DIV_SCALE = 2;

    /**
     * @param d1
     * @param d2
     * @return Double
     * @Description 两个Double数相加
     */
    public static Double sum(Double d1, Double d2) {
        BigDecimal b1 = new BigDecimal(d1.toString());
        BigDecimal b2 = new BigDecimal(d2.toString());
        return b1.add(b2).doubleValue();
    }

    /**
     * @param d1
     * @param d2
     * @return Double
     * @Description 两个Double数相减
     */
    public static Double sub(Double d1, Double d2) {
        BigDecimal b1 = new BigDecimal(d1.toString());
        BigDecimal b2 = new BigDecimal(d2.toString());
        return b1.subtract(b2).doubleValue();
    }

    /**
     * @param d1
     * @param d2
     * @return Double
     * @Description 两个Double数相乘
     */
    public static Double mul(Double d1, Double d2) {
        BigDecimal b1 = new BigDecimal(d1.toString());
        BigDecimal b2 = new BigDecimal(d2.toString());
        return b1.multiply(b2).doubleValue();
    }

    /**
     * @param d1
     * @param d2
     * @return Double
     * @Description 两个Double数相除
     */
    public static Double div(Double d1, Double d2) {
        BigDecimal b1 = new BigDecimal(d1.toString());
        BigDecimal b2 = new BigDecimal(d2.toString());
        return b1.divide(b2, DEF_DIV_SCALE, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * @param d1
     * @param d2
     * @param scale
     * @return Double
     * @Description 两个Double数相除，并保留scale位小数
     */
    public static Double div(Double d1, Double d2, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException(
                    "The scale must be a positive integer or zero");
        }
        BigDecimal b1 = new BigDecimal(d1.toString());
        BigDecimal b2 = new BigDecimal(d2.toString());
        return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    public static int compare(Double d1, Double d2){
        BigDecimal data1 = new BigDecimal(d1);
        BigDecimal data2 = new BigDecimal(d2);
        return data1.compareTo(data2);
    }
}
