package com.ygkj.visualization.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ygkj.entity.SingletonMap;
import com.ygkj.visualization.model.RealTyphoonData;
import com.ygkj.visualization.vo.request.RealTyphoonDataRequestVo;
import com.ygkj.visualization.vo.response.RealTyphoonDataResVo;
import com.ygkj.visualization.vo.response.TyphoonStatisticResVo;
import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.ApiOperation;

import java.util.List;
import java.util.Map;

public interface RealTyphoonService {

    List<RealTyphoonDataResVo> selectByCondition(RealTyphoonDataRequestVo realTyphoonDataRequestVo);

    List<RealTyphoonDataResVo> historyAndCurrent(RealTyphoonDataRequestVo realTyphoonDataRequestVo);

    RealTyphoonData newestTyphoon();

    RealTyphoonData selectDetail(String code);

    RealTyphoonData selectDetailById(Integer id);

    JSONObject analysis(String code,String type);

    double distanceFarFromWenZhou(String longitude,String latitude);

    List<Map<String,Object>> getAdDistanceFromTyphoon(Double lgtd, Double lttd);

    Object typhoon70PerArea(String code);

    JSONArray fuckingCurrentTyphoon();

    TyphoonStatisticResVo typhoonStatistic();

    Object typhoonStatisticRadarMap(Integer year);

    /**
     * 台风静态风险-统计各个行政区划台风登录次数
     * @return
     */
    List<SingletonMap<String, Integer>> landTimesEachAdcd();
}
