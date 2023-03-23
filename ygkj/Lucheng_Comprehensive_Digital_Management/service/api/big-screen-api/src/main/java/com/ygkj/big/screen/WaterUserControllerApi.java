package com.ygkj.big.screen;

import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@Api(tags = "取水户专题", value = "取水户专题")
public interface WaterUserControllerApi {

    @ApiOperation("取水户点位/列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "type", value = "1工业取水、2水利发电、3公共供、4农业取水、5其他"),
            @ApiImplicitParam(name = "status", value = "1正常、2许可证过期、3水量超许可、4掉线或未安装监控设施、5取水偏快提醒")
    })
    CommonResult mapPoints(String type, String status);

    @ApiOperation("取水户月取水量弹框")
    @ApiImplicitParam(name = "wiuCode", value = "取水户编码")
    CommonResult waterUserUseMonth(String wiuCode);

    @ApiOperation("取水许可总量统计")
    CommonResult adcdWptStatistics();

    @ApiOperation("取水许可类型统计")
    @ApiImplicitParam(name = "adcd", value = "行政区划")
    CommonResult wptQslxStatistics(String adcd);

    // 取水户统计（按行政区划区分）
    @ApiOperation("取水户统计")
    CommonResult adcdWiuStatistics();

    @ApiOperation("取水曲线")
    @ApiImplicitParam(name = "year", value = "年份")
    CommonResult wiuUseCurve(String year);

    @ApiOperation("档案完整率")
    CommonResult wiuDocRate();

    @ApiOperation("取水预警统计")
    CommonResult waterUseWarn();
}
