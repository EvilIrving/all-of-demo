package com.ygkj.aspects.aspect;

import com.ygkj.exception.exceptions.PermissionDeniedException;
import com.ygkj.jwt.JwtTokenUtil;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Aspect
@Component
public class CheckDataCenterAdminOperationAspect implements InitializingBean {

    private Set<String> candidateDepartmentIds;

    @Pointcut("@annotation(com.ygkj.aspects.annotations.CheckDataCenterAdminOperation)")
    public void execute() {

    }

    @Before(value = "execute()")
    public void before(JoinPoint joinPoint) {
        String departmentId = JwtTokenUtil.getCurrentUserDepartmentId().orElse("");
        if (!candidateDepartmentIds.contains(departmentId)) {
            throw new PermissionDeniedException("当前用户暂无操作权限！");
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        candidateDepartmentIds = new HashSet<>();
        //数据中心部门id
        candidateDepartmentIds.add("6d30d28b-d689-4fee-a718-fe75298fc5c6");
        candidateDepartmentIds.add("fdec6019-9bf3-4768-a967-49e40181d178");
        //...可以再添加新的部门允许操作特定接口
    }
}
