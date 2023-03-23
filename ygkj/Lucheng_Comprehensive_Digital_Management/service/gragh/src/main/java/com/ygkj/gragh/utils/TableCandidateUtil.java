package com.ygkj.gragh.utils;

import com.ygkj.gragh.enums.StationType;
import com.ygkj.utils.DateUtils;
import com.ygkj.utils.StringUtils;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * @author xq
 * @Description
 * @Date 2021/11/8
 */
public class TableCandidateUtil {

    public static final DateTimeFormatter yyyy = DateTimeFormatter.ofPattern("yyyy");
    public static final DateTimeFormatter yyyyMM = DateTimeFormatter.ofPattern("yyyyMM");
    public static final DateTimeFormatter yyyyMMdd = DateTimeFormatter.ofPattern("yyyyMMdd");

    /**
     * 水雨工情分表
     * @param stationType 测站枚举
     * @param start 起始日期
     * @param end 结束日期
     * @return
     */
    public static List<String> candidateRainWaterTable(StationType stationType, LocalDate start, LocalDate end){
        return candidateMonthTable(stationType.table(),start,end,null);
    }

    /**
     * 按月分表
     * @param tablePrefix 分表前缀
     * @param start 起始日期
     * @param end 结束日期
     * @return
     */
    public static List<String> candidateMonthTable(String tablePrefix, LocalDate start,LocalDate end){
        return candidateMonthTable(tablePrefix,start,end,null);
    }

    /**
     * 按月分表
     * @param tablePrefix 分表前缀
     * @param start 起始日期
     * @param end 结束日期
     * @param predicate 用于额外的逻辑
     * @return
     */
    public static List<String> candidateMonthTable(String tablePrefix, LocalDate start, LocalDate end, Predicate<String> predicate){
        MonthIterator iterator=new MonthIterator(start,end,yyyyMM);
        return candidateTable(tablePrefix,iterator,predicate);
    }

    /**
     * 按月分表
     * @param tablePrefix 分表前缀
     * @param date 日期
     * @param predicate 用于额外的逻辑
     * @return
     */
    public static String candidateMonthTable(String tablePrefix, LocalDate date, Predicate<String> predicate){
        return candidateSingleTable(tablePrefix,date,yyyyMM,predicate);
    }

    /**
     * 按月分表
     * @param tablePrefix 分表前缀
     * @param date 日期
     * @param predicate 用于额外的逻辑
     * @return
     */
    public static String candidateMonthTable(String tablePrefix, Date date, Predicate<String> predicate){
        return candidateSingleTable(tablePrefix, DateUtils.date2LocalDate(date),yyyyMM,predicate);
    }

    /**
     * 按月分表
     * @param tablePrefix 分表前缀
     * @param start 起始日期
     * @param end 结束日期
     * @param predicate 用于额外的逻辑
     * @return
     */
    public static List<String> candidateMonthTable(String tablePrefix, Date start, Date end, Predicate<String> predicate){
        return candidateMonthTable(tablePrefix,DateUtils.date2LocalDate(start),DateUtils.date2LocalDate(end),predicate);
    }

    /**
     * 按季度分表
     * @param tablePrefix 分表前缀
     * @param start 起始日期
     * @param end 结束日期
     * @param predicate 用于额外的逻辑
     * @return
     */
    public static List<String> candidateQuarterTable(String tablePrefix, LocalDate start, LocalDate end, Predicate<String> predicate){
        QuarterIterator iterator=new QuarterIterator(start,end);
        return candidateTable(tablePrefix,iterator,predicate);
    }



    /**
     * 按年分表
     * @param tablePrefix 分表前缀
     * @param tm 某个时刻
     * @param predicate 用于额外的逻辑
     * @return
     */
    public static String candidateYearTable(String tablePrefix, LocalDateTime tm, Predicate<String> predicate){
        String table = tablePrefix.concat(tm.format(yyyy));
        if (predicate.test(table)){
            return table;
        }else{
            return null;
        }
    }

    /**
     * 按年分表
     * @param tablePrefix 分表前缀
     * @param tm 某个时刻
     * @param predicate 用于额外的逻辑
     * @return
     */
    public static String candidateYearTable(String tablePrefix, Date tm, Predicate<String> predicate){
        return candidateYearTable(tablePrefix,DateUtils.date2LocalDateTime(tm),predicate);
    }

    /**
     * 按年分表
     * @param tablePrefix 分表前缀
     * @param start 起始日期
     * @param end 结束日期
     * @param predicate 用于额外的逻辑
     * @return
     */
    public static List<String> candidateYearTable(String tablePrefix, LocalDate start, LocalDate end, Predicate<String> predicate){
        YearIterator iterator=new YearIterator(start,end);
        return candidateTable(tablePrefix,iterator,predicate);
    }

    /**
     * 按年分表
     * @param tablePrefix 分表前缀
     * @param start 起始日期
     * @param end 结束日期
     * @param predicate 用于额外的逻辑
     * @return
     */
    public static List<String> candidateYearTable(String tablePrefix, Date start, Date end, Predicate<String> predicate){
        YearIterator iterator=new YearIterator(DateUtils.date2LocalDate(start),DateUtils.date2LocalDate(end));
        return candidateTable(tablePrefix,iterator,predicate);
    }

    /**
     * 按日分表
     * @param tablePrefix 分表前缀
     * @param start 起始日期 包含
     * @param end 结束日期 包含
     * @param predicate 用于额外的逻辑
     * @return
     */
    public static List<String> candidateDayTable(String tablePrefix, LocalDate start, LocalDate end, Predicate<String> predicate){
        DayIterator iterator=new DayIterator(start,end,yyyyMMdd);
        return candidateTable(tablePrefix,iterator,predicate);
    }

    /**
     * 分表
     * @param tablePrefix 分表前缀
     * @param iterator 分表迭代器
     * @param predicate 用于额外的逻辑
     * @return
     */
    public static List<String> candidateTable(String tablePrefix, TableSplitIterator iterator, Predicate<String> predicate){
        List<String> result =new ArrayList<>(iterator.size());
        while(iterator.hasNext()){
            String table = tablePrefix + iterator.next();
            if (predicate==null||predicate.test(table)){
                result.add(table);
            }
        }
        return result.stream().distinct().collect(Collectors.toList());
    }

    /**
     * 分表
     * @param tablePrefix 分表前缀
     * @param date 分表迭代器
     * @param dtf 分表迭代器
     * @param predicate 用于额外的逻辑
     * @return
     */
    public static String candidateSingleTable(String tablePrefix, LocalDate date,DateTimeFormatter dtf, Predicate<String> predicate){
        String table = tablePrefix + date.format(dtf);
        if (predicate.test(table)){
            return table;
        }else{
            return StringUtils.EMPTY;
        }
    }

    public static abstract class TableSplitIterator implements Iterator<String>{
        public abstract int size();
    }


    public static class DayIterator extends TableSplitIterator{

        LocalDate cursor=null;

        LocalDate start;

        LocalDate end;

        DateTimeFormatter yyyyMMdd;

        public DayIterator(LocalDate start, LocalDate end, DateTimeFormatter yyyyMMdd) {
            this.start = start;
            this.cursor = this.start;
            this.end = end;
            this.yyyyMMdd = yyyyMMdd;
        }

        @Override
        public boolean hasNext() {
            return !cursor.isAfter(this.end);
        }

        @Override
        public String next() {
            String result = cursor.format(yyyyMMdd);
            cursor = cursor.plusDays(1);
            return result;
        }

        @Override
        public int size(){
            return Math.abs((int)ChronoUnit.DAYS.between(start,end));
        }
    }


    public static class MonthIterator extends TableSplitIterator{

        LocalDate cursor=null;

        LocalDate start;

        LocalDate end;

        DateTimeFormatter yyyyMM;

        public MonthIterator(LocalDate start, LocalDate end, DateTimeFormatter yyyyMM) {
            this.start = start.withDayOfMonth(1);
            this.cursor = this.start.minusMonths(1);
            this.end = end.withDayOfMonth(1);
            this.yyyyMM = yyyyMM;
        }

        @Override
        public boolean hasNext() {
            return cursor.isBefore(this.end);
        }

        @Override
        public String next() {
            cursor = cursor.plusMonths(1);
            return cursor.format(yyyyMM);
        }

        @Override
        public int size(){
            return Math.abs((int)ChronoUnit.MONTHS.between(start,end));
        }
    }

    public static class QuarterIterator extends TableSplitIterator{

        LocalQuarter cursor=null;

        LocalQuarter start;

        LocalQuarter end;

        public QuarterIterator(LocalDate start, LocalDate end) {
            this.start = new LocalQuarter(start);
            this.cursor = new LocalQuarter(start).minus(1);
            this.end = new LocalQuarter(end);
        }

        @Override
        public boolean hasNext() {
            return cursor.isBefore(this.end);
        }

        @Override
        public String next() {
            cursor = cursor.plus(1);
            return cursor.format();
        }

        @Override
        public int size(){
            return (int)ChronoUnit.MONTHS.between(start.getFirstDayOfQuarter(),end.getFirstDayOfQuarter())/3;
        }

    }



    public static class LocalQuarter {

        public static int QUARTER_MONTH =3;

        public static int YEAR_QUARTER =4;

        LocalDate firstDayOfQuarter;

        public LocalQuarter(LocalDate localDate) {
            int month=localDate.getMonthValue();
            firstDayOfQuarter=localDate.withMonth(quarter2FirstMonth(month2Quarter(month))).withDayOfMonth(1);
        }

        public LocalQuarter minus(int num){
            return new LocalQuarter(firstDayOfQuarter.minusMonths((long) num *QUARTER_MONTH));
        }

        public LocalQuarter plus(int num){
            return new LocalQuarter(firstDayOfQuarter.plusMonths((long) num *QUARTER_MONTH));
        }

        // 数学不好
        public static int month2Quarter(int month){
            switch (month){
                case 1:
                case 2:
                case 3:
                    return 1;
                case 4:
                case 5:
                case 6:
                    return 2;
                case 7:
                case 8:
                case 9:
                    return 3;
                case 10:
                case 11:
                case 12:
                    return 4;
            }
            return -1;
        }

        // 数学不好
        public static int quarter2FirstMonth(int quarter){
            switch (quarter){
                case 1:
                    return 1;
                case 2:
                    return 4;
                case 3:
                    return 7;
                case 4:
                    return 10;
            }
            return -1;
        }

        public LocalDate getFirstDayOfQuarter(){
            return this.firstDayOfQuarter;
        }

        public boolean isAfter(LocalQuarter target){
            return this.firstDayOfQuarter.isAfter(target.getFirstDayOfQuarter());
        }

        public boolean isBefore(LocalQuarter target){
            return this.firstDayOfQuarter.isBefore(target.getFirstDayOfQuarter());
        }

        public String format(){
            return String.valueOf(this.firstDayOfQuarter.getYear()).concat(String.format("%02d",month2Quarter(this.firstDayOfQuarter.getMonthValue())));
        }
    }

    public static class YearIterator extends TableSplitIterator{

        int cursor;

        int start;

        int end;

        public YearIterator(LocalDate start, LocalDate end) {
            this.start = start.getYear();
            this.cursor = this.start-1;
            this.end = end.getYear();
        }

        @Override
        public boolean hasNext() {
            return cursor<end;
        }

        @Override
        public String next() {
            cursor++;
            return String.format("%04d",cursor);
        }

        @Override
        public int size(){
            return Math.abs(end-start)+1;
        }
    }

    public static void main(String[] args) {
        LocalDate now= LocalDate.now();
        System.out.println("按天");
        LocalDate start= now.minusDays(2);
        candidateDayTable("",start,now, null).forEach(System.out::println);
        System.out.println("月份");
        start= now.minusMonths(2);
        candidateMonthTable("",start,now, null).forEach(System.out::println);
        System.out.println("季度");
        start= now.minusYears(1);
        candidateQuarterTable("",start,now, null).forEach(System.out::println);
        System.out.println("年份");
        start= now.minusYears(2);
        candidateYearTable("",start,now, null).forEach(System.out::println);
    }


}
