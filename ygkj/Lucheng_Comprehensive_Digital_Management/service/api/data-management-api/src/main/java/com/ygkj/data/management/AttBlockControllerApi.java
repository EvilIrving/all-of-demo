package com.ygkj.data.management;

import com.ygkj.data.management.vo.request.AttBlockReqVo;
import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "标段管理", value = "标段管理")
public interface AttBlockControllerApi {

    @ApiOperation(value = "新增或修改")
    CommonResult saveOrUpdate(AttBlockReqVo vo);

    @ApiOperation(value = "删除")
    CommonResult del(String id);

    @ApiOperation(value = "分页查找")
    CommonResult loadPage(AttBlockReqVo vo);

}
