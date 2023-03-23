package com.ygkj.data.management;


import com.ygkj.data.management.vo.request.BusDataTablePageQueryVo;
import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "大屏组件---数据责任", value = "大屏组件---数据责任")
public interface DataDutyControllerApi {

    @ApiOperation("一数一责")
    CommonResult dataAndDuty();

    @ApiOperation("责任人分布")
    CommonResult dutyManDistribute();

    @ApiOperation("数据责任分布")
    CommonResult dutyDataDistribute();

    @ApiOperation("c位数据")
    CommonResult cBit();

    @ApiOperation("列表详情")
    CommonResult detaiList(BusDataTablePageQueryVo queryVo);
}
