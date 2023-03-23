package com.ygkj.gragh.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ygkj.gragh.model.AttStBase;
import com.ygkj.gragh.model.PrecedeAffectRainfall;
import com.ygkj.gragh.model.StPptnR;
import com.ygkj.gragh.model.StTideR;
import com.ygkj.gragh.vo.request.*;
import com.ygkj.gragh.vo.response.MultiWaterRainResVo;
import com.ygkj.gragh.vo.response.RainSurfaceResVo;
import com.ygkj.gragh.vo.response.RainfallSumResVo;
import com.ygkj.gragh.vo.response.StSoliRRespVo;
import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.ApiOperation;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public interface SynWaterAndRainService {

    List<AttStBase> stations(SynWaterRequestVo requestVo);

    JSONObject waterLevel(WaterLevelQueryVo queryVo);

    List<MultiWaterRainResVo> multiWaterRains(MultiWaterRainReqVo reqVo);

    List<StPptnR> rainfall(SynRainRequestVo synRainRequestVo);

    /**
     * 一张图 雨情报表 + 预警
     *
     * @param synRainRequestVo
     * @return
     */
    CommonResult rainfallForOne(SynRainRequestVo synRainRequestVo);

    /**
     * 降雨 + 超警 列表数量返回
     *
     * @param synRainRequestVo
     * @return
     */
    CommonResult rainfallForOneNum(SynRainRequestVo synRainRequestVo);

    List<StPptnR> hourRainfall(HourRainfallQueryVo hourRainfallQueryVo);

    List<StPptnR> rainfallCurve(HourRainfallQueryVo queryVo);

    JSONArray futureSevenDayRainfallPredict(String longitude, String latitude);

    JSONObject rainfallStatistic(String areaName, String endTime);

    JSONObject rainfallStatistic4Drill(String stcds, String startTime, String endTime);

    JSONArray pastHoursRainfall();

    /**
     * 累计面雨量（包括一些统计值）
     *
     * @return
     */
    JSONObject grandTotalRainfallSurface(String areaName, String endTime);

    JSONObject grandTotalMaxRainfallStatistic(String endTime, Integer hour);

    JSONObject grandTotalMaxRainfallStatistic4Drill(String stcds, String startTime, String endTime, Integer hour);

    JSONArray grandTotalRainfallGrid(SynRainRequestVo requestVo, String endTime, String type, String surfaceType, Integer hour);

    JSONArray grandTotalReservoirRainfall(String endTime, Integer hour);

    JSONArray grandTotalReservoirRainfall4Drill(String stcds, String startTime, String endTime, Integer hour);

    JSONObject judgeRainfallStation(String beginTime, String endTime);

    JSONObject customRainfallSurfaceStatistic(String beginTime, String endTime);

    List<AttStBase> customRainfallSurfaceData(String beginTime, String endTime);

    /**
     * 河网复盘统计
     *
     * @return
     */
    JSONObject tideReplayStats(TideReplayReqVo reqVo);

    List<AttStBase> yongJiaRainStation(SynRainRequestVo vo);

    List<AttStBase> soilMoistureMonitorStations(SynWaterRequestVo vo);

    List<StSoliRRespVo> soilMoistureMonitorLevels(WaterLevelQueryVo vo);


    /**
     * 前期影响雨量曲线
     *
     * @param adcd
     * @return
     */
    List<PrecedeAffectRainfall> precedeAffectRainfallCurve(String adcd);

    /**
     * 前期影响雨量列表
     *
     * @param theDay
     * @return
     */
    List<PrecedeAffectRainfall> todayPrecedeAffectRainfall(LocalDate theDay);

    /**
     * 前期影响雨量c位窗口
     *
     * @return
     */
    JSONObject precedeAffectRainfall4CWindow();

    JSONArray rainfallHistoryMax(String stationCode);

    RainfallSumResVo rainfall24(String stCode);

    /**
     * 导出水位
     * @param waterLevelQueryVo
     * @param response
     */
    void exportWaterLevels(WaterLevelQueryVo waterLevelQueryVo, HttpServletResponse response);

    /**
     * 导出雨量
     * @param hourRainfallQueryVo
     * @param response
     */
    void exportHourRainfall(HourRainfallQueryVo hourRainfallQueryVo, HttpServletResponse response);

     List<StTideR> selectTideByTimeCode(String table, List<String> stcds, Date dataTime);

    JSONObject waterRainHours(WaterLevelVo waterLevelVo);

    RainSurfaceResVo calculateRainfallSurface(String dataInterval, String startTime, String endTime);
}
