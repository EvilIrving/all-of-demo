package com.ygkj.digitization.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ygkj.digitization.vo.request.*;
import com.ygkj.digitization.vo.response.*;

import java.util.List;
import java.util.Map;

public interface RainAndWaterService {

    JSONObject rainStationSynchronizedStatistic(StationQueryVo queryVo);

    List<WaterLevelStationResVo> waterLevelStationSynchronizedStatistic(StationQueryVo queryVo);

    List<GateStationResVo> gateStationSynchronizedStatistic(StationQueryVo queryVo);

    JSONArray historyStatistic(StationHistoryQueryVo queryVo);

    List<Map<String, Object>> stationMaxWaterLevel(MaxWaterLevelQueryVo queryVo);

    List<Map<String, Object>> stationAverageRecords(AverageWaterAndRainQueryVo queryVo);

    JSONArray relationStatistic(RelationStatisticQueryVo queryVo);

    /**
     * 实时工情-农村供水工程列表
     *
     * @param reqVo
     * @return
     */
    List<CwsResVo> listCws(ProjectListReqVo reqVo);

    List<CwsResVo> listCwsDetail(CwsReqVo reqVo);

    /**
     * 实时雨量导出列表数据获取
     * @param vo
     * @return
     * @throws Exception
     */
    List<RainfallStationExportVo> exportRainfallForList(StationQueryVo vo) throws Exception;

}
