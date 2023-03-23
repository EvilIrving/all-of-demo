package com.ygkj.digitization;

import com.ygkj.digitization.vo.request.WaterWarnInformationReqVo;
import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "水位预警信息表", value = "水位预警信息表")
public interface WaterWarnInformationControllerApi {

    @ApiOperation(value = "新增或修改")
    CommonResult saveOrUpdate(WaterWarnInformationReqVo vo) throws Exception;

    @ApiOperation(value = "删除")
    CommonResult del(String id);

    @ApiOperation(value = "分页查找")
    CommonResult loadPage(WaterWarnInformationReqVo vo);

}
