package com.ygkj.river;

import com.ygkj.river.vo.request.AttAcapBaseReqVo;
import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "人工水道临水线点接口")
public interface AttAcapBaseControllerApi {

    @ApiOperation("分页查找")
    CommonResult loadPage(AttAcapBaseReqVo vo);

}
