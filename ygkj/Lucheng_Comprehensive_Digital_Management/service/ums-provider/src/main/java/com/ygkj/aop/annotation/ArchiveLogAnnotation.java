package com.ygkj.aop.annotation;

import java.lang.annotation.*;

/**
 * @author hucong
 * @version 1.0
 * @date 2021/1/20 10:54
 * 档案操作日志自定义注解
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ArchiveLogAnnotation {
    /**
     * 操作类型
     */
    String type() default "";

    /**
     * 操作状态 1:查看权限 2:上传权限 3:删除权限 4:下载权限 5:更新权限
     */
    String status() default "";
}
