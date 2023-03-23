package com.ygkj.visualization.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.ygkj.gragh.model.AttStBase;
import com.ygkj.gragh.model.AverageRainfall;
import com.ygkj.gragh.model.StPptnR;
import com.ygkj.visualization.model.AttRainGrid;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface WaterAndRainMapper {

    @DS("basic")
    @Select("<script>" +
            "SELECT * FROM att_st_base WHERE del_flag = 0 AND st_type = 'PP'" +
            "<if test = \"areaCode != null and areaCode != ''\">AND area_code = #{areaCode}</if>" +
            "</script>")
    List<AttStBase> findRainStation(@Param("areaCode") String areaCode);

    @DS("business")
    @Select("SELECT AVG(rainfall) AS rainfall,area_code,area_name,mgstcd  FROM average_rainfall WHERE  year = #{year} AND month = #{month} GROUP BY area_code")
    List<AverageRainfall> findStationTotalRainfallByYearMonth(String year,String month);

    @DS("business")
    @Select("SELECT AVG(rainfall) AS rainfall,mgstcd,area_name,area_code FROM average_rainfall WHERE year <> #{year} AND month = #{month} " +
            "GROUP BY area_code")
    List<AverageRainfall> findPastStationTotalRainfall(String year,String month);

    @DS("business")
    @Select("SELECT stcd,tm,drp,DATE_FORMAT(tm,'%Y-%m-%d %H:00:00') AS hourTime FROM st_pptn_r_202104 " +
            "WHERE tm >= #{time} " +
            "GROUP BY stcd,hourTime ORDER BY stcd,hourTime ")
    List<StPptnR> pastHoursRainfall(String table,String time);

    @Select("<script> SELECT * FROM att_rain_grid " +
            "<if test = 'areaName != null and areaName != \"\"'>WHERE area_name = #{areaName}</if>" +
            "</script>")
    List<AttRainGrid> grids(String areaName);

    @Select("SELECT * FROM att_rain_grid WHERE longitude LIKE CONCAT(#{longitude},'%') AND latitude LIKE CONCAT(#{latitude},'%')")
    List<AttRainGrid> loadByCoordinate(String longitude,String latitude);


    @Select("select * from att_rain_grid")
    List<AttRainGrid> rainGridList();

    @Select("select town_name from att_rain_grid group by town_name")
    List<String> townNames();
}
