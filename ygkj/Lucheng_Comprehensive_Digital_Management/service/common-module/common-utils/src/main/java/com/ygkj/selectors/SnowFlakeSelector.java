package com.ygkj.selectors;

import com.ygkj.controller.SnowFlakeController;
import com.ygkj.entity.SnowFlakeConfig;
import com.ygkj.service.GenerateDistributionIDService;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;

public class SnowFlakeSelector implements ImportSelector {
    @Override
    public String[] selectImports(AnnotationMetadata annotationMetadata) {
        return new String[]{SnowFlakeConfig.class.getName(),
                GenerateDistributionIDService.class.getName(),
                SnowFlakeController.class.getName()};
    }
}
