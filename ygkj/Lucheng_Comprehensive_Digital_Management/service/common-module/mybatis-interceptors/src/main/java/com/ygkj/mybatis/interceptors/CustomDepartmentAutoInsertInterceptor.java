package com.ygkj.mybatis.interceptors;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.binding.MapperMethod;
import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.springframework.beans.BeanUtils;
import org.springframework.util.ClassUtils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Intercepts({
        @Signature(type = ParameterHandler.class, method = "setParameters",
                args = {PreparedStatement.class})
})
@Slf4j
public class CustomDepartmentAutoInsertInterceptor implements Interceptor {
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        if (invocation.getTarget() instanceof ParameterHandler) {
            return invokeSetParameter(invocation);
        }
        return null;
    }

    //    @Override
//    public Object intercept(Invocation invocation) throws Throwable {
//        StatementHandler statementHandler = (StatementHandler) invocation.getTarget();
//        MetaObject MetaObjectHandler = SystemMetaObject.forObject(statementHandler);
//
//        //分离代理对象链
//        while (MetaObjectHandler.hasGetter("h")) {
//            Object obj = MetaObjectHandler.getValue("h");
//            MetaObjectHandler = SystemMetaObject.forObject(obj);
//        }
//
//        while (MetaObjectHandler.hasGetter("target")) {
//            Object obj = MetaObjectHandler.getValue("target");
//            MetaObjectHandler = SystemMetaObject.forObject(obj);
//        }
//        MappedStatement mappedStatement = (MappedStatement) MetaObjectHandler.getValue("delegate.mappedStatement");
//        //直接获取方法的全限定名
//        String method = mappedStatement.getId();
//        String className = method.substring(0, method.lastIndexOf("."));
//        method = method.substring(method.lastIndexOf(".")+1);
//        Class<?> clazz = Class.forName(className);
//        Method[] declaredMethods = clazz.getDeclaredMethods();
//        boolean needInsertDept = false;
//        for (Method declaredMethod : declaredMethods) {
//            if (declaredMethod.getName().equals(method) && declaredMethod.isAnnotationPresent(NeedInsertDepartment.class)){
//                needInsertDept = true;
//            }
//        }
//        if (needInsertDept){
//            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
//            String token = request.getHeader("authorization");
//            String deptId = "";
//            if (StringUtils.isNotBlank(token)){
//                deptId = JwtTokenUtil.getDeptId(token);
//            }
//            if (StringUtils.isNotBlank(deptId)){
//                //内置对象，将sql包装进去
//                BoundSql boundSql = statementHandler.getBoundSql();
//                String sql = boundSql.getSql();
//                List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
//                if (!boundSql.hasAdditionalParameter("deptId")){
//                    boundSql.setAdditionalParameter("deptId",deptId);
//                    MetaObjectHandler.setValue("delegate.boundSql",boundSql);
//                }
//                if (!sql.contains("dept_id")){
//                    String[] split = sql.split("\\)");
//                    split[0] = split[0].concat(",dept_id)");
//                    split[1] = split[1].concat(",?)");
//                    sql = split[0].concat(split[1]);
//                    Field field = boundSql.getClass().getDeclaredField("sql");
//                    field.setAccessible(true);
//                    field.set(boundSql, sql);
//                }
//            }
//        }
//        return invocation.proceed();
//    }

    private Object invokeSetParameter(Invocation invocation) throws Exception {

        ParameterHandler parameterHandler = (ParameterHandler) invocation.getTarget();
        PreparedStatement ps = (PreparedStatement) invocation.getArgs()[0];
        MetaObject MetaObjectHandler = SystemMetaObject.forObject(parameterHandler);
        //分离代理对象链
        while (MetaObjectHandler.hasGetter("h")) {
            Object obj = MetaObjectHandler.getValue("h");
            MetaObjectHandler = SystemMetaObject.forObject(obj);
        }

        while (MetaObjectHandler.hasGetter("target")) {
            Object obj = MetaObjectHandler.getValue("target");
            MetaObjectHandler = SystemMetaObject.forObject(obj);
        }
        BoundSql boundSql = (BoundSql) MetaObjectHandler.getValue("boundSql");
        // 反射获取 BoundSql 对象，此对象包含生成的sql和sql的参数map映射
//        Field boundSqlField = parameterHandler.getClass().getDeclaredField("boundSql");
//        boundSqlField.setAccessible(true);
//        BoundSql boundSql = (BoundSql) boundSqlField.get(parameterHandler);

        List<String> paramNames = new ArrayList<>();

        // 反射获取 参数对像
//        Field parameterField = parameterHandler.getClass().getDeclaredField("parameterObject");
//        parameterField.setAccessible(true);
        Object parameterObject = MetaObjectHandler.getValue("parameterObject");

        // 改写参数
        parameterObject = processSingle(parameterObject, paramNames);

        // 改写的参数设置到原parameterHandler对象
        MetaObjectHandler.setValue("parameterObject", parameterObject);
//        parameterField.set(parameterHandler, parameterObject);
        parameterHandler.setParameters(ps);
        return invocation.proceed();
    }

    private Object processSingle(Object paramObj, List<String> paramNames) throws Exception {

        Map<String, Object> paramMap = new MapperMethod.ParamMap<>();
        if (paramObj == null) {
            paramMap.put("deptId", 1L);
            paramObj = paramMap;
            // 单参数 将 参数转为 map
        } else if (ClassUtils.isPrimitiveOrWrapper(paramObj.getClass())
                || String.class.isAssignableFrom(paramObj.getClass())
                || Number.class.isAssignableFrom(paramObj.getClass())) {
            if (paramNames.size() == 1) {
                paramMap.put(paramNames.iterator().next(), paramObj);
                paramMap.put("deptId", 1L);
                paramObj = paramMap;
            }
        } else {
            processParam(paramObj);
        }

        return paramObj;
    }

    private void processParam(Object parameterObject) throws IllegalAccessException, InvocationTargetException {
        // 处理参数对象  如果是 map 且map的key 中没有 tenantId，添加到参数map中
        // 如果参数是bean，反射设置值
        if (parameterObject instanceof Map) {
            ((Map) parameterObject).putIfAbsent("deptId", 1L);
        } else {
            PropertyDescriptor ps = BeanUtils.getPropertyDescriptor(parameterObject.getClass(), "deptId");
            if (ps != null && ps.getReadMethod() != null && ps.getWriteMethod() != null) {
                Object value = ps.getReadMethod().invoke(parameterObject);
                if (value == null) {
                    ps.getWriteMethod().invoke(parameterObject, value);
                }
            }
        }
    }

    private static class ReflectUtil {
        /**
         * 利用反射获取指定对象的指定属性
         *
         * @param obj       目标对象
         * @param fieldName 目标属性
         * @return 目标属性的值
         */
        public static Object getFieldValue(Object obj, String fieldName) {
            Object result = null;
            Field field = ReflectUtil.getField(obj, fieldName);
            if (field != null) {
                field.setAccessible(true);
                try {
                    result = field.get(obj);
                } catch (IllegalArgumentException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            return result;
        }

        /**
         * 利用反射获取指定对象里面的指定属性
         *
         * @param obj       目标对象
         * @param fieldName 目标属性
         * @return 目标字段
         */
        private static Field getField(Object obj, String fieldName) {
            Field field = null;
            for (Class<?> clazz = obj.getClass(); clazz != Object.class; clazz = clazz.getSuperclass()) {
                try {
                    field = clazz.getDeclaredField(fieldName);
                    break;
                } catch (NoSuchFieldException e) {
                    //这里不用做处理，子类没有该字段可能对应的父类有，都没有就返回null。
                }
            }
            return field;
        }

        /**
         * 利用反射设置指定对象的指定属性为指定的值
         *
         * @param obj        目标对象
         * @param fieldName  目标属性
         * @param fieldValue 目标值
         */
        public static void setFieldValue(Object obj, String fieldName,
                                         String fieldValue) {
            Field field = ReflectUtil.getField(obj, fieldName);
            if (field != null) {
                try {
                    field.setAccessible(true);
                    field.set(obj, fieldValue);
                } catch (IllegalArgumentException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public Object plugin(Object target) {
        if (target instanceof ParameterHandler) {
            return Plugin.wrap(target, this);
        }
        return target;
    }
}
