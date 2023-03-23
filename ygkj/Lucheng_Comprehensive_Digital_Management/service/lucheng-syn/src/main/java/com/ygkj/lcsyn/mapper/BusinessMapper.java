package com.ygkj.lcsyn.mapper;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.ygkj.lcsyn.model.*;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.Date;
import java.util.List;

public interface BusinessMapper {

    void replacePp(@Param("tableName") String tableName,@Param("list") List<StPptnR> list);

    void replaceRiver(@Param("tableName") String tableName,@Param("list") List<StRiverR> list);

    void replaceRsvr(@Param("tableName") String tableName,@Param("list") List<StRsvrR> list);

    void replaceTide(@Param("tableName") String tableName,@Param("list") List<StTideR> list);

    @Select("SHOW TABLES LIKE #{tableName}")
    String checkTableExist(String tableName);

    @Insert("${createDataTableSql}")
    void createTable(String createDataTableSql);

    int replaceWas(@Param("tableName") String tableName,@Param("list") List<StWasR> list);

    void batchInsertProvinceData(List<ProvinceDataRecord> list);

    int updateTyphoon(RealTyphoonData data);

    void insertTyphoon(@Param("list")List<RealTyphoonData> subList);

    List<RealTyphoonData> selectTyphoons();

    @Select("select tm from ${tableName} where stcd = '01201' order by tm desc limit 1 ")
    Date findNearly(String tableName);

    @Select("select concat( '3303',mgstcd) as stationCode,drp as precipitationTime,tm as timeD from ${tableName} " +
            "where stcd = '01201' and tm between #{startTime} and #{endTime} ")
    List<PushPpData> findPpDateByTime(String tableName,Date startTime,Date endTime);

    @Select("select concat( '3303',mgstcd) as stationCode,drp as precipitationTime,tm as timeD from ${tableName} where stcd = '01201' ")
    List<PushPpData> findPpDate(String tableName, String format);

    @Select("select concat( '3303',mgstcd) as stationCode,rz as upwaterLevel,tm as timeD from ${tableName} where stcd = '01201' ")
    List<PushRsvrData> findWaterDate(String tableName);

    @Select("select concat( '3303',mgstcd) as stationCode,rz as upwaterLevel,tm as timeD from ${tableName} " +
            " where stcd = '01201' and tm between #{startTime} and #{endTime} ")
    List<PushRsvrData> findRsvrDateByTime(String tableName, Date startTime, Date endTime);

    void replaceReportFlood(List<RsvrReportFlood> list);
}
