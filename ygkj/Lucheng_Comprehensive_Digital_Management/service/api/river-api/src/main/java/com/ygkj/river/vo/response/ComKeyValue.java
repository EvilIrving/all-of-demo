package com.ygkj.river.vo.response;

import lombok.Data;

/**
 * @author xq
 * @Description
 * @Date 2021/4/7
 */
@Data
public class ComKeyValue<K, V> {
    K key;
    V value;
}
