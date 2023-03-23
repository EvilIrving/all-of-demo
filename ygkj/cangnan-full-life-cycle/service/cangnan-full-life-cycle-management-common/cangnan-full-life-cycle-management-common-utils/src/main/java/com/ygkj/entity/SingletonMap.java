package com.ygkj.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author xq
 * @Description 单个键值对对象
 * @Date 2021/8/6
 */
@Data
public class SingletonMap<K, V> implements Serializable {

    private static final long serialVersionUID = 8742209644954830869L;

    /**
     * 定义
     */
    K key;
    /**
     * 数值
     */
    V value;

    /**
     * 备注
     */
    String remark;

    public SingletonMap() {
    }

    public SingletonMap(K key, V value) {
        this.key = key;
        this.value = value;
    }

    public static <K, V> List<SingletonMap<K,V>> map2List(Map<K,V> map){
        if (map!=null){
            return map.entrySet().stream().map(o-> new SingletonMap<>(o.getKey(), o.getValue())).collect(Collectors.toList());
        }else{
            return Collections.emptyList();
        }
    }
    public static <K, V> Map<K,V> list2Map(List<SingletonMap<K,V>> list){
        if (list!=null){
            return list.stream().collect(Collectors.toMap(SingletonMap::getKey,SingletonMap::getValue));
        }else{
            return Collections.emptyMap();
        }
    }
}