package com.ygkj.bootstrap.selector;

import com.ygkj.bootstrap.annotation.EnableGracefulStopBootstrap;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;

import java.util.Map;

public class BootstrapSelector implements ImportSelector {

    @Override
    public String[] selectImports(AnnotationMetadata annotationMetadata) {
        Map<String, Object> map = annotationMetadata.getAnnotationAttributes(EnableGracefulStopBootstrap.class.getName());
        Class bootstrapClass = (Class) map.get("bootstrapClass");
        return new String[]{bootstrapClass.getName()};
    }
}
