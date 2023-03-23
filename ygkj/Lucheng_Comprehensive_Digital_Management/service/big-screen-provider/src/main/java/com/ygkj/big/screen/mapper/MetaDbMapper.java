package com.ygkj.big.screen.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.ygkj.big.screen.vo.response.RsvrStationWaterResVo;
import org.apache.ibatis.annotations.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

@DS("meta")
public interface MetaDbMapper {

    @Insert("INSERT INTO rainfall_drill_data (common_result,request_param,api) VALUES (#{commonResult},#{requestParam},#{api})")
    int insert(@Param("commonResult") String commonResult, @Param("requestParam") String requestParam, @Param("api") String api);

    @Update("update rainfall_drill_data set common_result=#{commonResult} where request_param=#{requestParam} and api=#{api}")
    int update(@Param("commonResult") String commonResult, @Param("requestParam") String requestParam, @Param("api") String api);

    @Select("SELECT common_result FROM rainfall_drill_data WHERE request_param = #{param} AND api = #{api}")
    String result(String api, String param);

    @Insert("INSERT INTO torrent_drill_data (data_time,result) VALUES (#{dataTime},#{result})")
    int insertTorrentDrill(@Param("dataTime") String dataTime, @Param("result") String result);

    @Update("update torrent_drill_data set result=#{result} where data_time=#{dataTime}")
    int updateTorrentDrill(@Param("dataTime") String dataTime, @Param("result") String result);

    @Select("SELECT  FROM torrent_drill_data WHERE data_time = #{dataTime}")
    String resultTorrentDrill(@Param("dataTime") String dataTime);

    @Select("select * from rsvr_drill where data_time=#{dataTime}")
    @ResultType(RsvrStationWaterResVo.class)
    List<RsvrStationWaterResVo> selectRsvrDrillDataByTime(Date dataTime);

    @Select("select * from rsvr_drill where data_time>=#{start} and data_time<=#{end} and res_code=#{resCode} order by data_time asc")
    @ResultType(RsvrStationWaterResVo.class)
    List<RsvrStationWaterResVo> selectSingleRsvrDrillDataByTimeInterval(String resCode, Date start, Date end);
}
