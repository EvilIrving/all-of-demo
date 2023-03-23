package com.ygkj.utils;

import java.util.*;

/**
 * @author xq
 * @Description 集合工具
 * @Date 2021/1/18
 */
public class CollectionUtils {

    /**
     * 判断集合是否为空
     * @param collection 集合
     * @return 空为true，否则为false
     */
    public static boolean isBlank(Collection collection){
        if (collection==null){
            return true;
        }else{
            return collection.isEmpty();
        }
    }

    public static boolean isNotBlank(Collection collection){
        return !isBlank(collection);
    }

    /**
     * 判断Map是否为空
     * @param map map
     * @return 空为true，否则为false
     */
    public static boolean isBlank(Map map){
        if (map==null){
            return true;
        }else{
            return map.isEmpty();
        }
    }

    /**
     * 判断map是否不为空
     * @param map
     * @return 为空
     */
    public static boolean isNotBlank(Map map){
        return !isBlank(map);
    }

    /**
     * 求两个集合的差集 a-b 并返回一个新的集合
     * @param a
     * @param b
     * @return a-b
     */
    public static <T> List<T> getExcept(List<T> a,List<T> b){
        if (isNotBlank(a)&&isNotBlank(b)){
            // 创建a的副本，否则remove 后会影响原来的集合
            List<T> aCopy=new ArrayList<>(a);
            List<T> common=new ArrayList<>(a);
            common.retainAll(b);
            aCopy.removeAll(common);
            return aCopy;
        }else if (isNotBlank(a)&&isBlank(b)){
           return a;
        }else{
            return Collections.EMPTY_LIST;
        }
    }

    /**
     * 移除集合中的空对象
     * @param collection
     * @return
     */
    public static Collection removeNull(Collection collection){
        if (collection!=null) {
            collection.removeAll(Collections.singleton(null));
        }
        return collection;
    }

    /**
     * 将数组转换成可以修改的有序list
     * @param ts
     * @param <T>
     * @return
     */
    public static <T> List<T> asList(T... ts){
        return new ArrayList<>(Arrays.asList(ts));
    }
}
