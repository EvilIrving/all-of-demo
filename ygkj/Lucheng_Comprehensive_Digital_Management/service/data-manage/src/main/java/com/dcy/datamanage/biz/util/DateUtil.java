package com.dcy.datamanage.biz.util;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class DateUtil {
    private static final String FORMAT_DATE = "yyyy-MM-dd";
    private static final String FORMAT_TIME = "HH:mm:ss";
    private static final String FORMAT_DATETIME = "yyyy-MM-dd HH:mm:ss";

    public static final SimpleDateFormat SDF_DATE = new SimpleDateFormat(FORMAT_DATE);
    public static final SimpleDateFormat SDF_TIME = new SimpleDateFormat(FORMAT_TIME);
    public static final SimpleDateFormat SDF_DATETIME = new SimpleDateFormat(FORMAT_DATETIME);
    public static final DateTimeFormatter DTF_DATE = DateTimeFormatter.ofPattern(FORMAT_DATE);
    public static final DateTimeFormatter DTF_TIME = DateTimeFormatter.ofPattern(FORMAT_TIME);
    public static final DateTimeFormatter DTF_DATETIME = DateTimeFormatter.ofPattern(FORMAT_DATETIME);

    public static Date strToDate(String str, SimpleDateFormat sdf) {
        try {
            return sdf.parse(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Date strToDate(String str, String format) {
        return strToDate(str, new SimpleDateFormat(format));
    }

    public static String dateToStr(Date date, SimpleDateFormat sdf) {
        return sdf.format(date);
    }

    public static String dateToStr(Date date, String format) {
        return dateToStr(date, new SimpleDateFormat(format));
    }

    public static String dateToStr(LocalDateTime localDateTime, DateTimeFormatter dtf) {
        return dtf.format(localDateTime);
    }

    public static String dateToStr(LocalDateTime localDateTime, String format) {
        return dateToStr(localDateTime, DateTimeFormatter.ofPattern(format));
    }

    public static Date timeToDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

}
