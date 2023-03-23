package com.ygkj.utils;


import com.ygkj.entity.StartEndTimeVo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author xq
 * @Description 时间日期处理工具
 * @Date 2021/1/16
 */
public class DateUtils {

    public final static ZoneId CN_ZONE = ZoneId.of("Asia/Shanghai");

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

    public static Date localDateTime2Date(LocalDateTime localDateTime) {
        ZonedDateTime zdt = localDateTime.atZone(CN_ZONE);
        return Date.from(zdt.toInstant());
    }

    public static Date localDate2Date(LocalDate localDate) {
        ZonedDateTime zdt = localDate.atStartOfDay(CN_ZONE);
        return Date.from(zdt.toInstant());
    }

    public static LocalDateTime date2LocalDateTime(Date date) {
        Instant instant = date.toInstant();
        return instant.atZone(CN_ZONE).toLocalDateTime();
    }

    public static LocalDate date2LocalDate(Date date) {
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

    /**
     * 获取两个时间之间的开始结束实体
     *
     * @param startTime
     * @param endTime
     * @return
     */
    public static List<StartEndTimeVo> getStartEndTimeVoList(String startTime, String endTime, int minute) {
        List<StartEndTimeVo> list = new ArrayList<>();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long addMills = minute * 60 * 1000;
        try {
            long startMillis = df.parse(startTime).getTime();
            long endMillis = df.parse(endTime).getTime();
            while (startMillis < endMillis) {
                StartEndTimeVo vo = new StartEndTimeVo();
                vo.setStartTime(df.format(new Date(startMillis)));
                vo.setEndTime(df.format(new Date(startMillis + addMills)));
                vo.setTag(0);
                startMillis += addMills;
                list.add(vo);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }


    public static String addDay(int add) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, add);
        //Date d = cal.getTime();//这个就是7天前的Date了
        return new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());
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

    public static String getWeekday(LocalDate date) {
        switch (date.getDayOfWeek().getValue()) {
            case 1:
                return "周一";
            case 2:
                return "周二";
            case 3:
                return "周三";
            case 4:
                return "周四";
            case 5:
                return "周五";
            case 6:
                return "周六";
            case 7:
                return "周日";
            default:
                return "";
        }
    }

    public static Integer hourBetween(String begin, String end) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:00:00");
        try {
            Date beginTime = format.parse(begin);
            Date endTime = format.parse(end);
            if (!beginTime.equals(endTime) && beginTime.after(endTime)) {
                throw new IllegalArgumentException();
            }
            return hourBetween(beginTime, endTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public static Integer hourBetween(Date begin, Date end) {
        long mills = end.getTime() - begin.getTime();
        return (int) (mills / (Math.pow(60, 2) * TimeUnit.MILLISECONDS.convert(1l, TimeUnit.SECONDS)));
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

    /**
     * 根据开始时间、结束时间获取表名
     */
    public static String[] getTableNamesByTimeInterval(String tablePrefix, String startTime, String endTime) {

        try {
            List<String> dateListStr = getDateListStr(startTime, endTime);
            Map<String, String> map = new HashMap<>();
            for (String time : dateListStr) {
                String ym = time.substring(0, 7).replace("-", "");
                map.put(tablePrefix + ym, tablePrefix + ym);
            }
            ArrayList<String> list = new ArrayList<>(map.keySet());
            String[] arr = new String[list.size()];
            list.toArray(arr);//将转化后的数组放入已经创建好的对象中
            return arr;
        } catch (ParseException e) {
            return null;
        }
    }

    public static Date parseDate(String strFormat, String dateValue) {
        if (dateValue == null) {
            return null;
        } else {
            if (strFormat == null) {
                strFormat = "yyyy-MM-dd HH:mm:ss";
            }

            SimpleDateFormat dateFormat = new SimpleDateFormat(strFormat);
            Date newDate = null;

            try {
                newDate = dateFormat.parse(dateValue);
            } catch (ParseException var5) {
                newDate = null;
            }

            return newDate;
        }
    }

    public static int getYear(Date date) {
        if (date == null) {
            return 0;
        } else {
            Calendar calendar = getCalendar();
            calendar.setTime(date);
            return calendar.get(1);
        }
    }

    public static int getMonth(Date date) {
        if (date == null) {
            return 0;
        } else {
            Calendar calendar = getCalendar();
            calendar.setTime(date);
            return calendar.get(2) + 1;
        }
    }

    public static Calendar getCalendar() {
        return Calendar.getInstance();
    }

    private static final SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static final SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
    private static final SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    private static final SimpleDateFormat sdf4 = new SimpleDateFormat("yyyy/MM/dd");
    private static final SimpleDateFormat sdf5 = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    public static Date parse(String str) {
        Date date = null;
        Pattern p1 = Pattern.compile("^\\d{2,4}\\-\\d{1,2}\\-\\d{1,2} \\d{1,2}:\\d{1,2}:\\d{1,2}$");
        Matcher m1 = p1.matcher(str);
        Pattern p2 = Pattern.compile("^\\d{2,4}\\-\\d{1,2}\\-\\d{1,2}$");
        Matcher m2 = p2.matcher(str);
        Pattern p3 = Pattern.compile("^\\d{2,4}\\/\\d{1,2}\\/\\d{1,2} \\d{1,2}:\\d{1,2}:\\d{1,2}$");
        Matcher m3 = p3.matcher(str);
        Pattern p4 = Pattern.compile("^\\d{2,4}\\/\\d{1,2}\\/\\d{1,2}$");
        Matcher m4 = p4.matcher(str);
        Pattern p5 = Pattern.compile("^\\d{2,4}\\-\\d{1,2}\\-\\d{1,2} \\d{1,2}:\\d{1,2}$");
        Matcher m5 = p5.matcher(str);

        try {
            if (m1.matches()) {
                date = sdf1.parse(str);
            } else if (m2.matches()) {
                date = sdf2.parse(str);
            } else if (m3.matches()) {
                date = sdf3.parse(str);
            } else if (m4.matches()) {
                date = sdf4.parse(str);
            } else if (m5.matches()) {
                date = sdf5.parse(str);
            }

            return date;
        } catch (ParseException var13) {
            return null;
        }
    }

    public static String format(Date date, String pattern) {
        if (date == null) {
            return "";
        } else {
            SimpleDateFormat sdf = new SimpleDateFormat(pattern);
            return sdf.format(date);
        }
    }

    public static Duration getDuration(Date start, Date end) {
        if (start == null || end == null) {
            return null;
        }
        return Duration.between(date2LocalDateTime(start), date2LocalDateTime(end));
    }

    public static Period getPeriod(Date start, Date end) {
        if (start == null || end == null) {
            return null;
        }
        return Period.between(date2LocalDate(start), date2LocalDate(end));
    }
}
