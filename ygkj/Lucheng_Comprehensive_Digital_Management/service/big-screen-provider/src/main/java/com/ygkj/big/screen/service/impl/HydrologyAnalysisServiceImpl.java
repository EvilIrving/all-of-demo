package com.ygkj.big.screen.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ygkj.big.screen.feign.FloodProtectClient;
import com.ygkj.big.screen.feign.GraphClient;
import com.ygkj.big.screen.feign.MeteorologicVisualizationClient;
import com.ygkj.big.screen.service.HydrologyAnalysisService;
import com.ygkj.big.screen.service.MeteorologicVisualizationService;
import com.ygkj.big.screen.model.AttStBase;
import com.ygkj.big.screen.vo.request.SynWaterRequestVo;
import com.ygkj.enums.StationType;
import com.ygkj.utils.Builder;
import com.ygkj.utils.CollectionUtils;
import com.ygkj.utils.DateUtils;
import com.ygkj.water.model.CommonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author xq
 * @Description
 * @Date 2021/4/24
 */
@Service
public class HydrologyAnalysisServiceImpl implements HydrologyAnalysisService {

    @Autowired
    MeteorologicVisualizationClient mvClient;

    @Autowired
    GraphClient graphClient;

    @Autowired
    FloodProtectClient floodClient;

    @Autowired
    MeteorologicVisualizationService meteorologicVisualizationService;

    @Override
    public List<JSONObject> warnStatistic() {
        JSONObject result = new JSONObject();
        List<JSONObject> water = meteorologicVisualizationService.warnStatsNum("水位");
        List<JSONObject> hour1 = meteorologicVisualizationService.warnStatsNum("1h");
        List<JSONObject> hour3 = meteorologicVisualizationService.warnStatsNum("3h");
        if (CollectionUtils.isNotBlank(water)) {
            result.put("waterWarn", water.get(0).getIntValue("warnNum"));
        } else {
            result.put("waterWarn", 0);
        }
        if (CollectionUtils.isNotBlank(hour1)) {
            result.put("rain1h", hour1.get(0).getIntValue("warnNum"));
        } else {
            result.put("rain1h", 0);
        }
        if (CollectionUtils.isNotBlank(hour3)) {
            result.put("rain3h", hour3.get(0).getIntValue("warnNum"));
        } else {
            result.put("rain3h", 0);
        }
        int num = 0;
        CommonResult<List<AttStBase>> response = graphClient.waterStations(Builder.of(SynWaterRequestVo::new).with(SynWaterRequestVo::setStationType, StationType.TIDE.type()).build());
        if (CollectionUtils.isNotBlank(response.getData())) {
            List<AttStBase> data = response.getData();
            for (AttStBase temp : data) {
                if (temp.getWarningLevel() != null && temp.getWaterLevel() >= temp.getWarningLevel()) {
                    num++;
                } else if (temp.getGuaranteeLevel() != null && temp.getWaterLevel() >= temp.getGuaranteeLevel()) {
                    num++;
                }
            }
        }
        result.put("tideWarn", num);
        return Arrays.asList(result);
    }

    @Override
    public List<JSONObject> tideExtreme(Integer type) {
        List<JSONObject> result = new ArrayList<>();
        CommonResult<List<AttStBase>> response = graphClient.waterStations(Builder.of(SynWaterRequestVo::new).with(SynWaterRequestVo::setStationType, StationType.TIDE.type()).build());
        if (CollectionUtils.isNotBlank(response.getData())) {
            List<AttStBase> data = response.getData();
            if (type == 1) {
                data = data.stream().sorted((o1, o2) -> ((Double) o2.getWaterLevel()).compareTo(o1.getWaterLevel())).collect(Collectors.toList());
            } else if (type == 2) {
                data = data.stream().sorted((o1, o2) -> ((Double) o1.getWaterLevel()).compareTo(o2.getWaterLevel())).collect(Collectors.toList());
            }
            for (AttStBase o : data) {
                result.add(Builder.of(JSONObject::new)
                        .with(JSONObject::put, "name", o.getStName())
                        .with(JSONObject::put, "waterLevel", o.getWaterLevel())
                        .build());
            }
        }
        return result;
    }

    @Override
    public List<JSONObject> waterLevelAnalysis(Integer type) {
        List<JSONObject> result = new ArrayList<>();
        if (type == 1) {
            CommonResult<List<AttStBase>> response = graphClient.waterStations(Builder.of(SynWaterRequestVo::new)
                    .with(SynWaterRequestVo::setStationType, StationType.RESERVOIR.type())
                    .with(SynWaterRequestVo::setResGrade, "大中型水库")
                    .build());
            if (CollectionUtils.isNotBlank(response.getData())) {
                List<AttStBase> data = response.getData();
                for (AttStBase o : data) {
                    result.add(Builder.of(JSONObject::new)
                            .with(JSONObject::put, "name", o.getStName())
                            .with(JSONObject::put, "waterLevel", o.getWaterLevel())
                            .with(JSONObject::put, "warningLevel", o.getWarningLevel())
                            .build());
                }
            }
        } else if (type == 2) {
            CommonResult<List<LinkedHashMap>> response = floodClient.watLevOfPlainBasInnerRiv(null, 2);
            if (CollectionUtils.isNotBlank(response.getData())) {
                List<LinkedHashMap> data = response.getData();
                for (LinkedHashMap o : data) {
                    result.add(Builder.of(JSONObject::new)
                            .with(JSONObject::put, "name", o.get("stName"))
                            .with(JSONObject::put, "waterLevel", o.get("realLevel"))
                            .with(JSONObject::put, "warningLevel", o.get("warningLevel"))
                            .build());
                }
            }
        } else if (type == 3) {
            CommonResult<List<LinkedHashMap>> response = floodClient.watLevOfPlainBasInnerRiv(null, 1);
            if (CollectionUtils.isNotBlank(response.getData())) {
                List<LinkedHashMap> data = response.getData();
                for (LinkedHashMap o : data) {
                    result.add(Builder.of(JSONObject::new)
                            .with(JSONObject::put, "name", o.get("stName"))
                            .with(JSONObject::put, "waterLevel", o.get("realLevel"))
                            .with(JSONObject::put, "warningLevel", o.get("warningLevel"))
                            .build());

                }
            }
        }
        return result;
    }

    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Override
    public List<JSONObject> rainfallExtreme() {
        CommonResult<List<LinkedHashMap>> response = floodClient.getHistoricRainfallExtremum();
        if (CollectionUtils.isNotBlank(response.getData())) {
            List<LinkedHashMap> data = response.getData();
            List<JSONObject> tempList = JSONArray.parseArray(JSONObject.toJSONString(data)).toJavaList(JSONObject.class);
            List<JSONObject> result = new ArrayList<>(tempList.size());
            for (JSONObject temp : tempList) {
                JSONObject r = new JSONObject();
                r.put("name", temp.getString("st_name"));
                r.put("type", temp.getString("drcd"));
                r.put("value", temp.getDoubleValue("drp"));
                r.put("date", DateUtils.date2LocalDateTime(temp.getDate("tm")).format(dtf));
                result.add(r);
            }
            return result;
        } else {
            return Collections.emptyList();
        }
    }

    @Override
    public List<JSONObject> waterLevelExtreme() {
        CommonResult<List<LinkedHashMap>> response = floodClient.getHistoricWaterLevelExtremum(null);
        if (CollectionUtils.isNotBlank(response.getData())) {
            List<LinkedHashMap> data = response.getData();
            List<JSONObject> tempList = JSONArray.parseArray(JSONObject.toJSONString(data)).toJavaList(JSONObject.class);
            List<JSONObject> result = new ArrayList<>(tempList.size());
            for (JSONObject temp : tempList) {
                JSONObject r = new JSONObject();
                r.put("name", temp.getString("st_name"));
                r.put("value", temp.getDoubleValue("rz"));
                r.put("date", DateUtils.date2LocalDateTime(temp.getDate("tm")).format(dtf));
                result.add(r);
            }
            return result;
        } else {
            return Collections.emptyList();
        }
    }
}
