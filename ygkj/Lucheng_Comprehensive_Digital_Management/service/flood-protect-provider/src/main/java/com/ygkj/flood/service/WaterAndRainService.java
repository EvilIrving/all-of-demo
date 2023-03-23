package com.ygkj.flood.service;

import com.alibaba.fastjson.JSONObject;
import com.ygkj.flood.model.AttResBase;
import com.ygkj.flood.model.StPptnR;
import com.ygkj.flood.vo.request.SynRainRequestVo;

import java.util.List;

/**
 * @author xq
 * @Description
 * @Date 2021/4/15
 */
public interface WaterAndRainService {
    List<StPptnR> rainfall(SynRainRequestVo synRainRequestVo);

    List<JSONObject> topNReservoir(Integer index);

    List<JSONObject> BigAndMidReservoirWaterStation(String basin);

    List<StPptnR> rainfallTop10(Integer hours);

    Integer beyondLimitLevelNumber();

    Integer superRainNumber();
}
