package com.ygkj.digitization.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.ygkj.digitization.model.RailWaterCommonEntity;
import com.ygkj.digitization.model.ReservoirCapacityCurve;
import com.ygkj.digitization.model.SumRainfallPeriods;
import com.ygkj.digitization.vo.PptnRAssistReq;
import com.ygkj.digitization.vo.request.CwsReqVo;
import com.ygkj.digitization.vo.request.RainfallOrWaterLevelQueryVo;
import com.ygkj.digitization.vo.request.StationHistoryQueryVo;
import com.ygkj.digitization.vo.response.CwsResVo;
import com.ygkj.digitization.vo.response.GateOpenResVo;
import com.ygkj.digitization.vo.response.HourRainfallResVo;
import com.ygkj.digitization.vo.response.WaterLevelResVo;
import com.ygkj.gragh.model.*;
import com.ygkj.gragh.vo.response.StPrrwtResVo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.*;

@DS("business")
public interface SynRainAndWaterMapper {

    List<HourRainfallResVo> pastTwelveHourRainfall(@Param("table") String table,
                                                   @Param("start")String start,@Param("end")String end,
                                                   @Param("list") List<String> list);

    List<WaterLevelResVo> selectNewestReserviorWaterLevelFromTable(String table,@Param("stationCodes") List<String> stationCodes);

    List<WaterLevelResVo> selectNewestRiverLevelFromTable(String table);

    List<GateOpenResVo> selectNewestWas(String table);

    List<Map<String,Object>> selectHistoryReservoirWaterLevel(@Param("list")List<String> tables, @Param("queryVo") StationHistoryQueryVo queryVo);

    List<Map<String,Object>> selectHistoryRiverWaterLevel(@Param("list")List<String> tables, @Param("queryVo") StationHistoryQueryVo queryVo);

    List<Map<String,Object>> selectHistoryGate(@Param("list")List<String> tables, @Param("queryVo") StationHistoryQueryVo queryVo);

    List<Map<String,Object>> selectReserVoirMaxWaterLevel(@Param("table")String table
            ,@Param("start") String start,@Param("end") String end,@Param("list") List<String> list);

    List<Map<String,Object>> selectRiverMaxWaterLevel(@Param("table")String table
            ,@Param("start") String start,@Param("end") String end,@Param("list") List<String> list);

    List<Map<String,Object>> selectRiverAverageWaterLevel(@Param("table")String table
            ,@Param("start") String start,@Param("end") String end,@Param("list") List<String> list);

    List<Map<String,Object>> selectReservoirAverageWaterLevel(@Param("table")String table
            ,@Param("start") String start,@Param("end") String end,@Param("list") List<String> list);

    List<Map<String,Object>> selectSumRainfall(@Param("table")String table
            ,@Param("start") String start,@Param("end") String end,@Param("list") List<String> list);

    List<Map<String,Object>> rainfallRelationStatistic(@Param("list") List<String> tables, @Param("queryVo") RainfallOrWaterLevelQueryVo queryVo);

    List<Map<String,Object>> reservoirWaterLevelRelationStatistic(@Param("list") List<String> tables, @Param("queryVo") RainfallOrWaterLevelQueryVo queryVo);

    List<Map<String,Object>> riverWaterLevelRelationStatistic(@Param("list") List<String> tables, @Param("queryVo") RainfallOrWaterLevelQueryVo queryVo);

    List<CwsResVo> listWaterQuality(Set<String> reqVo);

    List<CwsResVo> listCwsDetail(CwsReqVo reqVo);

    /*时段统计雨量和*/
    List<SumRainfallPeriods> sumRainfallPeriods(@Param("tableNames") List<String> tables, @Param("codes") List<String> codes, @Param("startTime") String startTime, @Param("endTime") String endTime);

    /*时段查找最大值*/
    List<SumRainfallPeriods> selectMaxRainDataTbs(@Param("tableNames") List<String> tableNames, @Param("startTime") String startTime, @Param("endTime") String endTime, @Param("codes") List<String> stcds);

    List<RailWaterCommonEntity> dayRainfallDataList(@Param("startTime") String startTime, @Param("endTime") String endTime, @Param("stcd") String stcd, @Param("tableNames") List<String> tableName);


    /**
     * 根据起止时间和测站编码查询水库水位
     * @param tables 水库水位表名
     * @param start 起始时间
     * @param end 结束时间
     * @param stcds 水库水位测站编码
     * @return
     */
    List<StPrrwtResVo> selectRsvrByTimeInterval(@Param("tables")List<String> tables, @Param("start") Date start, @Param("end")Date end, @Param("stcds")List<String> stcds);

    /**
     * 根据起止时间和测站编码查询水闸工情
     * @param tables 水闸测站表名
     * @param start 起始时间
     * @param end 结束时间
     * @param stcds 水闸测站编码
     * @return
     */
    List<StPrrwtResVo> selectWasByTimeInterval(@Param("tables")List<String> tables, @Param("start")Date start,@Param("end")Date end,@Param("stcds")List<String> stcds);

    /**
     * 根据起止时间和测站编码查询河道水位
     * @param tables 河道水位表名
     * @param start 起始时间
     * @param end 结束时间
     * @param stcds 河道水位测站编码
     * @return
     */
    List<StPrrwtResVo> selectRiverByTimeInterval(@Param("tables")List<String> tables, @Param("start")Date start,@Param("end")Date end,@Param("stcds")List<String> stcds);

    /**
     * 根据起止时间和测站编码查询潮位
     * @param tables 潮位测站表名
     * @param start 起始时间
     * @param end 结束时间
     * @param stcds 潮位测站编码
     * @return
     */
    List<StPrrwtResVo> selectTideByTimeInterval(@Param("tables")List<String> tables, @Param("start")Date start,@Param("end")Date end,@Param("stcds")List<String> stcds);

    /**
     * 根据起止时间和测站编码查询降雨
     * @param tables 降雨表名
     * @param start 起始时间
     * @param end 结束时间
     * @param stcds 降雨测站编码
     * @return
     */
    List<StPrrwtResVo> selectRainfallByTimeInterval(@Param("tables")List<String> tables, @Param("start")Date start,@Param("end")Date end,@Param("stcds")List<String> stcds);

    /**
     * 根据起止时间和测站编码查询天文潮位
     * @param start 起始时间
     * @param end 结束时间
     * @param stcds 降雨测站编码
     * @return
     */
    List<StPrrwtResVo> selectAstrotdByTimeInterval(@Param("start")Date start,@Param("end")Date end,@Param("stcds")List<String> stcds);

    StPrrwtResVo selectAstrotdByTime(@Param("dataTime")Date dataTime,@Param("stcd")String stcd);

    List<StPrrwtResVo> selectAstrotdBy2Timestamp(@Param("dataTime1")Date dataTime1,@Param("dataTime2")Date dataTime2,@Param("stcd")String stcd);

    @Select("SHOW TABLES LIKE #{tableName}")
    String checkTableExist(@Param("tableName")String tableName);

    /*每小时累计雨量*/
    List<PptnRAssistReq> selectStcdDataHour(@Param("startTime") String startTime, @Param("endTime") String endTime,
                                            @Param("stcd") String stcd, @Param("tableName") String tableName);

    /*三小时累计雨量*/
    PptnRAssistReq sumStcdDataThreeHour(@Param("startTime") String startTime, @Param("endTime") String endTime,
                                        @Param("stcd") String stcd, @Param("tableName") String tableName);

    /**
     * 一小时累计降雨量
     * @param tables 分表
     * @param start 开始时刻
     * @param end 结束时刻
     * @param codes 测站编码
     * @return java.util.List<StPptnR>
     */
    List<StPptnR> hourRainfall(@Param("tables") List<String> tables, @Param("start") Date start, @Param("end") Date end,
                               @Param("codes") List<String> codes);


    List<StPptnR> selectYongJiaRainfall(@Param("codes") List<String> codes,@Param("startTime") String startTime,
                                        @Param("endTime") String endTime);

    /**
     * 永嘉一小时累计降雨量
     * @param start 开始时刻
     * @param end 结束时刻
     * @param codes 测站编码
     * @return java.util.List<StPptnR>
     */
    List<StPptnR> hourYjRainfall(@Param("start") Date start, @Param("end") Date end,
                               @Param("codes") List<String> codes);

    /**
     * 查询某时刻的水库水位
     * @param table 表名
     * @param stcds 测站编码 测站编码不能为空 否则会变为全表扫描
     * @param dataTime 数据时间 数据时间为空，则查询最新的
     * @return
     */
    List<StRsvrR> selectRsvrWaterLevelByTimeCode(String table,List<String> stcds,Date dataTime);

    /**
     * 查询某时刻的河道水位
     * @param table 表名
     * @param stcds 测站编码 测站编码不能为空 否则会变为全表扫描
     * @param dataTime 数据时间 数据时间为空，则查询最新的
     * @return
     */
    List<StRiverR> selectRiverByTimeCode(String table, List<String> stcds, Date dataTime);

    List<StPptnR> selectNewestStationRainfall(String table);

    /**
     * 查询某时刻的潮位
     * @param table 表名
     * @param stcds 测站编码 测站编码不能为空 否则会变为全表扫描
     * @param dataTime 数据时间 数据时间为空，则查询最新的
     * @return
     */
    List<StTideR> selectTideByTimeCode(String table, List<String> stcds, Date dataTime);

    /**
     * 查询某时刻的水闸工情
     * @param table 表名
     * @param stcds 测站编码 测站编码不能为空 否则会变为全表扫描
     * @param dataTime 数据时间 数据时间为空，则查询最新的
     * @return
     */
    List<StWasR> selectWasByTimeCode(String table, List<String> stcds, Date dataTime);

    /**
     * 查询水库整点水位
     * @param tables 水库表名
     * @param start 起始时刻
     * @param end 截止时刻
     * @param stcd 水库测站编码
     * @return java.util.List
     */
    List<StRsvrR> selectHourRsvr(@Param("tables")List<String> tables,@Param("start")Date start,@Param("end")Date end,@Param("stcd") String stcd);

    /**
     * 根据测站查询库容曲线
     * @param code 测站代码
     * @return
     */
    List<ReservoirCapacityCurve> selectRsvrCapcityCurveAsc(String code);
}
