package com.ygkj.water.project.utils;

import com.google.common.collect.Maps;
import com.ygkj.jwt.JwtTokenUtil;
import com.ygkj.utils.StringUtils;
import com.ygkj.water.project.mapper.UserMapper;

import java.util.Map;
import java.util.Random;

public class CommonUtil {

    /**
     * 获取随机字符
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
     * 判断当前用户是否是建管中心的人员
     *
     * @return true:是   false:否
     */
    public static boolean isConstructionManagementPerson(UserMapper userMapper) {
        String userId = JwtTokenUtil.getCurrentUserId().orElse("");
        if (StringUtils.isEmpty(userId)) {
            return false;
        }
        Integer num = userMapper.constructionManagementPerson(userId);
        if (num != null && num > 0) {
            return true;
        }
        return false;
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
        Map<K, V> result = Maps.newLinkedHashMap();
        if (isDesc) {
            map.entrySet().stream().sorted(Map.Entry.<K, V>comparingByKey().reversed())
                    .forEachOrdered(e -> result.put(e.getKey(), e.getValue()));
        } else {
            map.entrySet().stream().sorted(Map.Entry.<K, V>comparingByKey())
                    .forEachOrdered(e -> result.put(e.getKey(), e.getValue()));
        }
        return result;
    }

}
