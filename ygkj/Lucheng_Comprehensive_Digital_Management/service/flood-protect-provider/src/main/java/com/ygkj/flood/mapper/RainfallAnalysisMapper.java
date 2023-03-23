package com.ygkj.flood.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.ygkj.flood.vo.response.RainfallExtremumResVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author xq
 * @Date 2021/1/18
 */
public interface RainfallAnalysisMapper {

    /**
     * 查询温州下辖一级行政区划所有的站点
     *
     * @return
     */
    @DS("basic")
    List<Map> selectWzAdSt(@Param("adnm") String adnm);

    @DS("basic")
    List<Map> selectStationBasin(@Param("basin") String basin);

    @DS("basic")
    List<Map<String, Object>> selectRainStationWarningThresholdByRainfallPeroid(@Param("rainfallPeroid") String rainfallPeroid);


    @DS("basic")
    List<RainfallExtremumResVo> selectRainfallExtremumByDurationTime(@Param("durationMinutes") Integer durationMinutes,
                                                                     @Param("stName") String stName);

    @DS("basic")
    List<RainfallExtremumResVo> selectAllRainfallExtremum(String stName);

}
