package com.ygkj.aspects.annotations;

import com.ygkj.aspects.aspect.CommonApiLogAspect;
import com.ygkj.aspects.subscriber.ApiEventSubscriber;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(value = {CommonApiLogAspect.class, ApiEventSubscriber.class})
public @interface EnableSystemCommonApiLog {
}
