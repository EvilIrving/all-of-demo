package com.ygkj.project.vo.response;

import lombok.Data;

/**
 * @author xq
 * @Description 通用图表回参
 * @Date 2021/3/21
 */
@Data
public class ChartResVo<K, V> {
    /**
     * 定义
     */
    K key;
    /**
     * 数值
     */
    V value;

    Object remark;


    public ChartResVo() {
    }

    public ChartResVo(K key, V value) {
        this.key = key;
        this.value = value;
    }
}
