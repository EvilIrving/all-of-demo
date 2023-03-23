package com.ygkj.utils;

import java.io.Serializable;
import java.util.Map;

/**
 * @author xq
 * @Description
 * @Date 2021/5/24
 */
public class MapComparator<T extends Map> extends AbstractComparator<T> implements Serializable {

    public MapComparator(String property) {
        this.property = property;
    }

    @Override
    public Object getComparableValue(Object bean, String property) {
        if (bean instanceof Map) {
            return ((Map<?, ?>) bean).get(property);
        }
        return null;
    }
}
