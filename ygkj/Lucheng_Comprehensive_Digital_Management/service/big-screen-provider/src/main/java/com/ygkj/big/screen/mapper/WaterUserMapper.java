package com.ygkj.big.screen.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.ygkj.big.screen.model.WiuUseMonth;
import com.ygkj.big.screen.vo.response.WuWaterUserMapRespVo;
import com.ygkj.big.screen.vo.response.WuWiuUseCurveRespVo;
import com.ygkj.big.screen.vo.response.WuWptQslxStatsRespVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface WaterUserMapper {

    List<WuWaterUserMapRespVo> getWiuInfos(@Param("type") String type);

    @DS("business")
    List<WiuUseMonth> getWiuUseMonths(@Param("year") String year, @Param("wiuCode") String wiuCode);

    List<Map<String, Object>> getAdcdWptStats();

    List<WuWptQslxStatsRespVo> wptQslxStatistics(@Param("adcd") String adcd);

    List<Map<String, Object>> adcdWiuStats();

    @DS("business")
    List<WuWiuUseCurveRespVo> wiuUseCurve(@Param("year") String year);

    Integer getWiuNum();

    @DS("business")
    Integer getDocNum();
}
