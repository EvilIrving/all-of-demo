package com.ygkj.river;

import com.ygkj.river.vo.request.AttPslpBaseReqVo;
import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "水闸接口")
public interface AttPslpBaseControllerApi {

    @ApiOperation("分页查找")
    CommonResult loadPage(AttPslpBaseReqVo vo);

}
