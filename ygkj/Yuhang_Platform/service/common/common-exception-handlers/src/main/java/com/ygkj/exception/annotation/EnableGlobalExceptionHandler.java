package com.ygkj.exception.annotation;

import com.ygkj.exception.handlers.GlobalExceptionHandler;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author Huang.zh
 * @date 2020/9/25 10:23
 * @Description: 用于开启全局参数校验异常拦截
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(GlobalExceptionHandler.class)
public @interface EnableGlobalExceptionHandler {
}
