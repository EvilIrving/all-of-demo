package com.ygkj.data.management;

import com.ygkj.data.management.vo.request.BusApiBaseAddVo;
import com.ygkj.data.management.vo.request.BusApiBasePageQueryVo;
import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@Api(tags = "服务管理", value = "服务管理")
public interface BusApiBaseControllerApi {

    @ApiOperation("服务列表")
    CommonResult apis(BusApiBasePageQueryVo queryVo);

    @ApiOperation("服务新增")
    CommonResult add(BusApiBaseAddVo apiBaseAddVo);

    @ApiOperation("服务更新")
    CommonResult update(BusApiBaseAddVo apiBaseAddVo);

    @ApiOperation("服务删除")
    CommonResult delete(String id);

    @ApiOperation("服务状态修改（上下线）")
    @ApiImplicitParams({@ApiImplicitParam(name = "status", value = "上线/下线"),
            @ApiImplicitParam(name = "id", value = "id")})
    CommonResult changeStatus(String status, String id);

    @ApiOperation("查询单条api记录")
    CommonResult load(String id);

    @ApiOperation("服务类别")
    CommonResult apiStates();

    @ApiOperation("服务分类")
    CommonResult apiCategory();
}
