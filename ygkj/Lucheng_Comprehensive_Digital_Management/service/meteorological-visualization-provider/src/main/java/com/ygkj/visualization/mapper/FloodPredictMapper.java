package com.ygkj.visualization.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import lombok.Data;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author xq
 * @description
 * @date 2022/5/9
 */
@DS("business")
public interface FloodPredictMapper {

    @Select("select STATION_CODE ,STATION_NAME,LNG,LAT from bus_station where `PROJECT_ID` IS NOT NULL")
    List<BusStationGrid> selectStation4FloodPredict();

    @Select("select a.* " +
            "from bus_station_grid as a " +
            "inner join bus_station as b " +
            "on a.STATION_CODE=b.STATION_CODE " +
            "where b.`PROJECT_ID` IS NOT NULL ")
    List<BusStationGrid> selectRRStation();

    @Update("update bus_station_grid set row_index=#{rowIndex},column_index=#{columnIndex} where STATION_CODE=#{stCode}")
    int updateStGrid(String stCode, int rowIndex, int columnIndex);


    /**
     * 为洪水预报系统插入每小时预报降雨量
     * @param stationCode 测站编码
     * @param upTime
     * @param data 小时雨量
     * @param createTime
     * @param source 数据来源 1默认 2省厅
     * @return
     */
    @Insert("replace into syn_rainhour (STATION_CODE,UP_TIME,DATA,create_time,SOURCE) " +
            "values (#{stationCode},#{upTime},#{data},#{createTime},#{source})")
    int insertPredictHourRain4FloodPredict(String stationCode, Date upTime, String data, Date createTime, String source);


    @Data
    class BusStationGrid {
        private String stationCode;

        private String stationName;

        private BigDecimal lng;

        private BigDecimal lat;

        private Integer rowIndex;

        private Integer columnIndex;

    }
}
