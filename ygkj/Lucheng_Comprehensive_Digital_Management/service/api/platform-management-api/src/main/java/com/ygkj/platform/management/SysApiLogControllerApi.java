package com.ygkj.platform.management;

import com.ygkj.platform.management.vo.request.SysApiLogPage;
import com.ygkj.platform.management.vo.request.SysLoginLogPage;
import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "行为日志")
public interface SysApiLogControllerApi {

    @ApiOperation("历史搜索---操作日志分页列表")
    CommonResult pageList(SysApiLogPage pageVo);

    @ApiOperation("历史搜索---登录日志分页列表")
    CommonResult loginLogPageList(SysLoginLogPage page);
}
