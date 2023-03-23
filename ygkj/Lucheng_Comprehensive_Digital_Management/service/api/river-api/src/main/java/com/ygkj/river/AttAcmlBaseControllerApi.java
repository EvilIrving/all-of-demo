package com.ygkj.river;

import com.ygkj.river.vo.request.AttAcmlBaseReqVo;
import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "人工水道管理范围线接口")
public interface AttAcmlBaseControllerApi {

    @ApiOperation("分页查找")
    CommonResult loadPage(AttAcmlBaseReqVo vo);

}
