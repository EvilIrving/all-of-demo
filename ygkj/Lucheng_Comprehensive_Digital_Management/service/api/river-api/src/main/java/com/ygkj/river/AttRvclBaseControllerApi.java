package com.ygkj.river;

import com.ygkj.river.vo.request.AttRvclBaseReqVo;
import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "河道中心线接口")
public interface AttRvclBaseControllerApi {

    @ApiOperation("分页查找")
    CommonResult loadPage(AttRvclBaseReqVo vo);

}
