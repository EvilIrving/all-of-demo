package com.ygkj.gragh.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public interface DataAggregationService {

    JSONArray ClassificationStatistics(String type);

    JSONObject onlineRateStatistics(String type, String year);

    JSONArray cBit();
}
