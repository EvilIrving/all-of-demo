package com.ygkj.lcsyn.utils;

import com.ygkj.lcsyn.model.BhDepJobsettinginfo;
import com.ygkj.lcsyn.model.StTideR;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class SqlUtil {


    private static final String comma = ",";
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static <T> List<String> batchInsertSql(List<T> list){
        return batchInsertSql(list,600);
    }

    public static <T> List<String> batchInsertSql(List<T> list,int pageSize) {
        if(list == null || list.size() == 0){
            return null;
        }
        List<String> result = new ArrayList<>();
        //首先拼装表字段
        Class<T> clazz = (Class<T>) list.get(0).getClass();
        StringBuilder basicSb = new StringBuilder("INSERT INTO ");
        basicSb.append(tableName(clazz));
        basicSb.append(" (");
        Field[] declaredFields = clazz.getDeclaredFields();
        for (Field field : declaredFields) {
            field.setAccessible(true);
            basicSb.append(humpToLine(field.getName())).append(comma);
        }
        basicSb.deleteCharAt(basicSb.length() - 1);
        basicSb.append(") VALUE ");
        //然后拼接值
        int total = list.size();
        //每次只批量插入600条
        int fromIndex = 0;
        int toIndex;
        do {
            StringBuilder sb = new StringBuilder(basicSb);
            toIndex = fromIndex + pageSize;
            List<T> subList = list.subList(fromIndex, toIndex > total ? total : toIndex);
            for (T t : subList) {
                sb.append("(");
                for (Field field : declaredFields) {
                    try {
                        Object o = field.get(t);
                        if (o == null) {
                            sb.append("null,");
                        } else {
                            Class fieldClass = field.getType();
                            sb.append("'");
                            if (Date.class.isAssignableFrom(fieldClass)) {
                                sb.append(sdf.format((Date) o));
                            } else if(String.class.isAssignableFrom(fieldClass)){
                                sb.append(((String) o).replace("'","\\'"));
                            }else {
                                sb.append(o);
                            }
                            sb.append("',");
                        }
                    } catch (IllegalAccessException e) {
                        sb.append("null,");
                        e.printStackTrace();
                    }
                }
                sb.deleteCharAt(sb.length() - 1);
                sb.append("),");
            }
            sb.deleteCharAt(sb.length() - 1);
            fromIndex = toIndex;
            log.info("[生成批量插入语句]{}",sb);
            result.add(sb.toString());
        } while (toIndex < total);
        return result;
    }

    private static Pattern humpPattern = Pattern.compile("[A-Z]");

    /**
     * 驼峰转下划线
     *
     * @param str
     * @return
     */
    public static String humpToLine(String str) {
        Matcher matcher = humpPattern.matcher(str);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(sb, "_" + matcher.group(0).toLowerCase());
        }
        matcher.appendTail(sb);
        //如果第一个是_删除第一个
        if(sb.indexOf("_") == 0){
            sb.deleteCharAt(0);
        }
        return sb.toString();
    }

    public static <T> String delSql(Class<T> clazz) {
        StringBuilder sb = new StringBuilder("DELETE FROM ");
        sb.append(humpToLine(clazz.getSimpleName()));
        return sb.toString();
    }

    public static String tableName(Class clazz){
        return humpToLine(clazz.getSimpleName());
    }

    public static void main(String[] args) {
        System.out.println(tableName(BhDepJobsettinginfo.class));
    }
}
