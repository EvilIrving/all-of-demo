package com.ygkj.mybatis.selectors;

import com.ygkj.mybatis.configurations.MybatisCustomInterceptorAutoConfiguration;
import com.ygkj.mybatis.interceptors.CustomDepartmentAutoInsertInterceptor;
import com.ygkj.mybatis.interceptors.DepartmentAutoInsertInterceptor;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;

public class MybatisCustomInterceptorSelector implements ImportSelector {

    @Override
    public String[] selectImports(AnnotationMetadata annotationMetadata) {
        return new String[]{CustomDepartmentAutoInsertInterceptor.class.getName(), DepartmentAutoInsertInterceptor.class.getName(),
                MybatisCustomInterceptorAutoConfiguration.class.getName()};
    }
}
