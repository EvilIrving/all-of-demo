package com.ygkj.mybatis.interceptors;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.ygkj.jwt.JwtTokenUtil;
import com.ygkj.mybatis.annotations.NeedInsertDepartment;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

@Intercepts({
        @Signature(type = Executor.class, method = "update",
                args = {MappedStatement.class, Object.class})
})
@Slf4j
public class DepartmentAutoInsertInterceptor implements Interceptor {

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        Object[] args = invocation.getArgs();
        Object arg = args[0];
        if (arg instanceof MappedStatement) {
            MappedStatement statement = (MappedStatement) arg;
            String method = statement.getId();
            String className = method.substring(0, method.lastIndexOf("."));
            method = method.substring(method.lastIndexOf(".") + 1);
            Class<?> clazz = Class.forName(className);
            Method declaredMethod = clazz.getDeclaredMethod(method, args[1].getClass());
            boolean needInsertDept = false;
            if (declaredMethod != null && declaredMethod.isAnnotationPresent(NeedInsertDepartment.class)) {
                needInsertDept = true;
            }
            if (needInsertDept) {
                HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
                String token = request.getHeader("authorization");
                String deptId = "";
                if (StringUtils.isNotBlank(token)) {
                    deptId = JwtTokenUtil.getDeptId(token);
                }
                if (StringUtils.isNotBlank(deptId)) {
                    Object parameter = args[1];
                    Field[] declaredFields = parameter.getClass().getDeclaredFields();
                    for (Field field : declaredFields) {
                        if (field.getName().equals("deptId")) {
                            field.setAccessible(true);
                            field.set(parameter, deptId);
                            args[1] = parameter;
                        }
                    }
                }
            }
        }
        return invocation.proceed();
    }

}
