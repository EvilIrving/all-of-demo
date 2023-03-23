package com.ygkj.aspects.annotations;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CommonApiLog {

    //系统标识
    String sysCode();

    //系统名称
    String sysName();

    //api名称
    String apiName();

    //api方法
    String apiCode();
}
