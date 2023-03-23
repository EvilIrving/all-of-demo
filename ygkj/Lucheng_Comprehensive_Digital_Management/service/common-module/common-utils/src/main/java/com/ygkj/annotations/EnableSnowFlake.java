package com.ygkj.annotations;

import com.ygkj.selectors.SnowFlakeSelector;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(SnowFlakeSelector.class)
public @interface EnableSnowFlake {
}
