package com.ygkj.aop.annotation;

import java.lang.annotation.*;

/**
* @ClassName:       LogAnnotation
*                   自定义注解
* @Author:          Huang.zh
* @CreateDate:      2019/10/7 14:22
* @UpdateUser:      Huang.zh
* @UpdateDate:      2019/10/7 14:22
* @Version:         0.0.1
*/
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LogAnnotation {
    /** 模块 */
    String title() default "";

    /** 功能 */
    String action() default "";
}
