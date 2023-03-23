package com.ygkj.data.management;

import com.ygkj.data.management.vo.request.BusDataTablePageQueryVo;
import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "大屏组件---数据治理", value = "大屏组件---数据治理")
public interface DataGovernanceControllerApi {

    @ApiOperation("数据来源")
    CommonResult dataSources();

    @ApiOperation("治理规则")
    CommonResult governanceRules();

    @ApiOperation("治理成效")
    CommonResult governanceEffectiveness();

    @ApiOperation("数据完整率")
    CommonResult dataCompleteRate();

    @ApiOperation("c位数据")
    CommonResult cBit();

    @ApiOperation("详表弹窗数据")
    CommonResult form(BusDataTablePageQueryVo queryVo);

}
