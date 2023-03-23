package com.ygkj.big.screen.utils;

import java.math.BigDecimal;

/**
 * @author fml
 * @Date 2020/9/23 01:09
 * @description Double工具类
 */
public class DoubleUtils {

    private static final int DEF_DIV_SCALE = 2;

    /**
     * 两个Double数相加
     *
     * @param d1 值1
     * @param d2 值2
     * @return Double
     */
    public static Double sum(Double d1, Double d2) {
        BigDecimal b1 = new BigDecimal(d1.toString());
        BigDecimal b2 = new BigDecimal(d2.toString());
        return b1.add(b2).doubleValue();
    }

    /**
     * 两个Double数相减
     *
     * @param d1 值1
     * @param d2 值2
     * @return Double
     */
    public static Double sub(Double d1, Double d2) {
        BigDecimal b1 = new BigDecimal(d1.toString());
        BigDecimal b2 = new BigDecimal(d2.toString());
        return b1.subtract(b2).doubleValue();
    }

    /**
     * 两个Double数相乘
     *
     * @param d1 值1
     * @param d2 值2
     * @return Double
     */
    public static Double mul(Double d1, Double d2) {
        BigDecimal b1 = new BigDecimal(d1.toString());
        BigDecimal b2 = new BigDecimal(d2.toString());
        return b1.multiply(b2).doubleValue();
    }

    /**
     * 两个Double数相除
     *
     * @param d1 值1
     * @param d2 值2
     * @return Double
     */
    public static Double div(Double d1, Double d2) {
        BigDecimal b1 = new BigDecimal(d1.toString());
        BigDecimal b2 = new BigDecimal(d2.toString());
        return b1.divide(b2, DEF_DIV_SCALE, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * 两个Double数相除，并保留scale位小数
     *
     * @param d1    值1
     * @param d2    值2
     * @param scale 并保留位数
     * @return Double
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
}
