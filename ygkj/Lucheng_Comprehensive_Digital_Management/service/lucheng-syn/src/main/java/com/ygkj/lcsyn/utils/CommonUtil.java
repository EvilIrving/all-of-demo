package com.ygkj.lcsyn.utils;



import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;

import java.text.SimpleDateFormat;
import java.util.*;

public class CommonUtil {

    /**
     * 获取随机字符
     *
     * @param length 长度
     * @return
     */
    public static String getRandomString(int length) {
        String str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();

        for (int i = 0; i < length; ++i) {
            //int number = random.nextInt(62);// [0,62)
            sb.append(str.charAt(random.nextInt(62)));
        }
        return sb.toString();
    }

    /**
     * 根据map的key排序
     *
     * @param map    待排序的map
     * @param isDesc 是否降序，true：降序，false：升序
     * @return 排序好的map
     * @author hucong
     */
    public static <K extends Comparable<? super K>, V> Map<K, V> sortByKey(Map<K, V> map, boolean isDesc) {
        Map<K, V> result = new LinkedHashMap<>();
        if (isDesc) {
            map.entrySet().stream().sorted(Map.Entry.<K, V>comparingByKey().reversed())
                    .forEachOrdered(e -> result.put(e.getKey(), e.getValue()));
        } else {
            map.entrySet().stream().sorted(Map.Entry.<K, V>comparingByKey())
                    .forEachOrdered(e -> result.put(e.getKey(), e.getValue()));
        }
        return result;
    }

    /**
     * 根据当前时间获取表名
     * @param type
     * @return
     */
    public static String getTableName(String type,Date date){
        if (ObjectUtils.isEmpty(date)){
            date = new Date();
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
        switch (type){
            case "PP":
                return "st_pptn_r_" + sdf.format(date);
            case "RSVR":
                return "st_rsvr_r_" + sdf.format(date);
            default:
                return null;
        }
    }

}
