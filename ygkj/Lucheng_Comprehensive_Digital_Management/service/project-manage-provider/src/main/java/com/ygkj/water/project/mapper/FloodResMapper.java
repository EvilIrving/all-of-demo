package com.ygkj.water.project.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.ygkj.gragh.model.AttStBase;
import com.ygkj.gragh.model.StRsvrR;
import com.ygkj.gragh.vo.request.HourRainfallQueryVo;
import com.ygkj.project.model.StPptnR;
import com.ygkj.project.vo.request.RainReqVo;
import com.ygkj.project.vo.request.ResProjectReqVo;
import com.ygkj.project.vo.request.WaterReqVo;
import com.ygkj.project.vo.response.ResProjectResVo;
import com.ygkj.project.vo.response.StResVo;
import com.ygkj.project.vo.response.WaterResVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface FloodResMapper {

    @DS("business")
    List<StPptnR> listRain(@Param("list") List<String> list, @Param("startTime") String startTime);

    List<StResVo> listRainStation(RainReqVo reqVo);

    @DS("business")
    List<StPptnR> selectHourRainfall(@Param("list") List<String> list, @Param("queryVo") HourRainfallQueryVo queryVo);

    List<ResProjectResVo> listResProject(ResProjectReqVo reqVo);

    List<WaterResVo> listWater(WaterReqVo reqVo);

    @DS("business")
    List<WaterResVo> listWaterLevel(@Param("tableTime") String tableTime, @Param("startTime") String startTime);

    List<AttStBase> selectStationsByProjectCode(String projectCode);

    @DS("business")
    StRsvrR selectNewestReserviorWaterLevelFromTable(@Param("table") String table, @Param("stationCode") String stationCode);
}
