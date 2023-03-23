package com.ygkj.flood;

import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;

@Api(tags = "联合调度-水库点位", value = "联合调度-水库点位")
public interface UnionDispatchResControllerApi {

    @ApiOperation("大中型水库超汛限统计")
    CommonResult warnNum();

    @ApiOperation("大中型水库地图列表")
    @ApiImplicitParam(name = "projectCode", value = "工程编码(单查)")
    CommonResult resProList(String projectCode);

    @ApiOperation("内涝积水点")
    CommonResult newestFloodPoint(Integer pageNum, Integer pageSize);

    @ApiOperation("一键研判-全部")
    CommonResult warnStatsAll();

    @ApiOperation("一键研判-雨情")
    CommonResult rainfallWarn();

    @ApiOperation("一键研判-江河水情")
    CommonResult riverWagaWarn();

    @ApiOperation("一键研判-大中型水库水情")
    CommonResult largeMidRsvrWarn();

    @ApiOperation("一键研判-内涝积水点")
    CommonResult floodPointWarn();
}
