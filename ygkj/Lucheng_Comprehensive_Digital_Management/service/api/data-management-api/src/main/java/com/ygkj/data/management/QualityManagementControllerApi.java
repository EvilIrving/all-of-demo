package com.ygkj.data.management;

import com.ygkj.data.management.vo.request.MyWarningPageQueryVo;
import com.ygkj.data.management.vo.request.RealTimeDataPageQueryVo;
import com.ygkj.data.management.vo.request.WarningHandleVo;
import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "质量管理", value = "质量管理")
public interface QualityManagementControllerApi {

    @ApiOperation("我的管理---实时数据列表")
    CommonResult stations(RealTimeDataPageQueryVo queryVo);

    @ApiOperation("我的告警---实时数据列表")
    CommonResult myWarning(MyWarningPageQueryVo queryVo);

    @ApiOperation("处理告警")
    CommonResult handleWarning(WarningHandleVo handleVo);
}
