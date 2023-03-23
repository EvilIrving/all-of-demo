package com.ygkj.platform.management;

import com.ygkj.platform.management.vo.request.SysRouteRelationAnalysisPage;
import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "路由关联")
public interface SysRouteRelationAnalysisControllerApi {

    @ApiOperation("查询路由关联分页列表")
    CommonResult pageList(SysRouteRelationAnalysisPage pageVo);
}
