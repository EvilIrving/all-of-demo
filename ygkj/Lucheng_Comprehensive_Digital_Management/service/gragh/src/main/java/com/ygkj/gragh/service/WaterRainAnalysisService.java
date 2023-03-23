package com.ygkj.gragh.service;

import java.util.List;
import java.util.Map;

/**
 * @author xq
 * @Date 2021/2/22
 */
public interface WaterRainAnalysisService {
    Map<String, Object> selectWaterRainSimpleReport();

    Map<String, Object> rainfallOverview();

    Map<String, Object> riverOverview();

    Map<String, Object> largeMidResOverview();

    List<Map<String, Object>> selectHistoryExtrem(String type, String stName);
}
