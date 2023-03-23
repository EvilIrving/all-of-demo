package com.ygkj.river;

import com.ygkj.river.vo.request.AttPlkpBaseReqVo;
import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "船闸接口")
public interface AttPlkpBaseControllerApi {

    @ApiOperation("分页查找")
    CommonResult loadPage(AttPlkpBaseReqVo vo);

}
