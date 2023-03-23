package com.ygkj.flood.service;

import com.alibaba.fastjson.JSONObject;
import com.ygkj.flood.model.HazardPoint;
import com.ygkj.flood.model.StPptnR;
import com.ygkj.flood.vo.request.EmergencyDispatchRequestVo;
import com.ygkj.flood.vo.request.MaterialDispatchRequestVo;
import com.ygkj.flood.vo.request.SynRainRequestVo;

import java.util.List;

public interface EmergencySupportService {

    JSONObject emergencyDispatch(EmergencyDispatchRequestVo requestVo);

    List<String> materials();

    JSONObject intelligentAnalysis(String teamId, List<MaterialDispatchRequestVo> list);

    List<StPptnR> rainfallWarningInfo(SynRainRequestVo synRainRequestVo);

    List<HazardPoint> hazardPoints();

    JSONObject emergencySupportCount();
}
