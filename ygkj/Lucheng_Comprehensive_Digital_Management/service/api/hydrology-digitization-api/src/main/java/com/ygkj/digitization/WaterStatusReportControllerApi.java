package com.ygkj.digitization;

import com.ygkj.digitization.vo.request.MrwlCurveReqVo;
import com.ygkj.digitization.vo.request.MrwlReqVo;
import com.ygkj.digitization.vo.request.WaterStatusReportReqVo;
import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import javax.servlet.http.HttpServletResponse;

/**
 * @author xq
 * @Description
 * @Date 2021/6/8
 */
@Api(tags = "水情报表")
public interface WaterStatusReportControllerApi {

    @ApiOperation("河网水位")
    CommonResult rivernetWaterLevel(WaterStatusReportReqVo reqVo);

    @ApiOperation("河网水位-导出")
    void rivernetWaterLevelExport(WaterStatusReportReqVo reqVo, HttpServletResponse response);

    @ApiOperation("列出山区河道水位的流量曲线配置列表")
    CommonResult listMrwlZqCurveConfig();

    @ApiOperation("山区河道水位曲线")
    CommonResult mrwlCurve(MrwlCurveReqVo reqVo);

    @ApiOperation("山区河道水位表")
    CommonResult mountainRiverWaterLevel(MrwlReqVo reqVo);

    @ApiOperation("河道水位统计表")
    CommonResult riverWaterStats(WaterStatusReportReqVo reqVo);

    @ApiOperation("河道水位统计表-导出")
    void riverWaterStatsExport(WaterStatusReportReqVo reqVo, HttpServletResponse response);

    @ApiOperation("河道日平均水位")
    CommonResult riverDayAvgWater(WaterStatusReportReqVo reqVo);

    @ApiOperation("河道日平均水位-导出")
    void riverDayAvgWaterExport(WaterStatusReportReqVo reqVo, HttpServletResponse response);

    @ApiOperation("河道水位超警表")
    CommonResult riverOverWarn(WaterStatusReportReqVo reqVo);

    @ApiOperation("河道水位超警表-导出")
    void riverOverWarnExport(WaterStatusReportReqVo reqVo, HttpServletResponse response);

    @ApiOperation("逐时潮位增水")
    CommonResult tideSubPerHour(WaterStatusReportReqVo reqVo);

    @ApiOperation("逐时潮位增水-导出")
    void tideSubPerHourExport(WaterStatusReportReqVo reqVo, HttpServletResponse response);

    @ApiOperation("高潮位增水")
    CommonResult highTideSub(WaterStatusReportReqVo reqVo);

    @ApiOperation("高潮位增水-导出")
    void highTideSubExport(WaterStatusReportReqVo reqVo, HttpServletResponse response);

    @ApiOperation("水库蓄水统计表")
    CommonResult rsvrStorageStats(WaterStatusReportReqVo reqVo);

    @ApiOperation("水库蓄水统计表-导出")
    void rsvrStorageStatsExport(WaterStatusReportReqVo reqVo, HttpServletResponse response);

    @ApiOperation("水库水位统计表")
    CommonResult rsvrWaterStats(WaterStatusReportReqVo reqVo);

    @ApiOperation("水库水位统计表-导出")
    void rsvrWaterStatsExport(WaterStatusReportReqVo reqVo, HttpServletResponse response);

    @ApiOperation("水库水位时段表")
    CommonResult rsvrWaterTimeInterval(WaterStatusReportReqVo reqVo);

    @ApiOperation("水库水位时段表-导出")
    void rsvrWaterTimeIntervalExport(WaterStatusReportReqVo reqVo, HttpServletResponse response);

    @ApiOperation("水库水位超讯表")
    CommonResult rsvrWaterOverLimit(WaterStatusReportReqVo reqVo);

    @ApiOperation("水库水位超讯表-导出")
    void rsvrWaterOverLimitExport(WaterStatusReportReqVo reqVo, HttpServletResponse response);

    @ApiOperation("水库水雨情过程表")
    CommonResult rsvrWaterRainInfo(WaterStatusReportReqVo reqVo);

    @ApiOperation("水库水雨情过程表-导出")
    void rsvrWaterRainInfoExport(WaterStatusReportReqVo reqVo, HttpServletResponse response);

    @ApiOperation("水库名称编码列表")
    CommonResult rsvrList(String engScalType);
}
