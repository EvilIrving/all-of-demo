package com.ygkj.big.screen.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class DateUtil {

    /**
     * 获取今天开始时间和结束时间
     *
     * @return
     */
    public static Map getTodayTime() {
        Long startTime = getStartTime();
        //Long endTime = getEndTime(); //当期时间

        Long endTime = System.currentTimeMillis(); //当期时间
        DateTimeFormatter ftf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String startTimeStr = ftf.format(LocalDateTime.ofInstant(Instant.ofEpochMilli(startTime), ZoneId.systemDefault()));
        String endTimeStr = ftf.format(LocalDateTime.ofInstant(Instant.ofEpochMilli(endTime), ZoneId.systemDefault()));
        Map map = new HashMap();
        map.put("startDate", startTimeStr);
        map.put("endDate", endTimeStr);
        return map;
    }

    //获取今天日期 年月日
    public  static  Date getTodayYmd(){
        Date now = new Date();
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(now);
        // 将时分秒,毫秒域清零
        cal1.set(Calendar.HOUR_OF_DAY, 0);
        cal1.set(Calendar.MINUTE, 0);
        cal1.set(Calendar.SECOND, 0);
        cal1.set(Calendar.MILLISECOND, 0);
        return cal1.getTime();
    }

    //获取昨天日期 年月日
    public static Date getYesterdayYmd() {
        Calendar cal = new GregorianCalendar();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        cal.add(Calendar.DAY_OF_MONTH, -1);
        return cal.getTime();
    }




    /**
     * 获取昨天开始时间和结束时间
     *
     * @return
     */
    public static Map getYesterdayTime() {
        Long startTime = getBeginDayOfYesterday();
        Long endTime = getEndDayOfYesterDay();
        DateTimeFormatter ftf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String startTimeStr = ftf.format(LocalDateTime.ofInstant(Instant.ofEpochMilli(startTime), ZoneId.systemDefault()));
        String endTimeStr = ftf.format(LocalDateTime.ofInstant(Instant.ofEpochMilli(endTime), ZoneId.systemDefault()));
        Map map = new HashMap();
        map.put("startDate", startTimeStr);
        map.put("endDate", endTimeStr);
        return map;




    }

    /**
     * 获取当月开始时间和结束时间
     *
     * @return
     */
    public static Map getMonthTime() {
        Long startTime = getMonthStartTime();
        Long endTime = getMonthEndTime();
        DateTimeFormatter ftf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String startTimeStr = ftf.format(LocalDateTime.ofInstant(Instant.ofEpochMilli(startTime), ZoneId.systemDefault()));
        String endTimeStr = ftf.format(LocalDateTime.ofInstant(Instant.ofEpochMilli(endTime), ZoneId.systemDefault()));
        Map map = new HashMap();
        map.put("startDate", startTimeStr);
        map.put("endDate", endTimeStr);
        return map;
    }

    /**
     * 获取上个月月开始时间和结束时间
     *
     * @return
     */
    public static Map<String,String> getLastMonthTime() throws Exception {
        Long startTime = getLastMonthStartTime();
        Long endTime = getLastMonthEndTime();
        DateTimeFormatter ftf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String startTimeStr = ftf.format(LocalDateTime.ofInstant(Instant.ofEpochMilli(startTime), ZoneId.systemDefault()));
        String endTimeStr = ftf.format(LocalDateTime.ofInstant(Instant.ofEpochMilli(endTime), ZoneId.systemDefault()));
        Map map = new HashMap();
        map.put("startDate", startTimeStr);
        map.put("endDate", endTimeStr);
        return map;
    }


    //获取月均开始和结束时间
    public static Map getAverageTime(String minCreateTime) throws ParseException {


        Long yesterdayTimeEnd =getEndDayOfYesterDay();

        DateTimeFormatter ftf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        //昨天的时间
        String yesterdayStr = ftf.format(LocalDateTime.ofInstant(Instant.ofEpochMilli(yesterdayTimeEnd), ZoneId.systemDefault()));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String yesterdayBefore30Str = "";
        Calendar calc = Calendar.getInstance();

        calc.setTime(sdf.parse(yesterdayStr));
        calc.add(Calendar.DATE, -30);
        calc.set(Calendar.HOUR_OF_DAY, 0);
        calc.set(Calendar.MINUTE, 0);
        calc.set(Calendar.SECOND, 0);
        calc.set(Calendar.MILLISECOND, 0);
        Date minDate = calc.getTime();
        yesterdayBefore30Str = sdf.format(minDate);

        int num = daysBetween(yesterdayBefore30Str, yesterdayStr);

        int checkNum = daysBetween(minCreateTime, yesterdayBefore30Str);
        System.out.println("checkNum:"+checkNum);
        Map map = new HashMap();
        if (checkNum < 0) {
            int numNew = daysBetween(minCreateTime, yesterdayStr);
            map.put("startDate", minCreateTime);
            map.put("endDate", yesterdayStr);
            map.put("num", Math.abs(numNew)); //取绝对值
            return map;
        }

        map.put("startDate", yesterdayBefore30Str);
        map.put("endDate", yesterdayStr);
        map.put("num", num);
        return map;
    }




    public static int daysBetween(String smdate, String bdate) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        cal.setTime(sdf.parse(smdate));
        long time1 = cal.getTimeInMillis();
        cal.setTime(sdf.parse(bdate));
        long time2 = cal.getTimeInMillis();
        long between_days = (time2 - time1) / (1000 * 3600 * 24);
        return Integer.parseInt(String.valueOf(between_days));

    }


    /**
     * 获取今天开始时间
     */
    private static Long getStartTime() {
        Calendar todayStart = Calendar.getInstance();
        todayStart.set(Calendar.HOUR_OF_DAY, 0);
        todayStart.set(Calendar.MINUTE, 0);
        todayStart.set(Calendar.SECOND, 0);
        todayStart.set(Calendar.MILLISECOND, 0);
        return todayStart.getTimeInMillis();
    }

    /**
     * 获取今天结束时间
     */
    private static Long getEndTime() {
        Calendar todayEnd = Calendar.getInstance();
        todayEnd.set(Calendar.HOUR, 23);
        todayEnd.set(Calendar.MINUTE, 59);
        todayEnd.set(Calendar.SECOND, 59);
        todayEnd.set(Calendar.MILLISECOND, 999);
        return todayEnd.getTime().getTime();
    }


    /**
     * 获取当月开始时间
     */
    public static Long getMonthStartTime() {

        Long currentTime = System.currentTimeMillis();

        String timeZone = "GMT+8:00";
        Calendar calendar = Calendar.getInstance();// 获取当前日期
        calendar.setTimeZone(TimeZone.getTimeZone(timeZone));
        calendar.setTimeInMillis(currentTime);
        calendar.add(Calendar.YEAR, 0);
        calendar.add(Calendar.MONTH, 0);
        calendar.set(Calendar.DAY_OF_MONTH, 1);// 设置为1号,当前日期既为本月第一天
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTimeInMillis();


    }

    /**
     * 获取当月的结束时间戳
     */
    public static Long getMonthEndTime() {
        Long currentTime = System.currentTimeMillis();

        String timeZone = "GMT+8:00";
        Calendar calendar = Calendar.getInstance();// 获取当前日期
        calendar.setTimeZone(TimeZone.getTimeZone(timeZone));
        calendar.setTimeInMillis(currentTime);
        calendar.add(Calendar.YEAR, 0);
        calendar.add(Calendar.MONTH, 0);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));// 获取当前月最后一天
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return calendar.getTimeInMillis();
    }


    public static Long getLastMonthStartTime() throws Exception {
        Long currentTime = System.currentTimeMillis();

        String timeZone = "GMT+8:00";
        Calendar calendar = Calendar.getInstance();// 获取当前日期
        calendar.setTimeZone(TimeZone.getTimeZone(timeZone));
        calendar.setTimeInMillis(currentTime);
        calendar.add(Calendar.YEAR, 0);
        calendar.add(Calendar.MONTH, -1);
        calendar.set(Calendar.DAY_OF_MONTH, 1);// 设置为1号,当前日期既为本月第一天
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        return calendar.getTimeInMillis();
    }


    public static Long getLastMonthEndTime() {
        Long currentTime = System.currentTimeMillis();

        String timeZone = "GMT+8:00";
        Calendar calendar = Calendar.getInstance();// 获取当前日期
        calendar.setTimeZone(TimeZone.getTimeZone(timeZone));
        calendar.setTimeInMillis(currentTime);
        calendar.add(Calendar.YEAR, 0);
        calendar.add(Calendar.MONTH, -1);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));// 获取当前月最后一天
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);

        return calendar.getTimeInMillis();
    }





    public static Long getBeginDayOfYesterday() {
        Calendar cal = new GregorianCalendar();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        cal.add(Calendar.DAY_OF_MONTH, -1);
        return cal.getTimeInMillis();
    }




    public static Long getEndDayOfYesterDay() {

        Calendar cal = new GregorianCalendar();
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.add(Calendar.DAY_OF_MONTH, -1);


        return cal.getTimeInMillis();
    }








    //获取今年是哪一年

    public static Integer getNowYear() {

        Date date = new Date();

        GregorianCalendar gc = (GregorianCalendar) Calendar.getInstance();

        gc.setTime(date);

        return Integer.valueOf(gc.get(1));

    }


    public static Map getThisYearTime() throws Exception {
        Long startTime = getBeginDayOfYear();
        Long endTime = getEndDayOfYear();
        DateTimeFormatter ftf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String startTimeStr = ftf.format(LocalDateTime.ofInstant(Instant.ofEpochMilli(startTime), ZoneId.systemDefault()));
        String endTimeStr = ftf.format(LocalDateTime.ofInstant(Instant.ofEpochMilli(endTime), ZoneId.systemDefault()));
        Map map = new HashMap();
        map.put("startDate", startTimeStr);
        map.put("endDate", endTimeStr);
        return map;
    }

    //获取本年的开始时间

    public static Long getBeginDayOfYear() {

        Calendar cal = Calendar.getInstance();

        cal.set(Calendar.YEAR, getNowYear());

        // cal.set

        cal.set(Calendar.MONTH, Calendar.JANUARY);

        cal.set(Calendar.DATE, 1);



        return getDayStartTime(cal.getTime());

    }

    //获取本年的结束时间

    public static Long getEndDayOfYear() {

        Calendar cal = Calendar.getInstance();

        cal.set(Calendar.YEAR, getNowYear());

        cal.set(Calendar.MONTH, Calendar.DECEMBER);

        cal.set(Calendar.DATE, 31);

        return getDayEndTime(cal.getTime());

    }

    //获取某个日期的开始时间

    public static Long getDayStartTime(Date d) {

        Calendar calendar = Calendar.getInstance();

        if(null != d) calendar.setTime(d);

        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),    calendar.get(Calendar.DAY_OF_MONTH), 0, 0, 0);

        calendar.set(Calendar.MILLISECOND, 0);

        return calendar.getTimeInMillis();

    }

    //获取某个日期的结束时间

    public static Long getDayEndTime(Date d) {

        Calendar calendar = Calendar.getInstance();

        if(null != d) calendar.setTime(d);

        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),    calendar.get(Calendar.DAY_OF_MONTH), 23, 59, 59);

        calendar.set(Calendar.MILLISECOND, 999);

        return calendar.getTimeInMillis();

    }
}

