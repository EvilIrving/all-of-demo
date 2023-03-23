package com.ygkj.river;

import com.ygkj.river.vo.request.AttRswlBaseReqVo;
import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "水库水面线接口")
public interface AttRswlBaseControllerApi {

    @ApiOperation("分页查找")
    CommonResult loadPage(AttRswlBaseReqVo vo);

}
