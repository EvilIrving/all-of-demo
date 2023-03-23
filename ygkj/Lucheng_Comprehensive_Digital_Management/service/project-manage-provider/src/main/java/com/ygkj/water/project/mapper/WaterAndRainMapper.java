package com.ygkj.water.project.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.ygkj.gragh.model.*;
import com.ygkj.gragh.vo.response.BswqResVo;
import com.ygkj.project.model.RsvrFloodPredict;
import com.ygkj.project.model.RsvrReportFlood;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;

import java.util.Date;
import java.util.List;

@DS("business")
public interface WaterAndRainMapper {

    List<StPptnR> candidateRainfall(String table, List<String> codes);

    List<StRsvrR> candidateReservoirWaterLevel(String table, List<String> codes, Integer limit);

    List<StRsvrR> candidateReservoirWaterLevelWithTime(String table, List<String> codes, Integer limit, Date tm);

    List<StRsvrR> candidateYjReservoirWaterLevelWithTime(Date tm);

    List<StRsvrR> candidateRsvrWaterLevelWithTimeInternal(@Param("tables") List<String> tables, @Param("codes") List<String> codes, @Param("start") Date start, @Param("end") Date end);

    List<StRiverR> candidateRiverWaterLevel(String table, List<String> codes, Integer limit);

    List<StRiverR> candidateRiverWaterLevelWithTime(String table, List<String> codes, Date tm);

    List<StTideR> candidateTide(String table, List<String> codes);

    List<StTideR> candidateTideWithTime(String table, List<String> codes, Date tm);

    List<StWasR> candidateSluiceWaterLevel(String table, List<String> codes);

    List<StWasR> candidateSluiceWaterLevelWithTime(String table, List<String> codes, Date tm, Integer hours);

    List<StWasR> candidateSluiceWaterLevelWithTimeInternal(@Param("tables") List<String> tables, @Param("codes") List<String> codes, @Param("start") Date start, @Param("end") Date end);

    List<BswqResVo> candidateWaterQuality(@Param("codes") List<String> codes);

    @Select("SHOW TABLES LIKE #{tableName}")
    String checkTableExist(@Param("tableName") String tableName);

    /**
     * 根据时间区间查询洪水预报记录
     *
     * @param resCodes
     * @param startTime
     * @param endTime
     * @return
     */
    @DS("business")
    @Select("select * from rsvr_flood_predict where find_in_set(res_code,#{resCodes}) and forecast_time between #{startTime} and #{endTime}")
    @ResultType(RsvrFloodPredict.class)
    List<RsvrFloodPredict> selectRsvrFloodPredict(@Param("resCodes") String resCodes, @Param("startTime") Date startTime, @Param("endTime") Date endTime);


    /**
     * 查询最新一条洪水预报记录
     *
     * @param resCodes
     * @return
     */
    @DS("business")
    List<RsvrFloodPredict> selectNewestRsvrFloodPredict(@Param("resCodes") List<String> resCodes);

    /**
     * 查询最新一条水库报汛记录
     *
     * @param resCodes
     * @return
     */
    @DS("business")
    List<RsvrReportFlood> selectNewestRsvrReportFlood(@Param("resCodes") List<String> resCodes);

    /**
     * 查询某时刻的泵站工情
     *
     * @param stcds    测站编码 测站编码不能为空 否则会变为全表扫描
     * @param dataTime 数据时间 数据时间为空，则查询最新的
     * @return java.lang.List<>
     */
    List<StPustR> selectPustByTimeCode(List<String> stcds, Date dataTime);


}
