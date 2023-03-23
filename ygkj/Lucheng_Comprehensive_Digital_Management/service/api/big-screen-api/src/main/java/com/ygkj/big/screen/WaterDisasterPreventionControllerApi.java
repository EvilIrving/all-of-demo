package com.ygkj.big.screen;

import com.ygkj.big.screen.vo.request.*;
import com.ygkj.big.screen.vo.response.CurTyphoonReqVo;
import com.ygkj.big.screen.vo.response.MtFcstMonValueDetailResVo;
import com.ygkj.project.vo.request.SeawallWarnReqVo;
import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.*;

import javax.servlet.http.HttpServletResponse;
import java.text.ParseException;
import java.util.Date;

@Api(tags = "水灾害防御大屏", value = "水灾害防御大屏")
public interface WaterDisasterPreventionControllerApi {

    @ApiOperation("气象预警")
    CommonResult weatherWarning();

    @ApiOperation("天气预报")
    @ApiImplicitParam(name = "areaName", value = "区县名称：温州市/鹿城区/瓯海区/瑞安市...")
    CommonResult weatherPredict(String areaName);

    @ApiOperation("预报面雨量")
    @ApiImplicitParam(name = "areaName", value = "区县名称：温州市/鹿城区/瓯海区/瑞安市...")
    CommonResult predictRainfallByAreaName(String areaName);

    @ApiOperation("水库工程统计，统计（总数、超汛限、正常）*（全部、大中型、小（Ⅰ）型、小（Ⅱ）型）")
    CommonResult rsvrProjStats1(@ApiParam("复盘时刻：格式是\"yyyy-MM-dd HH:mm:ss\"，不传则为最新值") Date dataTime,
                                @ApiParam("是否预演") String drillAffairId);

    @ApiOperation("水库工程统计，统计大中型、小（Ⅰ）型、小（Ⅱ）型），平均蓄水率、带纳蓄能力的水库工程list")
    CommonResult rsvrProjStats2(@ApiParam("水库规模：大中型/小型，必传") String scaleType,
                                @ApiParam("复盘时刻：格式是\"yyyy-MM-dd HH:mm:ss\"，不传则为最新值") Date dataTime,
                                @ApiParam("是否预演") String drillAffairId);

    @ApiOperation("大中型水库演练水位曲线")
    CommonResult lmRsvrDrillWlCurve(
            @ApiParam("水库编码") String resCode,
            @ApiParam("演练起始时刻：格式是\"yyyy-MM-dd HH:00:00\"") Date start,
            @ApiParam("演练起始时刻：格式是\"yyyy-MM-dd HH:00:00\"") Date end,
            @ApiParam("往后推的时间戳") Boolean timeForward,
            String drillAffairId);

    @ApiOperation("水库工程统计，统计大中型水库24小时的泄流量")
    CommonResult rsvrProjDrainageStats(@ApiParam("复盘时刻：格式是\"yyyy-MM-dd HH:mm:ss\"，不传则为最新值") Date dataTime);

    @ApiOperation("水库工程列表")
    CommonResult rsvrProjList(RsvrStationWaterReqVo reqVo);

    @ApiOperation("水闸测站统计:水闸工程总数/温瑞平水闸测站(水闸数目/开闸数目/泄流总量)")
    CommonResult wagaStaStats(@ApiParam("复盘时刻：格式是\"yyyy-MM-dd HH:mm:ss\"，不传则为最新值") Date dataTime,
                              @ApiParam("过去多少小时") Integer hours);

    @ApiOperation("水闸测站统计:水闸工程总数/温瑞平水闸测站(水闸数目/开闸数目/泄流总量)")
    CommonResult wagaProjStats(@ApiParam("复盘时刻：格式是\"yyyy-MM-dd HH:mm:ss\"，不传则为最新值") Date dataTime,
                               @ApiParam("过去多少小时") Integer hours);

    @ApiOperation("水闸工程列表")
    CommonResult wagaProjList(WrpWagaQueryReqVo reqVo);

    @ApiOperation("河网大屏统计")
    CommonResult riverNetworkStats(@ApiParam("复盘时刻：格式是\"yyyy-MM-dd HH:mm:ss\"，不传则为最新值") Date dataTime,
                                   @ApiParam("统计类型 1=第一个场景柱状图，2=第二个场景纳洪能力与纳洪量") Integer type);

    @ApiOperation("河网详情列表")
    CommonResult riverNetworkList(RivNetListQueryReqVo reqVo);

    @ApiOperation("潮位曲线")
    CommonResult tideCurve(@ApiParam("潮位测站名") String tideStaName,
                           @ApiParam("复盘时刻：格式是\"yyyy-MM-dd HH:mm:ss\"，不传则为最新值") Date dataTime);

    @ApiOperation("潮位列表")
    CommonResult tideList(TideListQueryReqVo reqVo);

    @ApiOperation("潮位表格-就是有农历的那个翻转")
    CommonResult astroTideTable(@ApiParam("潮位测站名") String tideStaName, @ApiParam("复盘时刻：格式是\"yyyy-MM-dd HH:mm:ss\"，不传则为最新值") Date dataTime);

    @ApiOperation("海塘统计")
    CommonResult seawallProjStats();

    @ApiOperation("海塘工程列表")
    CommonResult seawallProjList(SeawallListQueryReqVo reqVo);

    @ApiOperation("海塘组件C位")
    CommonResult seawallUnitCWindow(SeawallWarnReqVo reqVo);

    @ApiOperation("海塘组件报警卡片")
    CommonResult seawallUnitWarnCard(SeawallWarnReqVo reqVo);

    @ApiOperation("1、3、6小时最大降雨统计（区域/站点/流域）")
    CommonResult rainfallMaxValuePredict(Integer hour);

    @ApiOperation("降雨预报详细表格")
    @ApiImplicitParam(name = "type", value = "统计类型：行政区划/流域")
    CommonResult rainfallPredictGrid(String type);

    @ApiOperation("1、3、6、12、24、72小时累计最大降雨统计（区域/站点/流域）")
    CommonResult grandTotalMaxRainfallStatistic(String endTime, Integer hour, String drillAffairId);

    @ApiOperation("累计降雨量统计详表")
    @ApiImplicitParams({@ApiImplicitParam(name = "endTime", value = "时间点，可不传，默认当前时间"),
            @ApiImplicitParam(name = "type", value = "统计类型：只能传面雨量或者点雨量"),
            @ApiImplicitParam(name = "surfaceType", value = "面雨量统计类型：只能传行政区划或者流域")})
    CommonResult grandTotalRainfallGrid(SynRainRequestVo requestVo, String endTime, String type, String surfaceType, Integer hour);

    @ApiOperation("大中型水库平均面雨量图表")
    @ApiImplicitParams({@ApiImplicitParam(name = "endTime", value = "时间点，可不传，默认当前时间"),
            @ApiImplicitParam(name = "hour", value = "小时")})
    CommonResult grandTotalReservoirRainfall(String endTime, Integer hour, String drillAffairId);

    @ApiOperation("病险工程统计")
    CommonResult dilapProjStats();

    @ApiOperation("病险工程列表")
    CommonResult dilapProjList(DilapProjReqVo reqVo);

    @ApiOperation("风险工程统计")
    CommonResult projRiskStats(@ApiParam("风险类型") String type);

    @ApiOperation("风险工程列表")
    CommonResult projRiskList(ProjRiskReqVo reqVo);

    @ApiOperation("右侧地图落点")
    CommonResult rightMapPoints(@ApiParam("水库/水闸/河网/潮位/海塘/病险工程") String type,
                                @ApiParam("病险工程类型：水库/水闸/山塘/海塘") String dilapProjType);

    @ApiOperation("温州各区县的气象信息")
    CommonResult wholeWeather();

    @ApiOperation("山洪预警")
    @ApiImplicitParams(
            {@ApiImplicitParam(name = "endTime", value = "时间 格式:yyyy-MM-dd HH:mm:ss"),
                    @ApiImplicitParam(name = "currentFlag", value = "是否监测标识")}
    )
    CommonResult floodWarning(String endTime, Boolean currentFlag, String drillAffairId);

    @ApiOperation("山洪基础数据统计")
    CommonResult mountainTorrentBaseStats();

    @ApiOperation("山洪C位统计")
    CommonResult mountainTorrentCWindow(Date dataTime);

    @ApiOperation(value = "山洪C位详表", response = MtFcstMonValueDetailResVo.class)
    CommonResult mountainTorrentCWindowTable(Date dataTime, Integer warnType);

    @ApiOperation(value = "山洪C位详表批量报警", response = CommonResult.class)
    CommonResult batchSendWarnSms(MtSendWarnReqVo reqVo);

    @ApiOperation(value = "山洪报警落点", response = CommonResult.class)
    CommonResult mountainTorrentWarnMapPoint(Date dataTime);

    @ApiOperation(value = "山洪报警落点单个报警", response = CommonResult.class)
    CommonResult singleSendWarnSms(MtSendWarnReqVo reqVo);

    @ApiOperation("山洪报警列表")
    @ApiImplicitParams({@ApiImplicitParam(name = "floodType", value = "山洪类型 5:可能性很大  4:有可能发生山洪")})
    CommonResult flashFloodWarnList(String endTime, Boolean currentFlag, String drillAffairId, String floodType);

    @ApiOperation("清空山洪预警")
    CommonResult clearFloodWarning();

    @ApiImplicitParams({@ApiImplicitParam(name = "endTime", value = "结束时间"),
            @ApiImplicitParam(name = "beginTime", value = "开始时间")})
    @ApiOperation("防御复盘雨量站降雨区间统计")
    CommonResult judgeRainfallIntervalStation(String beginTime, String endTime);

    @ApiImplicitParams({@ApiImplicitParam(name = "endTime", value = "结束时间"),
            @ApiImplicitParam(name = "beginTime", value = "开始时间")})
    @ApiOperation("防御复盘自定义区间面雨量统计")
    CommonResult customRainfallSurfaceStatistic(String beginTime, String endTime);

    @ApiImplicitParams({@ApiImplicitParam(name = "endTime", value = "结束时间"),
            @ApiImplicitParam(name = "beginTime", value = "开始时间")})
    @ApiOperation("防御复盘自定义区间面雨量数据")
    CommonResult customRainfallSurfaceData(String beginTime, String endTime);

    @ApiOperation("水库复盘统计")
    CommonResult rsvrReplayStats(RsvrReplayReqVo reqVo);

    @ApiOperation("温瑞平水闸复盘统计")
    CommonResult wrpWagaReplayStats(WagaReplayReqVo reqVo);

    @ApiOperation("河网复盘统计")
    CommonResult rivernetReplayStats(RivernetReplayReqVo reqVo);


    @ApiOperation("潮位复盘曲线")
    CommonResult tideReplayCurve(@ApiParam("潮位测站名") String tideStaName,
                                 @ApiParam("复盘起始时刻：格式是\"yyyy-MM-dd HH:mm:ss\"") Date start,
                                 @ApiParam("复盘起始时刻：格式是\"yyyy-MM-dd HH:mm:ss\"") Date end);

    @ApiOperation("潮位复盘统计")
    CommonResult tideReplayStats(TideReplayReqVo reqVo);

    @ApiOperation("报警地图落点")
    @ApiImplicitParams({@ApiImplicitParam(name = "type", value = "暴雨/河网/水库/山洪/潮位  不传则查全部"),
            @ApiImplicitParam(name = "endTime", value = "时间 格式:yyyy-MM-dd HH:mm:ss")})
    CommonResult warnMapPoints(String type, String endTime, String drillAffairId);

    @ApiOperation("报警地图落点统计")
    CommonResult warnMapPointStats();

    @ApiOperation("地图报警落点导出")
    void exportWarnMapPoint(HttpServletResponse response);

    @ApiOperation("水文工程测站概化图")
    CommonResult hydrProjStaDiagram(HydrDiagReqVo reqVo);

    @ApiOperation("水文工程测站概化图统计")
    CommonResult hydrProjStaDiagramStatistic(Integer hours);

    @ApiOperation("暴雨演练统计")
    @ApiImplicitParams({@ApiImplicitParam(name = "startTime", value = "时间 格式:yyyy-MM-dd HH:mm:ss"), @ApiImplicitParam(name = "endTime", value = "时间 格式:yyyy-MM-dd HH:mm:ss")})
    CommonResult rainstormDrillStatistics(String startTime, String endTime, String drillAffairId) throws ParseException;


    @ApiOperation("暴雨演练报警")
    @ApiImplicitParams({@ApiImplicitParam(name = "endTime", value = "时间 格式:yyyy-MM-dd HH:mm:ss")})
    CommonResult rainstormDrillWarn(String endTime) throws ParseException;

    @ApiOperation("山洪详情")
    @ApiImplicitParams({@ApiImplicitParam(name = "nvi", value = "村落")
            , @ApiImplicitParam(name = "avi", value = "自然村")
            , @ApiImplicitParam(name = "endTime", value = "时刻 yyyy-MM-dd HH:mm:ss")
    })
    CommonResult flashFloodDetails(String nvi, String avi, Date endTime, String drillAffairId);

    @Deprecated
    @ApiOperation("更新演练降雨等值面")
    Object updateDrillRainContourGisImg(DrillRainContourGisImgReqVo reqVo);

    @ApiOperation("海塘报警统计")
    CommonResult seawallWarnStatistic(SeawallWarnStatisticReqVo reqVo);

    @ApiOperation("潮位报警统计")
    CommonResult tideWarnStatistic();

    @ApiOperation("山洪演练")
    CommonResult mountainTorrentDrill(MountainTorrentDrillReqVo reqVo);

    @ApiOperation("积水点C位统计")
    CommonResult floodPointCWindows(Date dataTime);

    @ApiImplicitParams({@ApiImplicitParam(name = "statsType", value = "1. 按行政区划统计，2. 按是否有积水统计，3 统计积水点数量")
    })
    @ApiOperation("积水统计")
    CommonResult waterLoggingStats(Integer statsType);

    @ApiImplicitParams({@ApiImplicitParam(name = "areaCode", value = "行政区划编码")
            , @ApiImplicitParam(name = "ponding", value = "是否有积水")
            , @ApiImplicitParam(name = "orderColumn", value = "排序column列名")
            , @ApiImplicitParam(name = "ascOrDesc", value = "升序降序,false asc/true desc")
    })
    @ApiOperation("积水列表和落点")
    CommonResult waterLoggingList(String areaCode, Boolean ponding, String name, String orderColumn, Boolean ascOrDesc);

    @ApiOperation("流量组件列表、落点")
    CommonResult waterFlowList();

    @ApiOperation("流量组件统计")
    CommonResult waterFlowStats();

    @ApiOperation("台风C位统计")
    CommonResult typhoonStats4C(CurTyphoonReqVo reqVo);

    @ApiOperation("视频组件，视频列表")
    CommonResult listCamera(String cameraName, String areaCode, String projectType, String status, String orderColumn, Boolean ascOrDesc);

    @ApiOperation("抢险支持，队伍列表")
    CommonResult listTeam(String areaCode, String typeId, String name, String orderColumn, Boolean ascOrDesc);

    @ApiOperation("抢险支持，仓库列表")
    CommonResult listWarehouses(String areaCode, String typeId, String name, String orderColumn, Boolean ascOrDesc);

    @ApiOperation("水资源保障-数字珊溪总览")
    @ApiImplicitParams({@ApiImplicitParam(name = "reservoirName", value = "水库名称 珊溪水库、赵山渡水库")
    })
    CommonResult digitalShanXiOverview(String reservoirName);
}
