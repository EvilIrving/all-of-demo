package com.ygkj.flood.service;

import com.ygkj.flood.vo.response.AdRealtimeRainResVo;
import com.ygkj.flood.vo.response.CollectPointRainfall;
import com.ygkj.flood.vo.response.RainfallExtremumResVo;
import com.ygkj.flood.vo.response.RainfallGenResVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.util.List;
import java.util.Map;

/**
 * @author xq
 * @Description 降雨分析服务层
 * @Date 2021/1/18
 */
public interface RainfallAnalysisService {

    List<AdRealtimeRainResVo> getAllAdcdRealtimeRainfall(Integer minutes, String adnm);

    List<RainfallGenResVo> getAllBasinRealtimeRainfall(Integer minutes, String basin);

    Map<String, Integer> getRealtimeRainLevelStatistic(Integer minutes);

    List<RainfallExtremumResVo> getRainfallExtremum(Integer minutes, String stNames);

    Integer getRainWarningStationNum(Integer hours);

    List<Map<String, Object>> getHistoricRainfallExtremum();

    List<Map<String, Object>> getPpStNumStatistic(Integer minutes);

    List<CollectPointRainfall> getCollectPointRainfall();

    List<RainfallGenResVo> getResStRainfall();
}
