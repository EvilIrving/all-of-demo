package com.ygkj.utils;

import java.io.Serializable;
import java.lang.reflect.Field;

/**
 * @author xq
 * @Description
 * @Date 2021/5/24
 */
public class BeanComparator<T> extends AbstractComparator<T> implements Serializable {

    public BeanComparator(String property) {
        this.property = property;
    }

    @Override
    public Object getComparableValue(Object bean, String property) throws Exception {
        Field field = null;
        try {
            field = bean.getClass().getDeclaredField(property);
        } catch (NoSuchFieldException ignore) {
            // 循环去找父类的属性，直到找到或父类为Object为止
            boolean loop = true;
            Class currentClass = bean.getClass();
            do {
                try {
                    currentClass = currentClass.getSuperclass();
                    field = currentClass.getDeclaredField(property);
                    loop = false;
                } catch (NoSuchFieldException ignore2) {

                }
            } while (loop && currentClass != Object.class);
        }
        if (field != null) {
            field.setAccessible(true);
            return field.get(bean);
        }
        return null;
    }
}
