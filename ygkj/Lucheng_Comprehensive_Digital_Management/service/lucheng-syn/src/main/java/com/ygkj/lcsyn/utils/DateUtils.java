package com.ygkj.lcsyn.utils;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.util.*;

/**
 * @author xq
 * @Description 时间日期处理工具
 * @Date 2021/1/16
 */
public class DateUtils {

    public final static ZoneId CN_ZONE=ZoneId.of("Asia/Shanghai");

    public static Date clearDateTimePrecision(Date source, int... calendarFields) {
        if (source != null
                && calendarFields != null
                && calendarFields.length > 0) {
            Calendar ca = Calendar.getInstance();
            ca.setTime(source);
            for (int i : calendarFields) {
                ca.set(i, 0);
            }
            return ca.getTime();
        } else {
            return source;
        }
    }

    public static Date addValForDateTime(Date source, int calendarField, int val) {
        if (source != null) {
            Calendar ca = Calendar.getInstance();
            ca.setTime(source);
            ca.add(calendarField, val);
            return ca.getTime();
        } else {
            return source;
        }
    }


    public static Date localDateTime2Date(LocalDateTime localDateTime){
        ZonedDateTime zdt = localDateTime.atZone(CN_ZONE);
        return Date.from(zdt.toInstant());
    }
    public static Date localDate2Date(LocalDate localDate){
        ZonedDateTime zdt = localDate.atStartOfDay(CN_ZONE);
        return  Date.from(zdt.toInstant());
    }

    public static LocalDateTime date2LocalDateTime(Date date){
        Instant instant = date.toInstant();
        return instant.atZone(CN_ZONE).toLocalDateTime();
    }

    public static LocalDate date2LocalDate(Date date){
        Instant instant = date.toInstant();
        return instant.atZone(CN_ZONE).toLocalDate();
    }


    /**
     * 获取本周第一天
     *
     * @return
     */
    public static String getCurrentWeekFirstDay() {
        Calendar curStartCal = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
        Calendar cal = (Calendar) curStartCal.clone();
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        return df.format(cal.getTime());
    }

    /**
     * 获取本月第一天
     *
     * @return
     */
    public static String getCurrentMonthFirstDay() {
        Calendar curStartCal = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
        Calendar cal = (Calendar) curStartCal.clone();
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
        return df.format(cal.getTime());
    }

    /**
     * 获取本月最后一天
     *
     * @return
     */
    public static String getCurrentMonthLastDay() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd 23:59:59");
        Calendar ca = Calendar.getInstance();
        ca.set(Calendar.DAY_OF_MONTH, ca.getActualMaximum(Calendar.DAY_OF_MONTH));
        return format.format(ca.getTime());
    }


    /**
     * 获取本年度第一天
     *
     * @return
     */
    public static String getCurrentYearFirstDay() {
        Calendar curStartCal = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
        Calendar cal = (Calendar) curStartCal.clone();
        cal.set(Calendar.DAY_OF_YEAR, cal.getActualMinimum(Calendar.DAY_OF_YEAR));
        return df.format(cal.getTime());
    }

    /**
     * 获取两个时间范围内包含的天数
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return
     */
    public static Integer getDays(String startTime, String endTime) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        int days = 1;
        try {
            startTime = startTime.substring(0, 10);
            endTime = endTime.substring(0, 10);
            days = (int) ((df.parse(endTime).getTime() - df.parse(startTime).getTime()) / (24 * 60 * 60 * 1000)) + 1;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Math.abs(days);
    }


    public static String addDay(int add) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, add);
        //Date d = cal.getTime();//这个就是7天前的Date了
        return new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());
    }

    /**
     * 根据开始时间结束时间中间查询日期
     *
     * @param beginDate
     * @param endDate
     * @return
     * @throws ParseException
     */
    public static List<String> getDateListStr(String beginDate, String endDate) throws ParseException {
        List<String> list = new LinkedList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        cal.setTime(sdf.parse(beginDate));

        for (long d = cal.getTimeInMillis(); d <= sdf.parse(endDate).getTime(); d = get_D_Plaus_1(cal)) {
            //System.out.println(sdf.format(d));
            list.add(sdf.format(d));
        }
        return list;
    }

    public static long get_D_Plaus_1(Calendar c) {
        c.set(Calendar.DAY_OF_MONTH, c.get(Calendar.DAY_OF_MONTH) + 1);
        return c.getTimeInMillis();
    }

    public static void main(String[] args) {
        System.out.println("本月第一天:" + addDay(-7));
    }


    public static List<String> getMonthBetween(String minDate, String maxDate) throws ParseException {
        ArrayList<String> result = new ArrayList<String>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        Calendar min = Calendar.getInstance();
        Calendar max = Calendar.getInstance();
        min.setTime(sdf.parse(minDate));
        min.set(min.get(Calendar.YEAR), min.get(Calendar.MONTH), 1);

        max.setTime(sdf.parse(maxDate));
        max.set(max.get(Calendar.YEAR), max.get(Calendar.MONTH), 2);
        Calendar curr = min;
        while (curr.before(max)) {
            result.add(sdf.format(curr.getTime()));
            curr.add(Calendar.MONTH, 1);
        }
        return result;
    }

    public static String nextYearMonth() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) + 1);
        return sdf.format(calendar.getTime());
    }

    /**
     * 对日期进行 年 月 日 时 分 秒 的加减
     *
     * @param curDate    要计算的日期
     * @param plusYear   年数		(+/-)正负亦可
     * @param plusMonth  月数		(+/-)正负亦可
     * @param plusDay    天数		(+/-)正负亦可
     * @param plusHour   小时数	(+/-)正负亦可
     * @param plusMinute 分数		(+/-)正负亦可
     * @param plusSecond 秒数		(+/-)正负亦可
     * @return 计算后的日期
     */
    public static Date getNextDay(Date curDate, Integer plusYear, Integer plusMonth, Integer plusDay, Integer plusHour, Integer plusMinute, Integer plusSecond) {
        Calendar c = Calendar.getInstance();
        c.setTime(curDate == null ? new Date() : curDate);
        if (plusYear != null) {
            c.set(Calendar.YEAR, c.get(Calendar.YEAR) + plusYear);
        }
        if (plusMonth != null) {
            c.set(Calendar.MONTH, c.get(Calendar.MONTH) + plusMonth);
        }
        if (plusDay != null) {
            c.set(Calendar.DAY_OF_YEAR, c.get(Calendar.DAY_OF_YEAR) + plusDay);
        }
        if (plusHour != null) {
            c.set(Calendar.HOUR_OF_DAY, c.get(Calendar.HOUR_OF_DAY) + plusHour);
        }
        if (plusMinute != null) {
            c.set(Calendar.MINUTE, c.get(Calendar.MINUTE) + plusMinute);
        }
        if (plusSecond != null) {
            c.set(Calendar.SECOND, c.get(Calendar.SECOND) + plusSecond);
        }
        return c.getTime();
    }

    /**
     * 根据 年、月 获取对应的月份 的 天数
     */
    public static int getDaysByYearMonth(int year, int month) {
        Calendar a = Calendar.getInstance();
        a.set(Calendar.YEAR, year);
        a.set(Calendar.MONTH, month - 1);
        a.set(Calendar.DATE, 1);
        a.roll(Calendar.DATE, -1);
        int maxDate = a.get(Calendar.DATE);
        return maxDate;
    }



    /**
     * 根据开始时间、结束时间获取表名
     */
    public static String[] getTableNamesByTimeInterval(String tablePrefix,String startTime,String endTime){

        try {
            List<String> dateListStr = getDateListStr(startTime, endTime);
            Map<String,String> map = new HashMap<>();
            for (String time : dateListStr) {
                String ym = time.substring(0, 7).replace("-","");
                map.put(tablePrefix +ym,tablePrefix + ym);
            }
            ArrayList<String> list = new ArrayList<>(map.keySet());
            String[] arr = new String[list.size()];
            list.toArray(arr);//将转化后的数组放入已经创建好的对象中
            return arr;
        } catch (ParseException e) {
            return null;
        }
    }

    public static Date minOf(Date a,Date b){
        return a.before(b)?a:b;
    }
}
