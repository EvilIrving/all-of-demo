package com.ygkj.data.management;

import com.ygkj.data.management.vo.request.BusApiApplyAddVo;
import com.ygkj.data.management.vo.request.BusApiApplyPageQueryVo;
import com.ygkj.data.management.vo.request.TokenVo;
import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@Api(tags = "我的服务/待我审核", value = "我的服务/待我审核")
public interface BusApiApplyControllerApi {

    @ApiOperation("我的服务列表/待我审核列表")
    CommonResult apiApply(BusApiApplyPageQueryVo queryVo);

    @ApiOperation("申请服务")
    CommonResult apply(BusApiApplyAddVo apiApplyAddVo);

    @ApiOperation("审核/取消服务申请")
    @ApiImplicitParams({@ApiImplicitParam(name = "status", value = "取消/已通过/已驳回"),
            @ApiImplicitParam(name = "id", value = "id")})
    CommonResult changeStatus(String status, String id);

    @ApiOperation("获取开放api调用需要的token")
    CommonResult getToken(TokenVo tokenVo);
}
