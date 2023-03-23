package com.ygkj.platform.management;

import com.ygkj.platform.management.model.SysRouteUserVisitRule;
import com.ygkj.platform.management.vo.request.SysRouteUserVisitRulePage;
import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "用户查看页面设置")
public interface SysRouteUserVisitRuleControllerApi {

    @ApiOperation("添加用户查看页面设置")
    CommonResult add(SysRouteUserVisitRule addVo);

    @ApiOperation("用户查看页面设置列表")
    CommonResult pageList(SysRouteUserVisitRulePage pageVo);
}
