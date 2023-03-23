package com.ygkj.visualization.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.ygkj.visualization.model.AttMeteorologicalStBase;
import com.ygkj.visualization.model.StMeR;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface MeteorologicalMapper {
    @DS("business")
    List<StMeR> newestMeteorologicData(String startTime);
    @DS("business")
    List<StMeR> pastMeteorologicData(String stationCode,String startTime);

    @DS("basic")
    @Select("SELECT * FROM att_meteorological_st_base WHERE del_flag= 0")
    List<AttMeteorologicalStBase> meteorologicalStations();
}
