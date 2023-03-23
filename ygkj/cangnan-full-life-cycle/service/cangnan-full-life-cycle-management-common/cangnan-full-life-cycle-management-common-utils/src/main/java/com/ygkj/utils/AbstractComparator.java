package com.ygkj.utils;

import java.io.Serializable;
import java.util.Comparator;

/**
 * @author xq
 * @Description
 * @Date 2021/5/25
 */
public abstract class AbstractComparator<T> implements Comparator<T>, Serializable {
    String property;

    public void setProperty(String property) {
        this.property = property;
    }

    public String getProperty() {
        return this.property;
    }


    @Override
    public int compare(T o1, T o2) {
        return internalCompare(o1, o2);
    }

    private int internalCompare(T o1, T o2) {
        if (this.property == null) {
            return this.doCompare(o1, o2);
        } else {
            try {
                Object value1 = getComparableValue(o1, this.property);
                Object value2 = getComparableValue(o2, this.property);
                return this.doCompare(value1, value2);
            } catch (Exception e) {
                return 0;
            }
        }
    }

    private int doCompare(Object o1, Object o2) {
        if (o1 != null & o2 != null) {
            if (o1 instanceof Comparable && o2 instanceof Comparable) {
                return (((Comparable) o1).compareTo(o2));
            } else if (o1 instanceof Number && o2 instanceof Number) {
                return Double.compare(((Number) o1).doubleValue(), ((Number) o2).doubleValue());
            }
        } else if (o1 == null & o2 != null) {
            return -1;
        } else if (o1 != null & o2 == null) {
            return 1;
        }
        return 0;

    }

    public abstract Object getComparableValue(Object bean, String property) throws Exception;
}
