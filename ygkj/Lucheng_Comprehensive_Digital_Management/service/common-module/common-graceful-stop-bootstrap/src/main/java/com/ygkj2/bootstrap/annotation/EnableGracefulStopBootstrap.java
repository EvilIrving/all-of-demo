package com.ygkj.bootstrap.annotation;

import com.ygkj.bootstrap.selector.BootstrapSelector;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(BootstrapSelector.class)
public @interface EnableGracefulStopBootstrap {

    Class bootstrapClass();
}
