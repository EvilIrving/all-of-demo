package com.ygkj.big.screen.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.ygkj.big.screen.model.AttSwhsBase;
import com.ygkj.big.screen.vo.response.DataStatisticsInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author fml
 * @date 2021-04-02 9:43
 * @description 水资源保障专题
 */
public interface WaterProtectionMapper {

    List<Map<String, Object>> waterSourceAdcdStats();

    List<DataStatisticsInfo> waterSourceTypeStats();

    List<AttSwhsBase> getWaterSource(@Param("type") String type, @Param("scale") String scale);

    List<DataStatisticsInfo> wiuValidStats();

    List<DataStatisticsInfo> wiuScaleStats();

    @DS("business")
    List<DataStatisticsInfo> wiuUseWaterMonthStats(@Param("year") String year);

    List<Map<String, Object>> wiuUseWaterAdcdStats(String year);
}
