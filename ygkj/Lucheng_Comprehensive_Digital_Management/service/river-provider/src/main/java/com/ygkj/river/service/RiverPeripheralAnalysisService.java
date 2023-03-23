package com.ygkj.river.service;

import com.alibaba.fastjson.JSONArray;

import java.util.List;
import java.util.Map;

public interface RiverPeripheralAnalysisService {

    JSONArray peripheralAnalysisTree(Integer riverSegId, Integer distance);

    List<Map<String, Object>> selectPoints(String type, Integer riverSegId, Integer distance);
}
