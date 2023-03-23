package com.ygkj.platform.management.vo.request;

import com.ygkj.entity.PageEntity;
import lombok.Data;

@Data
public class SysRouteRelationAnalysisPage extends PageEntity {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private Long id;

    /**
     * 路由链，不大于三个路由
     */
    private String routeIdChain;

    /**
     * 路由名称链
     */
    private String routeNameChain;

    /**
     * 路由链出现的次数
     */
    private Long chainCount;

}
