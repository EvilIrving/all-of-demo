package com.ygkj.river;

import com.ygkj.river.vo.request.AttAcaaBaseReqVo;
import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "人工水道水域面接口")
public interface AttAcaaBaseControllerApi {

    @ApiOperation("分页查找")
    CommonResult loadPage(AttAcaaBaseReqVo vo);

}
