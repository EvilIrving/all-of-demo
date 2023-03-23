package com.ygkj.aspects.aspect;

import com.ygkj.aspects.adapters.ColumnAssignmentAdapter;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Aspect
@Component
@Slf4j
public class SystemColumnAssignmentAspect implements InitializingBean {

    private ColumnAssignmentAdapter columnAssignmentAdapter;

    public SystemColumnAssignmentAspect(@Autowired ColumnAssignmentAdapter columnAssignmentAdapter) {
        this.columnAssignmentAdapter = columnAssignmentAdapter;
    }

    @Pointcut("@annotation(com.ygkj.aspects.annotations.EnableSystemColumnAssignment)")
    public void execute() {

    }

    @Before(value = "execute()")
    public void before(JoinPoint joinPoint) throws NoSuchMethodException, IllegalAccessException {
        Method targetMethod = getTargetMethod(joinPoint);
        Object[] args = joinPoint.getArgs();
        for (Object arg : args) {
            log.info("正在为方法{}的参数类型{}填充系统字段...", targetMethod.getName(), arg.getClass().getName());
            columnAssignmentAdapter.assignmentSystemColumns(arg);
        }
    }

    /**
     * @param joinPoint
     * @return
     * @throws NoSuchMethodException
     * @auther huang.zh
     */
    private static Method getTargetMethod(JoinPoint joinPoint) throws NoSuchMethodException {
        Class<?> targetCls = joinPoint.getTarget().getClass();
        //获取方法签名(通过此签名获取目标方法信息)
        MethodSignature ms = (MethodSignature) joinPoint.getSignature();
        //获取目标方法上的注解指定的操作名称
        Method targetMethod = targetCls.getDeclaredMethod(ms.getName(),
                ms.getParameterTypes());
        return targetMethod;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        log.info("-----系统参数填充功能已完成初始化-----");
    }
}
