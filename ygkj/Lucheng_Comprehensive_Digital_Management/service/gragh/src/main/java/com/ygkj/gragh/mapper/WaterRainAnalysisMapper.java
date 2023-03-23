package com.ygkj.gragh.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.ygkj.gragh.model.*;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author xq
 * @Date 2021/2/22
 */

public interface WaterRainAnalysisMapper {

    @DS("business")
    StPptnR selectMaxRainfallOver24Hours(List<String> list);

    @DS("business")
    List<StPptnR> selectHourSumRainfall(@Param("list") List<String> list, @Param("hour") Integer hour);

    @DS("business")
    List<Map<String, Object>> selectRainfallExtrem(@Param("stName") String stName);

    @DS("business")
    List<Map<String, Object>> selectWaterExtrem(@Param("stName") String stName);

    @DS("basic")
    @Select("select a.st_code,b.res_name,b.low_left_long,b.low_left_lat,b.res_code,b.mei_flood_limit_water_level from (select st_code,proj_code from att_st_base where res_grade='大中型水库') as a left JOIN att_res_base as b on a.proj_code=b.res_code")
    List<Map<String, Object>> selectLargeMidResData();


    @DS("business")
    @Select("SHOW TABLES LIKE #{tableName}")
    String checkBusinessTableExist(@Param("tableName") String tableName);

    @DS("business")
    List<StRsvrR> selectNewestReserviorWaterLevel1Hour(@Param("table") String table, @Param("stCodeList") List<String> stCodeList);

    @DS("basic")
    @Select("select st_code,threshold from warning_threshold WHERE del_flag=0 and st_type='PP' and rainfall_period=#{rainfallPeroid}")
    List<Map<String, Object>> selectRainStationWarningThresholdByRainfallPeroid(@Param("rainfallPeroid") String rainfallPeroid);

    @DS("basic")
    List<Map<String, String>> selectStcdStName(@Param("stCodeList") List<String> stCodeList);

    @DS("business")
    int insertExtremWater(ExtremWater extremWater);

    @DS("business")
    int insertExtremRainfall(ExtremRainfall extremRainfall);

    @DS("business")
    List<StRiverR> selectNewestRiverLevel1Hour(@Param("table") String table, @Param("stCodeList") List<String> stCodeList);


    @DS("basic")
    @Select("select * from att_st_base where st_type=#{stType}")
    @ResultType(AttStBase.class)
    List<AttStBase> selectStationByType(@Param("stType") String stType);
}
