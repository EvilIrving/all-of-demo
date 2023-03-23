package com.ygkj.data.management;

import com.ygkj.data.management.vo.request.AttBlockAuthorizaReqVo;
import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "标段授权管理", value = "标段授权管理")
public interface AttBlockAuthorizaControllerApi {

    @ApiOperation(value = "新增或修改")
    CommonResult saveOrUpdate(AttBlockAuthorizaReqVo vo);

    @ApiOperation(value = "删除")
    CommonResult del(String id);

    @ApiOperation(value = "分页查找")
    CommonResult loadPage(AttBlockAuthorizaReqVo vo);

}
