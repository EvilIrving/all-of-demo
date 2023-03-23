package com.ygkj.river;

import com.ygkj.river.vo.request.AttAcwlBaseReqVo;
import com.ygkj.river.vo.request.AttRvalBaseReqVo;
import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "人工水道水面线接口")
public interface AttAcwlBaseControllerApi {

    @ApiOperation("分页查找")
    CommonResult loadPage(AttAcwlBaseReqVo vo);

}
