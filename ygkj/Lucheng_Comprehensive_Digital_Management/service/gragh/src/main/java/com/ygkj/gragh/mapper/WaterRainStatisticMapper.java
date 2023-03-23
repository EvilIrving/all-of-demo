package com.ygkj.gragh.mapper;

import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author xq
 * @Date 2021/2/23
 */
public interface WaterRainStatisticMapper {
    List<Map<String, Object>> statisticStationNumByAdcd(@Param("stTypeList") List<String> stTypeList);
}
