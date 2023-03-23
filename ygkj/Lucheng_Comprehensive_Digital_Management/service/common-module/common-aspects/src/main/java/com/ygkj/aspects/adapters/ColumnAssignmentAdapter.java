package com.ygkj.aspects.adapters;

import com.ygkj.jwt.JwtTokenUtil;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Order(0)
@Component
public class ColumnAssignmentAdapter {

    private static final String[] DEFAULT_COLUMN = new String[]{"createId,createTime,updateTime,updateId"};


    public Object assignmentSystemColumns(Object arg) throws IllegalAccessException {
        Field[] declaredFields = arg.getClass().getDeclaredFields();
        List<String> columns = Arrays.stream(DEFAULT_COLUMN).collect(Collectors.toList());
        List<Field> list = Arrays.asList(declaredFields).stream()
                .filter(field -> columns.contains(field.getName()))
                .collect(Collectors.toList());
        for (Field field : list) {
            doAssignment(field, arg);
        }
        return arg;
    }

    /**
     * 填充系统公共字段
     *
     * @param field
     * @return
     */
    private void doAssignment(Field field, Object object) throws IllegalAccessException {
        field.setAccessible(true);
        switch (field.getName()) {
            case "createId":
            case "updateId":
                field.set(object, JwtTokenUtil.getCurrentUserId().get());
                break;
            case "createTime":
            case "updateTime":
                field.set(object, new Date());
                break;
            default:
                break;
        }
    }


}
