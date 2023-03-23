package com.ygkj.aspects.aspect;

import com.alibaba.fastjson.JSONObject;
import com.ygkj.aspects.annotations.CommonApiLog;
import com.ygkj.aspects.event.SysLogApiPersistenceEvent;
import com.ygkj.common.notify.NotifyCenter;
import com.ygkj.jwt.JwtTokenUtil;
import com.ygkj.platform.management.model.SysApiLog;
import com.ygkj.water.model.CommonCode;
import com.ygkj.water.model.CommonResult;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Date;

@Component
@Aspect
public class CommonApiLogAspect {

    private ThreadLocal<SysApiLog> threadLocal = new ThreadLocal<>();

    private static final NotifyCenter notifyCenter = NotifyCenter.instance();

    @Pointcut("@annotation(com.ygkj.aspects.annotations.CommonApiLog)")
    public void execute() {

    }

    @Before(value = "execute()")
    public void before(JoinPoint joinPoint) {
        try {
            Method method = getTargetMethod(joinPoint);
            CommonApiLog annotation = method.getAnnotation(CommonApiLog.class);
            String parameters = getMethodParameters(joinPoint);
            SysApiLog sysApiLog = new SysApiLog(annotation.sysCode(), annotation.sysName()
                    , JwtTokenUtil.getCurrentUserId().orElse("-"), JwtTokenUtil.getCurrentUserName().orElse("-"),
                    JwtTokenUtil.getCurrentUserDepartmentId().orElse("-"), JwtTokenUtil.getCurrentDepartmentName().orElse("-"),
                    annotation.apiName(), annotation.apiCode()
                    , getRequestIP(), System.currentTimeMillis(), new Date(), parameters);
            threadLocal.set(sysApiLog);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    @Around(value = "execute()")
    public Object around(ProceedingJoinPoint joinPoint) {
        Object result = null;
        try {
            result = joinPoint.proceed();
            if (result instanceof CommonResult) {
                CommonResult commonResult = (CommonResult) result;
                SysApiLog log = threadLocal.get();
                log.setCode((int) commonResult.getCode());
                log.setResponseStatus(judgeResponseStatus(log.getCode()));
            }
            return result;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return result;
    }

    private static Method getTargetMethod(JoinPoint joinPoint) throws NoSuchMethodException {
        Class<?> targetCls = joinPoint.getTarget().getClass();
        //获取方法签名(通过此签名获取目标方法信息)
        MethodSignature ms = (MethodSignature) joinPoint.getSignature();
        //获取目标方法上的注解指定的操作名称
        Method targetMethod = targetCls.getDeclaredMethod(ms.getName(),
                ms.getParameterTypes());
        return targetMethod;
    }

    private static String getMethodParameters(JoinPoint joinPoint) {
        Object[] objects = joinPoint.getArgs();
        MethodSignature ms = (MethodSignature) joinPoint.getSignature();
        String[] parameterNames = ms.getParameterNames();
        JSONObject object = new JSONObject();
        for (int i = 0; i < objects.length; i++) {
            object.put(parameterNames[i], objects[i]);
        }
        return object.toJSONString();
    }

    private static String getRequestIP() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    private String judgeResponseStatus(int code) {
        String status;
        if (code == CommonCode.SUCCESS.code()) {
            status = CommonCode.SUCCESS.message();
        } else if (code == CommonCode.FAILED.code()) {
            status = CommonCode.FAILED.message();
        } else if (code == CommonCode.VALIDATE_FAILED.code()) {
            status = CommonCode.VALIDATE_FAILED.message();
        } else if (code == CommonCode.FLOW_LIMITED.code()) {
            status = CommonCode.FLOW_LIMITED.message();
        } else if (code == CommonCode.TABLEAPPLYING.code()) {
            status = CommonCode.TABLEAPPLYING.message();
        } else if (code == CommonCode.TABLENOAUTH.code()) {
            status = CommonCode.TABLENOAUTH.message();
        } else if (code == CommonCode.UNAUTHORIZED.code()) {
            status = CommonCode.UNAUTHORIZED.message();
        } else if (code == CommonCode.FORBIDDEN.code()) {
            status = CommonCode.FORBIDDEN.message();
        } else {
            status = CommonCode.SUCCESS.message();
        }
        return status;
    }

    //after和after*类型的通知不能同时用
    //因为通过动态代理，在同一个线程之中先后执行，会覆盖掉threadLocal之中的值
//    @After(value = "execute()")
//    public void after(){
//        persistenceApiLog();
//    }

    @AfterReturning(value = "execute()")
    public void afterReturning() {
        persistenceApiLog();
    }

    @AfterThrowing(value = "execute()")
    public void AfterThrowing() {
        persistenceApiLog();
    }

    private void persistenceApiLog() {
        SysApiLog sysApiLog = threadLocal.get();
        sysApiLog.setResponseTime(System.currentTimeMillis() - sysApiLog.getResponseTime());
        //由于多线程环境下还是会有一定的IO瓶颈，所以日志的IO还是交给通知中心
        //通知中心会自动托管给业务系统中的subscriber进行异步处理，提高性能
        notifyCenter.publishEvent(new SysLogApiPersistenceEvent(sysApiLog));
        threadLocal.remove();
//        if (null != sysApiLog){
//            sysApiLog.setResponseTime(System.currentTimeMillis() - sysApiLog.getResponseTime());
//            sysAspectApiLogMapper.insertApiLog(sysApiLog);
//        }
    }

}
