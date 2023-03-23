package com.ygkj.river;

import com.ygkj.river.vo.request.AttAcalBaseReqVo;
import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "人工水道临水线接口")
public interface AttAcalBaseControllerApi {

    @ApiOperation("分页查找")
    CommonResult loadPage(AttAcalBaseReqVo vo);

}