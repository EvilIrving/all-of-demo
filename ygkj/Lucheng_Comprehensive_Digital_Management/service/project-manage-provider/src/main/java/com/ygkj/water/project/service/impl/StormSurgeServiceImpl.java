package com.ygkj.water.project.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ygkj.project.model.WaveHeight;
import com.ygkj.utils.CollectionUtils;
import com.ygkj.water.project.service.StormSurgeService;
import com.ygkj.water.project.utils.WaveHeightUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class StormSurgeServiceImpl implements StormSurgeService {
    @Override
    public Object forecastWaveHeight(String tideLevelJsonArray, String windSpeedJsonArray, Double fetchLength, Double averageDepth) {
        List<JSONObject>  result = new ArrayList<>();
        // 转换为天文潮list
        List<JSONObject> tideLevelList = JSONArray.parseArray(tideLevelJsonArray, JSONObject.class);
        // 转换为风速list
        List<JSONObject> windSpeedList = JSONArray.parseArray(windSpeedJsonArray, JSONObject.class);

        Map<String, List<JSONObject>> windSpeedMap = windSpeedList.stream().collect(Collectors.groupingBy(o -> o.getString("time")));

        for (JSONObject jsonObject : tideLevelList) {
            JSONObject jsonObject1 = new JSONObject();
            jsonObject1.put("time",jsonObject.getString("time"));
            if (windSpeedMap.containsKey(jsonObject.getString("time"))){
                List<JSONObject> list = windSpeedMap.get(jsonObject.getString("time"));
                JSONObject object = list.get(0);
                WaveHeight waveHeight = WaveHeightUtils.WaveHeight(object.getDouble("windSpeed"), 10, fetchLength, averageDepth);
                double h2 = waveHeight.getH2();
                Double tideLevel = jsonObject.getDouble("tideLevel");
                Double d = tideLevel + h2;
                if(d == null || d.isNaN()){
                    d = 0d;
                }
                jsonObject1.put("h",new BigDecimal(d + "").setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue());
            }else {
                jsonObject1.put("h",null);
            }
            result.add(jsonObject1);
        }
        if (CollectionUtils.isNotBlank(result)){
           result = result.stream().sorted(Comparator.comparing(o ->  o.getDate("time"))).collect(Collectors.toList());
        }
        return result;
    }
}
