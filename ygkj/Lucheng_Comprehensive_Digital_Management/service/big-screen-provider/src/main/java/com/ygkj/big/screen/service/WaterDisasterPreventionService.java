package com.ygkj.big.screen.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ygkj.big.screen.vo.request.*;
import com.ygkj.big.screen.vo.response.*;
import com.ygkj.flood.vo.request.AttTeamBasePageQueryVo;
import com.ygkj.flood.vo.request.AttWarehousePageQueryVo;
import com.ygkj.project.vo.request.HikCameraQueryReqVo;
import com.ygkj.project.vo.request.SeawallWarnReqVo;
import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

/**
 * 水灾害防御大屏service
 */
public interface WaterDisasterPreventionService {

    JSONArray weatherWarning();


    CommonResult weatherPredict(String areaName);

    CommonResult predictRainfallByAreaName(String areaName);

    /**
     * 水库工程统计，统计（总数、超汛限、正常）*（全部、大中型、小（Ⅰ）型、小（Ⅱ）型）
     */
    JSONObject rsvrProjStats1(Date dataTime, String affairId);

    /**
     * 水库工程统计，统计大中型、小（Ⅰ）型、小（Ⅱ）型），平均蓄水率、最小纳蓄能力的水库
     *
     * @param scaleType 大中型/小型
     * @param dataTime  复盘时刻
     * @return
     */
    JSONObject rsvrProjStats2(String scaleType, Date dataTime, String affairId);

    /**
     * 大中型水库演练水位曲线
     *
     * @param resCode 水库编码
     * @param start   演练起始时刻：格式是"yyyy-MM-dd HH:00:00"
     * @param end     演练截止时刻：格式是"yyyy-MM-dd HH:00:00"
     * @return
     */
    List<RsvrStationWaterResVo> lmRsvrDrillWlCurve(String resCode, Date start, Date end, Boolean timeForward, String drillAffairId);

    /**
     * 水库工程统计，统计大中型水库24小时的泄流量
     *
     * @param dataTime 复盘时刻：格式是"yyyy-MM-dd HH:mm:ss"，不传则为最新值
     * @return
     */
    JSONObject rsvrProjDrainageStats(Date dataTime);

    /**
     * 水库工程统计列表
     *
     * @param reqVo
     * @return
     */
    JSONObject rsvrProjList(RsvrStationWaterReqVo reqVo);

    /**
     * 水闸测站统计:水闸测站总数/温瑞平(水闸数目/开闸数目/泄流总量
     *
     * @param dataTime 复盘时刻
     * @return
     */
    JSONObject wagaStaStats(Date dataTime, Integer hours);

    /**
     * 水闸工程统计:水闸工程总数/温瑞平水闸工程(水闸数目/开闸数目/泄流总量
     *
     * @param dataTime 复盘时刻
     * @return
     */
    JSONObject wagaProjStats(Date dataTime, Integer hours);

    /**
     * 水闸工程列表 同时统计水闸数量、小型水闸数量、闸门开启数量、24h累计排洪量（所有水闸的过闸流量*24小时）
     *
     * @param reqVo
     * @return
     */
    JSONObject wagaProjList(WrpWagaQueryReqVo reqVo);

    /**
     * 根据潮位名称，获取实时潮位和天文潮位的曲线
     *
     * @param tideStaName 潮位测站名称
     * @param dataTime    复盘时刻
     * @return
     */
    JSONObject tideCurve(String tideStaName, Date dataTime);

    /**
     * 潮位列表
     *
     * @return
     */
    JSONObject tideList(TideListQueryReqVo reqVo);

    /**
     * astroTideTable
     *
     * @param dataTime 复盘时刻
     * @return
     */
    List<JSONObject> astroTideTable(String tideStaName, Date dataTime);

    /**
     * 统计海塘长度，海塘段数
     *
     * @return
     */
    CommonResult seawallProjStats();

    /**
     * 海塘工程列表
     *
     * @return
     */
    CommonResult seawallProjList(SeawallListQueryReqVo reqVo);

    /**
     * 海塘组件C位
     *
     * @param reqVo
     * @return
     */
    CommonResult seawallUnitCWindow(SeawallWarnReqVo reqVo);

    /**
     * 海塘组件报警卡片
     *
     * @param reqVo
     * @return
     */
    CommonResult seawallUnitWarnCard(SeawallWarnReqVo reqVo);


    CommonResult rainfallMaxValuePredict(Integer hour);

    CommonResult rainfallPredictGrid(String type);

    CommonResult grandTotalMaxRainfallStatistic(String endTime, Integer hour, String affairId);

    CommonResult grandTotalRainfallGrid(SynRainRequestVo requestVo, String endTime, String type, String surfaceType, Integer hour);

    CommonResult grandTotalReservoirRainfall(String endTime, Integer hour, String affairId);

    /**
     * 河网测站统计，统计正常、超警戒、超保证数目
     *
     * @param dataTime 复盘时刻
     * @return
     */
    JSONObject riverNetStats(Date dataTime, Integer type);

    /**
     * 河网列表
     *
     * @return
     */
    JSONObject riverNetworkList(RivNetListQueryReqVo reqVo);


    /**
     * 病险工程统计
     *
     * @return
     */
    List<ChartResVo<String, Long>> dilapProjStats();

    /**
     * 病险工程列表
     *
     * @return
     */
    CommonResult dilapProjList(DilapProjReqVo reqVo);

    /**
     * 工程风险统计
     *
     * @param type
     * @return
     */
    JSONObject projRiskStats(String type);

    /**
     * 工程风险列表
     *
     * @param reqVo
     * @return
     */
    CommonResult projRiskList(ProjRiskReqVo reqVo);


    /**
     * 右侧地图落点
     *
     * @param type:         水库/水闸/河网/潮位/海塘/病险工程
     * @param dilapProjType 病险工程类型：水库/水闸/山塘/海塘
     * @return
     */
    Object rightMapPoints(String type, String dilapProjType);

    CommonResult wholeWeather();

    /**
     * 山洪预警模块
     * 旧的模块我实在是改不下去了
     *
     * @param endTime
     * @param currentFlag
     * @param drillAffairId
     * @return
     */
    JSONObject floodWarning4NewVersion(String endTime, Boolean currentFlag, String drillAffairId);

    /**
     * 山洪基础数据落点
     *
     * @return
     */
    MountainTorrentBaseInfoStats mountainTorrentBaseStats();


    /**
     * 山洪C位统计
     *
     * @param dataTime
     * @return
     */
    JSONObject mountainTorrentCWindow(Date dataTime);

    /**
     * 山洪C位详表
     *
     * @param dataTime
     * @param warnType
     * @return
     */
    Object mountainTorrentCWindowTable(Date dataTime, Integer warnType);

    /**
     * 山洪C位详表批量报警
     *
     * @param reqVo
     * @return
     */
    CommonResult batchSendWarnSms(MtSendWarnReqVo reqVo);

    /**
     * 山洪报警落点
     *
     * @param dataTime
     * @return
     */
    Object mountainTorrentWarnMapPoint(Date dataTime);

    /**
     * 山洪报警落点单个报警
     *
     * @param reqVo
     * @return
     */
    CommonResult singleSendWarnSms(MtSendWarnReqVo reqVo);

    JSONObject floodWarning(String endTime, Boolean currentFlag, String drillAffairId);

    CommonResult clearFloodWarning();

    /**
     * 水库复盘统计
     *
     * @return
     */
    CommonResult rsvrReplayStats(RsvrReplayReqVo reqVo);

    /**
     * 水闸复盘统计
     *
     * @return
     */
    CommonResult wrpWagaReplayStats(WagaReplayReqVo reqVo);

    /**
     * 河网复盘统计
     *
     * @return
     */
    CommonResult rivernetReplayStats(RivernetReplayReqVo reqVo);


    /**
     * 潮位复盘曲线
     *
     * @param tideStaName 潮位站名称
     * @param start       复盘起始时刻
     * @param end         复盘结束时刻
     * @return
     */
    JSONObject tideReplayCurve(String tideStaName, Date start, Date end);


    CommonResult tideReplayStats(TideReplayReqVo reqVo);


    CommonResult judgeRainfallIntervalStation(String beginTime, String endTime);

    CommonResult customRainfallSurfaceStatistic(String beginTime, String endTime);

    CommonResult customRainfallSurfaceData(String beginTime, String endTime);


    /**
     * 报警地图落点
     *
     * @param type 暴雨/河网/水库/山洪/潮位  不传则查全部
     * @return
     */
    Object warnMapPoints(String type,
                         String endTime, String affairId);

    @Deprecated
    int testWarnMapPoint();

    /**
     * 报警地图落点统计
     *
     * @return
     */
    JSONObject warnMapPointStats();

    /**
     * 地图报警落点导出
     *
     * @param response
     */
    void exportWarnMapPoint(HttpServletResponse response);

    /**
     * 水文工程测站概化图
     *
     * @param reqVo
     * @return
     */
    CommonResult hydrProjStaDiagram(HydrDiagReqVo reqVo);

    /**
     * 水文工程测站概化图
     *
     * @return
     */
    CommonResult hydrProjStaDiagramStatistic(Integer hours);

    /**
     * 暴雨演练统计
     *
     * @param endTime
     * @return
     */
    JSONObject rainstormDrillStatistics(String startTime, String endTime, String affairId) throws ParseException;

    JSONObject rainstormDrillWarn(String endTime) throws ParseException;

    /**
     * 山洪详情
     *
     * @param nvi
     * @return
     */
    JSONObject flashFloodDetails(String nvi, String avi, Date endTime, String affairId);

    /**
     * 更新降雨
     *
     * @return
     */
    Object updateDrillRainContourGisImg(DrillRainContourGisImgReqVo reqVo);


    /**
     * 海塘报警统计
     *
     * @param reqVo
     * @return
     */
    CommonResult seawallWarnStatistic(SeawallWarnStatisticReqVo reqVo);

    /**
     * 潮位报警统计
     *
     * @return
     */
    JSONObject tideWarnStatistic();

    /**
     * 山洪演练
     *
     * @return
     */
    MountainTorrentDrillResVo mountainTorrentDrill(MountainTorrentDrillReqVo reqVo);

    /**
     * 积水点组件C位统计
     *
     * @param dataTime
     * @return
     */
    CommonResult floodPointCWindows(Date dataTime);


    /**
     * 积水统计
     *
     * @param statsType
     * @return
     */
    CommonResult waterLoggingStats(Integer statsType);

    /**
     * 积水列表和落点
     *
     * @param areaCode
     * @param ponding
     * @return
     */
    CommonResult waterLoggingList(String areaCode, Boolean ponding, String name, String orderColumn, Boolean ascOrDesc);

    /**
     * 流量组件列表、落点
     *
     * @return
     */
    CommonResult waterFlowList();

    /**
     * 流量组件统计
     *
     * @return
     */
    CommonResult waterFlowStats();


    /**
     * 台风组件C位统计
     *
     * @param reqVo
     * @return
     */
    CommonResult typhoonStats4C(CurTyphoonReqVo reqVo);

    CommonResult listCamera(HikCameraQueryReqVo vo);


    CommonResult listTeam(AttTeamBasePageQueryVo reqVo);

    CommonResult listWarehouses(AttWarehousePageQueryVo reqVo);

    List<Villages> flashFloodWarnList(String endTime, Boolean currentFlag, String drillAffairId, String floodType);

    /**
     * 水资源保障-数字珊溪总览
     *
     * @param reservoirName
     * @return
     */
    JSONObject digitalShanXiOverview(String reservoirName);
}
