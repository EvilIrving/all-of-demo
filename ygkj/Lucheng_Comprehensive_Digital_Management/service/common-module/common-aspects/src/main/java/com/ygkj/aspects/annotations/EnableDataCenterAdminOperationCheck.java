package com.ygkj.aspects.annotations;

import com.ygkj.aspects.aspect.CheckDataCenterAdminOperationAspect;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(value = {CheckDataCenterAdminOperationAspect.class})
public @interface EnableDataCenterAdminOperationCheck {
}
