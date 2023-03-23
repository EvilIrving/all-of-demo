package com.ygkj.platform.management;

import com.ygkj.platform.management.model.SysRouteUserVisitWarn;
import com.ygkj.platform.management.vo.request.SysRouteUserVisitWarnPage;
import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "用户查看页面提醒")
public interface SysRouteUserVisitWarnControllerApi {

    @ApiOperation("添加用户查看页面提醒")
    CommonResult add(SysRouteUserVisitWarn addVo);

    @ApiOperation("用户查看页面提醒列表")
    CommonResult pageList(SysRouteUserVisitWarnPage pageVo);
}
