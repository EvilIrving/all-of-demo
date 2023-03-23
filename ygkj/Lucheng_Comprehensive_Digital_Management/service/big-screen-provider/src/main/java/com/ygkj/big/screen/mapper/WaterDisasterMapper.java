package com.ygkj.big.screen.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.ygkj.big.screen.model.RealTyphoonData;
import com.ygkj.big.screen.model.StPptnR;
import com.ygkj.big.screen.vo.response.RainfallStaInfo;
import com.ygkj.big.screen.vo.response.WaResStaWaterLevelInfo;
import com.ygkj.big.screen.vo.response.WuWiuStatsRespVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author fml
 * @date 2021-03-31 11:41
 * @description 水灾害防御专题
 */
public interface WaterDisasterMapper {

    List<RainfallStaInfo> getRainfallStas();

    @DS("business")
    List<StPptnR> getRealRainfalls(@Param("tableName") String tableName, @Param("hour") Integer hour);

    List<WaResStaWaterLevelInfo> getLargeMediResStas();

    List<Map<String, Object>> getLargeMediResStaWaterLevels(@Param("tableName") String tableName, @Param("list") List<String> list);

    List<WaResStaWaterLevelInfo> getResStaAndAreas();

    @DS("business")
    List<WuWiuStatsRespVo> typhoonPowerStats(@Param("yearPeriod") int yearPeriod);

    @DS("business")
    List<WuWiuStatsRespVo> hisTyphoonStats(@Param("yearPeriod") int yearPeriod, @Param("type") String type);

    @DS("business")
    List<WuWiuStatsRespVo> yearTyphoonStats(@Param("yearPeriod") int yearPeriod);

    List<RealTyphoonData> typhoonInfos(@Param("id") String id, @Param("year") String year, @Param("landPower") String landPower);
}
