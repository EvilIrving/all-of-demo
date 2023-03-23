package com.ygkj.digitization;

import com.ygkj.digitization.vo.request.WaterRuleConfigurationReqVo;
import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "水位规则配置表", value = "水位规则配置表")
public interface WaterRuleConfigurationControllerApi {

    @ApiOperation(value = "新增或修改")
    CommonResult saveOrUpdate(WaterRuleConfigurationReqVo vo);

    @ApiOperation(value = "删除")
    CommonResult del(String id);

    @ApiOperation(value = "分页查找")
    CommonResult loadPage(WaterRuleConfigurationReqVo vo);

}
