package com.ygkj.selectors;

import com.ygkj.entity.RsvrFeatureConfig;
import com.ygkj.service.RsvrComputeService;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;

/**
 * @author xq
 * @Description
 * @Date 2021/8/2
 */
public class RsvrComputeServiceSelector implements ImportSelector {
    @Override
    public String[] selectImports(AnnotationMetadata annotationMetadata) {
        return new String[]{RsvrFeatureConfig.class.getName(),
                RsvrComputeService.class.getName()};
    }
}
