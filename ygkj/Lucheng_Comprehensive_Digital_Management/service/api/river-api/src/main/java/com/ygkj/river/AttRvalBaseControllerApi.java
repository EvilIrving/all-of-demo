package com.ygkj.river;

import com.ygkj.river.vo.request.AttRvalBaseReqVo;
import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "河道临水线接口")
public interface AttRvalBaseControllerApi {

    @ApiOperation("分页查找")
    CommonResult loadPage(AttRvalBaseReqVo vo);

}