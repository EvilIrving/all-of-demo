package com.ygkj.digitization;

import com.ygkj.digitization.vo.request.WaterWarnTemplateReqVo;
import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "水位预警模板表", value = "水位预警模板表")
public interface WaterWarnTemplateControllerApi {

    @ApiOperation(value = "新增或修改")
    CommonResult saveOrUpdate(WaterWarnTemplateReqVo vo);

    @ApiOperation(value = "删除")
    CommonResult del(String id);

    @ApiOperation(value = "分页查找")
    CommonResult loadPage(WaterWarnTemplateReqVo vo);

}
