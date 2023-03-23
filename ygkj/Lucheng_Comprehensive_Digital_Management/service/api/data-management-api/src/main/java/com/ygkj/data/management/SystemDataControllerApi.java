package com.ygkj.data.management;

import com.ygkj.data.management.vo.request.SysUserQueryVo;
import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(value = "系统数据", tags = "系统数据")
public interface SystemDataControllerApi {

    @ApiOperation("查询用户列表")
    CommonResult users(SysUserQueryVo queryVo);
}
