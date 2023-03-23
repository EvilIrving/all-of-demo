package com.ygkj.big.screen.service;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.ApiOperation;

import java.util.List;

/**
 * @author xq
 * @Description
 * @Date 2021/4/24
 */
public interface HydrologyAnalysisService {

    /**
     * 报警统计
     *
     * @return
     */
    List<JSONObject> warnStatistic();

    /**
     * 潮位极值：type=1(最大值)/type=2(最小值)
     *
     * @param type
     * @return
     */
    List<JSONObject> tideExtreme(Integer type);

    /**
     * 水位分析：type=1(大中型水库)/type=2(流域水位)/type=3(平原水位)
     *
     * @param type
     * @return
     */
    List<JSONObject> waterLevelAnalysis(Integer type);

    /**
     * 降雨极值
     *
     * @return
     */
    List<JSONObject> rainfallExtreme();

    /**
     * 水位极值
     *
     * @return
     */
    List<JSONObject> waterLevelExtreme();
}
