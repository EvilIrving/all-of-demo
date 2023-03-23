package com.ygkj.gragh.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.ygkj.gragh.model.*;
import com.ygkj.gragh.vo.request.HourRainfallQueryVo;
import com.ygkj.gragh.vo.request.RainfallQueryVo;
import com.ygkj.gragh.vo.request.WaterLevelQueryVo;
import com.ygkj.gragh.vo.response.StPrrwtResVo;
import com.ygkj.gragh.vo.response.StSoliRRespVo;
import com.ygkj.project.vo.request.SlProjStReqVo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.Date;
import java.util.List;

@DS("business")
public interface SynRainAndWaterMapper {

    List<StRsvrR> selectNewestReserviorWaterLevelFromTable(String table);

    /**
     * 查询某时刻的水库水位
     *
     * @param table    表名
     * @param stcds    测站编码 测站编码不能为空 否则会变为全表扫描
     * @param dataTime 数据时间 数据时间为空，则查询最新的
     * @return
     */
    List<StRsvrR> selectRsvrWaterLevelByTimeCode(String table, List<String> stcds, Date dataTime);


    /**
     * 查询永嘉某个时刻的水位
     *
     * @param dataTime 数据时间 数据时间为空，则查询最新的
     * @return
     */
    List<StRsvrR> selectYjRsvrWaterLevelByTime(Date dataTime);

    List<StRiverR> selectNewestRiverLevelFromTable(String table);

    /**
     * 查询某时刻的河道水位
     *
     * @param table    表名
     * @param stcds    测站编码 测站编码不能为空 否则会变为全表扫描
     * @param dataTime 数据时间 数据时间为空，则查询最新的
     * @return
     */
    List<StRiverR> selectRiverByTimeCode(String table, List<String> stcds, Date dataTime);

    List<StPptnR> selectNewestStationRainfall(String table);

    List<StWasR> selectNewestWas(String table);

    /**
     * 查询某时刻的水闸工情
     *
     * @param table    表名
     * @param stcds    测站编码 测站编码不能为空 否则会变为全表扫描
     * @param dataTime 数据时间 数据时间为空，则查询最新的
     * @return
     */
    List<StWasR> selectWasByTimeCode(String table, List<String> stcds, Date dataTime);

    List<StTideR> selectNewestTide(String table);

    /**
     * 查询某时刻的潮位
     *
     * @param table    表名
     * @param stcds    测站编码 测站编码不能为空 否则会变为全表扫描
     * @param dataTime 数据时间 数据时间为空，则查询最新的
     * @return
     */
    List<StTideR> selectTideByTimeCode(String table, List<String> stcds, Date dataTime);

    /**
     * 查询某时刻的天文潮位
     *
     * @param stcds    测站编码 测站编码不能为空 否则会变为全表扫描
     * @param dataTime 数据时间 数据时间为空，则查询最新的
     * @return
     */
    List<StAstrotdR> selectAstrotdByTimeCode(List<String> stcds, Date dataTime);

    /**
     * 查询某时刻的泵站工情
     *
     * @param stcds    测站编码 测站编码不能为空 否则会变为全表扫描
     * @param dataTime 数据时间 数据时间为空，则查询最新的
     * @return java.lang.List<>
     */
    List<StPustR> selectPustByTimeCode(List<String> stcds, Date dataTime);

    List<StRiverR> selectRiverStationWaterLevel(List<String> list, @Param("start") Date start, @Param("end") Date end, @Param("stationCode") String stationCode);

    List<StRsvrR> selectResStationWaterLevel(List<String> list, @Param("start") Date start, @Param("end") Date end, @Param("stationCode") String stationCode);

    List<StPptnR> selectRainfallByCondition(@Param("list") List<String> list, @Param("queryVo") RainfallQueryVo queryVo);

    List<StPptnR> selectHourRainfall(@Param("list") List<String> list, @Param("queryVo") HourRainfallQueryVo queryVo);

    List<StPptnR> selectYJStationRainfall(@Param("queryVo") HourRainfallQueryVo queryVo);

    List<StWasR> selectHourWas(@Param("list") List<String> list, @Param("start") Date start, @Param("end") Date end, @Param("stationCode") String stationCode);

    List<StTideR> selectHourTide(@Param("list") List<String> list, @Param("start") Date start, @Param("end") Date end, @Param("stationCode") String stationCode);

    int insertRainfall(StPptnR stPptnR);

    int insertReservoirWaterLevel(StRsvrR stRsvrR);


    List<StPptnR> selectPastHoursRainfall(String table, String endTime, Integer hour, List<String> codes, boolean forceTimeIndex);

    List<StPptnR> selectYJPastHoursRainfall(String endTime, Integer hour, List<String> codes);


    /**
     * 根据时间区间查询雨量站的累计降雨量
     *
     * @param tables    数据表名
     * @param startTime 起始时刻
     * @param endTime   截止时刻
     * @param codes     测站编码
     * @return
     */
    List<StPptnR> selectSumRainfallByTimeInterval(List<String> tables, String startTime, String endTime, List<String> codes);

    /**
     * 根据时间区间查询永嘉雨量站的累计降雨量
     *
     * @param startTime
     * @param endTime
     * @param codes
     * @return
     */
    List<StPptnR> selectYjSumRainfallByTimeInterval(String startTime, String endTime, List<String> codes);

    List<StPptnR> selectNewestOneHourRainfall(String table, String startTime, List<String> codes);

    List<StPptnR> selectYJNewestOneHourRainfall(String startTime);

    List<StPptnR> selectNewestOneHourRainfallByPastTime(String table, String endTime, List<String> codes);

    @Select("SELECT a.* FROM (\n" +
            "SELECT MGSTCD,STCD,TM,SUM(drp) AS drp FROM ${table}\n" +
            "WHERE TM BETWEEN DATE_ADD(#{time},INTERVAL -1 HOUR) AND NOW() \n" +
            "GROUP BY mgstcd\n" +
            "UNION ALL\n" +
            "SELECT MGSTCD,STCD,TM,SUM(drp) AS drp FROM ${table}\n" +
            "WHERE TM BETWEEN DATE_ADD(#{time},INTERVAL -3 HOUR) AND NOW() \n" +
            "GROUP BY mgstcd\n" +
            "UNION ALL\n" +
            "SELECT MGSTCD,STCD,TM,SUM(drp) AS drp FROM ${table}\n" +
            "WHERE TM BETWEEN DATE_ADD(#{time},INTERVAL -6 HOUR) AND NOW() \n" +
            "GROUP BY mgstcd\n" +
            "UNION ALL\n" +
            "SELECT MGSTCD,STCD,TM,SUM(drp) AS drp FROM ${table}\n" +
            "WHERE TM BETWEEN DATE_ADD(#{time},INTERVAL -12 HOUR) AND NOW() \n" +
            "GROUP BY mgstcd\n" +
            "UNION ALL\n" +
            "SELECT MGSTCD,STCD,TM,SUM(drp) AS drp FROM ${table}\n" +
            "WHERE TM BETWEEN DATE_ADD(#{time},INTERVAL -24 HOUR) AND NOW() \n" +
            "GROUP BY mgstcd\n" +
            "UNION ALL\n" +
            "SELECT MGSTCD,STCD,TM,SUM(drp) AS drp FROM ${table}\n" +
            "WHERE TM BETWEEN DATE_ADD(#{time},INTERVAL -72 HOUR) AND NOW() \n" +
            "GROUP BY mgstcd\n" +
            ") a ORDER BY a.MGSTCD ")
    List<StPptnR> selectStationPastHoursTotalRainfall(Date time, String table);

    //    @Select("SELECT a.mgstcd,a.drp FROM " +
//            "( SELECT mgstcd, tm, SUM( drp ) AS drp FROM ${table} " +
//            "WHERE " +
//            "<script>" +
//            "<if test='codes != null and codes.size() > 0'>" +
//            " AND  mgstcd IN (" +
//            "<foreach collection='codes' item='item' separator=','>" +
//            "#{item}" +
//            "</foreach>"  +
//            ")" +
//            "</if>" +
//            "</script>" +
//            " tm > DATE_ADD(#{endTime},INTERVAL -${hour} HOUR) AND tm <= #{endTime} GROUP BY mgstcd ORDER BY drp desc) a LIMIT 1")
    StPptnR selectMaxRainfallBetweenPastHours(@Param("table") String table, @Param("endTime") String endTime
            , @Param("hour") Integer hour, @Param("codes") List<String> codes);

    //    @Select("SELECT a.mgstcd,a.drp FROM " +
//            "( SELECT mgstcd, tm, SUM( drp ) AS drp FROM ${table} " +
//            "WHERE" +
//            "<script>" +
//            "<if test='codes != null and codes.size() > 0'>" +
//            " AND  mgstcd IN (" +
//            "<foreach collection='codes' item='item' separator=','>" +
//            "#{item}" +
//            "</foreach>"  +
//            ")" +
//            "</if>" +
//            "</script>" +
//            " tm >= #{startTime} AND tm <= NOW() GROUP BY mgstcd ORDER BY drp desc) a LIMIT 1")
    StPptnR selectNewestOneHourMaxRainfall(@Param("table") String table, @Param("startTime") String startTime,
                                           @Param("codes") List<String> codes, @Param("drill") Boolean drill);

    /**
     * 根据起止时间和测站编码查询水库水位
     *
     * @param tables 水库水位表名
     * @param start  起始时间
     * @param end    结束时间
     * @param stcds  水库水位测站编码
     * @return
     */
    List<StPrrwtResVo> selectRsvrByTimeInterval(@Param("tables") List<String> tables, @Param("start") Date start, @Param("end") Date end, @Param("stcds") List<String> stcds);

    /**
     * 根据起止时间和测站编码查询水闸工情
     *
     * @param tables 水闸测站表名
     * @param start  起始时间
     * @param end    结束时间
     * @param stcds  水闸测站编码
     * @return
     */
    List<StPrrwtResVo> selectWasByTimeInterval(@Param("tables") List<String> tables, @Param("start") Date start, @Param("end") Date end, @Param("stcds") List<String> stcds);

    /**
     * 根据起止时间和测站编码查询河道水位
     *
     * @param tables 河道水位表名
     * @param start  起始时间
     * @param end    结束时间
     * @param stcds  河道水位测站编码
     * @return
     */
    List<StPrrwtResVo> selectRiverByTimeInterval(@Param("tables") List<String> tables, @Param("start") Date start, @Param("end") Date end, @Param("stcds") List<String> stcds);

    /**
     * 根据起止时间和测站编码查询潮位
     *
     * @param tables 潮位测站表名
     * @param start  起始时间
     * @param end    结束时间
     * @param stcds  潮位测站编码
     * @return
     */
    List<StPrrwtResVo> selectTideByTimeInterval(@Param("tables") List<String> tables, @Param("start") Date start, @Param("end") Date end, @Param("stcds") List<String> stcds);
    /**
     * 根据起止时间和测站编码查询降雨
     *
     * @param tables 降雨表名
     * @param start  起始时间
     * @param end    结束时间
     * @param stcds  降雨测站编码
     * @return
     */
    List<StPrrwtResVo> selectRainfallByTimeInterval(@Param("tables") List<String> tables, @Param("start") Date start, @Param("end") Date end, @Param("stcds") List<String> stcds);

    /**
     * 根据起止时间和测站编码查询天文潮位
     *
     * @param start 起始时间
     * @param end   结束时间
     * @param stcds 降雨测站编码
     * @return
     */
    List<StPrrwtResVo> selectAstrotdByTimeInterval(@Param("start") Date start, @Param("end") Date end, @Param("stcds") List<String> stcds);

    /**
     * 根据起止时间和测站编码查询天文潮位
     *
     * @param start 起始时间
     * @param end   结束时间
     * @param stcds 降雨测站编码
     * @return
     */
    List<StPrrwtResVo> selectPustByTimeInterval(@Param("start") Date start, @Param("end") Date end, @Param("stcds") List<String> stcds);

    @Select("SHOW TABLES LIKE #{tableName}")
    String checkTableExist(@Param("tableName") String tableName);

    List<StPptnR> selectYongJiaRainfall(List<String> codes, Double min, Double max);

    List<StSoliR> findSoliRLastNewData(@Param("codes") List<String> codes);

    List<StSoliR> selectSoliRByTime(@Param("codes") List<String> codes, @Param("dataTime") Date dataTime);

    List<StSoliRRespVo> findSoliRListData(@Param("stationCode") String stationCode,
                                          @Param("startTime") String startTime,
                                          @Param("endTime") String endTime);

    @DS("basic")
    List<StPrrwtResVo> listFcstVal(WaterLevelQueryVo queryVo);


    List<StWasR> selectHourWasLevel(@Param("list") List<String> list, @Param("start") Date start, @Param("end") Date end, @Param("stationCode") String stationCode);

    @DS("basic")
    String ProjStRel(@Param("prcd") String prcd, @Param("type") String type);
}
