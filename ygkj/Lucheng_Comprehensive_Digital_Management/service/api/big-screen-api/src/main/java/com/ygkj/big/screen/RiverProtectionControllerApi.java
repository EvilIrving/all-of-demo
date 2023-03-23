package com.ygkj.big.screen;

import com.ygkj.big.screen.vo.response.RpProblemStatsRespVo;
import com.ygkj.big.screen.vo.response.RpRiverNumStatitstcsRespVo;
import com.ygkj.big.screen.vo.response.RpWaterLevelsRespVo;
import com.ygkj.big.screen.vo.response.WzAdcdInfo;
import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

import java.util.List;

@Api(tags = "河湖保护专题", value = "河湖保护专题")
public interface RiverProtectionControllerApi {

    @ApiOperation("获取温州行政区划信息")
    List<WzAdcdInfo> getWzAdcds();

    @ApiOperation("河流数量统计")
    @ApiImplicitParam(name = "adcd", value = "行政区划")
    CommonResult<List<RpRiverNumStatitstcsRespVo>> riverNumStatistics(String adcd);

    @ApiOperation("河道水位列表")
    @ApiImplicitParam(name = "adcd", value = "行政区划")
    CommonResult<List<RpWaterLevelsRespVo>> waterLevels(String adcd);

    @ApiOperation("四乱问题统计")
    @ApiImplicitParam(name = "type", value = "1行政区划，2四乱类型，3状态")
    CommonResult<List<RpProblemStatsRespVo>> problemStatistics(String type);

    @ApiOperation("雨量分析")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "adcd", value = "1行政区划"),
            @ApiImplicitParam(name = "type", value = "1流域，2行政区划")
    })
    CommonResult railfallStatistics(String adcd, String type);

    @ApiOperation("地图点位")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "type", value = "1水源地、2水库、 3水质监测、 4水位监测、5雨量监测、 6河湖全景图、 7摄像机、 8四乱点位"),
            @ApiImplicitParam(name = "id", value = "id单查")
    })
    CommonResult mapPoint(String type, String id);

    @ApiOperation("水位统计")
    CommonResult waterLevelStatistics();

    @ApiOperation("水质统计")
    CommonResult szStatistics();

    @ApiOperation("雨量统计")
    CommonResult rainfallStatistics();

    @ApiOperation("视频统计")
    CommonResult cameraStatistics();

    @ApiOperation("行政区划内设备统计")
    @ApiImplicitParam(name = "type", value = "1水位/2水质/3雨量/4视频")
    CommonResult adcdEquipmentStats(String type);

    @ApiOperation("行政区划河湖库数量统计")
    @ApiImplicitParam(name = "type", value = "1河流/2湖泊/3水位")
    CommonResult adcdRLRStats(String type);
}
