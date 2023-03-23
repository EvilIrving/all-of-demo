package com.dcy.datamanage.biz.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;

public class NumberUtil {

    public static final DecimalFormat NORMAL = new DecimalFormat("#.##");

    public static BigDecimal format(Number number, DecimalFormat df) {
        return new BigDecimal(df.format(number));
    }

    public static BigDecimal format(Number number, String format) {
        return new BigDecimal(new DecimalFormat(format).format(number));
    }
}
