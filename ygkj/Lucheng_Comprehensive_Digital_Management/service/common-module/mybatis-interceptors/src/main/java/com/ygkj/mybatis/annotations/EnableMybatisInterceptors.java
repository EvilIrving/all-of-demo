package com.ygkj.mybatis.annotations;

import com.ygkj.mybatis.selectors.MybatisCustomInterceptorSelector;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(MybatisCustomInterceptorSelector.class)
public @interface EnableMybatisInterceptors {
}
