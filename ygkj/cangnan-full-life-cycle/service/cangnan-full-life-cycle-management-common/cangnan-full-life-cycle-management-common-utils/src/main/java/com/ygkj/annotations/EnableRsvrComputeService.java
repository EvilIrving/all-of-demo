package com.ygkj.annotations;

import com.ygkj.selectors.RsvrComputeServiceSelector;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author xq
 * @Description
 * @Date 2021/8/2
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(RsvrComputeServiceSelector.class)
public @interface EnableRsvrComputeService {
}
