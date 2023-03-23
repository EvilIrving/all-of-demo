package com.ygkj.aspects.annotations;

import com.ygkj.aspects.adapters.ColumnAssignmentAdapter;
import com.ygkj.aspects.aspect.SystemColumnAssignmentAspect;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({ColumnAssignmentAdapter.class, SystemColumnAssignmentAspect.class})
public @interface EnableAutoAssignment {
}
