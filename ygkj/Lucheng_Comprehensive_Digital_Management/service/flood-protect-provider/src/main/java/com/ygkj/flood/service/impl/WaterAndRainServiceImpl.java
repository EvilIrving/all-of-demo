package com.ygkj.flood.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ygkj.enums.StationType;
import com.ygkj.flood.feign.WaterAndRainClient;
import com.ygkj.flood.mapper.FloodProjectMapper;
import com.ygkj.flood.mapper.WaterAndRainMapper;
import com.ygkj.flood.model.*;
import com.ygkj.flood.service.WaterAndRainService;
import com.ygkj.flood.vo.request.SynRainRequestVo;
import com.ygkj.water.model.CommonCode;
import com.ygkj.water.model.CommonResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author xq
 * @Description
 * @Date 2021/4/15
 */
@Service
public class WaterAndRainServiceImpl implements WaterAndRainService {

    //    @Autowired
    private WaterAndRainClient waterAndRainClient;

    //    @Autowired
    private WaterAndRainMapper waterAndRainMapper;

    private FloodProjectMapper floodProjectMapper;

    public WaterAndRainServiceImpl(@Autowired WaterAndRainClient waterAndRainClient,
                                   @Autowired WaterAndRainMapper waterAndRainMapper,
                                   @Autowired FloodProjectMapper floodProjectMapper
    ) {
        this.waterAndRainClient = waterAndRainClient;
        this.waterAndRainMapper = waterAndRainMapper;
        this.floodProjectMapper = floodProjectMapper;
    }

    @Override
    public List<StPptnR> rainfall(SynRainRequestVo synRainRequestVo) {
        //模仿前端 请求参数
        CommonResult<List<StPptnR>> result = waterAndRainClient.rainfall(synRainRequestVo);
        if (result.getCode() == CommonCode.SUCCESS.code()) {
            return result.getData();
        } else {
            return Collections.emptyList();
        }
    }

    @Override
    public List<JSONObject> topNReservoir(Integer index) {
        if (index == null) {
            index = new Integer(10);
        }
        List<JSONObject> result = new ArrayList<>();
        List<StBase> stations = waterAndRainMapper.findLargeAndMidReservoirStation(null);
        List<String> stationCodeList = stations.stream().map(StBase::getStCode).collect(Collectors.toList());
        String table = judgeReservoirWaterLevelTable();
        List<StRsvrR> waterLevels = waterAndRainMapper.selectNewestLargeMidReservoirWaterLevel(table, stationCodeList);
        Map<String, List<StBase>> stationMap = stations.stream().collect(Collectors.groupingBy(StBase::getStCode));
        //超汛的水库
        List<StRsvrR> beyond = waterLevels.stream().filter(stRsvrR ->
                stRsvrR.getRz().doubleValue() > stationMap.get(stRsvrR.getMgstcd()).get(0).getMeiFloodLimitWaterLevel().doubleValue())
                .collect(Collectors.toList());
        for (StRsvrR stRsvrR : beyond) {
            StBase station = stationMap.get(stRsvrR.getMgstcd()).get(0);
            result.add(convertJson(station, stRsvrR));
        }
        //正常的水库
        waterLevels = waterLevels.stream().filter(stRsvrR -> !beyond.contains(stRsvrR)).collect(Collectors.toList());
        for (StRsvrR stRsvrR : waterLevels) {
            StBase station = stationMap.get(stRsvrR.getMgstcd()).get(0);
            result.add(convertJson(station, stRsvrR));
        }
        //升序
        result.sort(Comparator.comparing(jsonObject -> jsonObject.getDouble("distance")));
        return result.subList(0, index);
    }

    @Override
    public List<JSONObject> BigAndMidReservoirWaterStation(String basin) {
        List<JSONObject> result = new ArrayList<>();
        List<StBase> stations = waterAndRainMapper.findLargeAndMidReservoirStation(basin);
        List<String> stationCodeList = stations.stream().map(StBase::getStCode).collect(Collectors.toList());
        String table = judgeReservoirWaterLevelTable();
        List<StRsvrR> waterLevels = waterAndRainMapper.selectNewestLargeMidReservoirWaterLevel(table, stationCodeList);
        Map<String, List<StRsvrR>> waterLevelMap = waterLevels.stream().collect(Collectors.groupingBy(StRsvrR::getMgstcd));

        List<Map<String, String>> maps = this.floodProjectMapper.listFloodIdByResCode(stations.stream()
                .map(StBase::getProjCode)
                .collect(Collectors.joining(",")));
        Map<String, Optional<String>> floodIdMap = maps.stream().collect(Collectors.toMap(o -> o.get("res_code"), o -> Optional.ofNullable(o.get("flood_id"))));
        for (StBase station : stations) {
            String stationCode = station.getStCode();
            if (!waterLevelMap.containsKey(stationCode)) {
                continue;
            }
            JSONObject jsObj = convertJson(station, waterLevelMap.get(stationCode).get(0));
            Optional<String> floodId = floodIdMap.getOrDefault(station.getProjCode(), Optional.empty());
            if (floodId.isPresent()) {
                jsObj.put("floodId", floodId.get());
            } else {
                jsObj.put("floodId", null);
            }
            result.add(jsObj);
        }
//        for (StRsvrR stRsvrR : waterLevels) {
//            StBase station = stationMap.get(stRsvrR.getMgstcd()).get(0);
//            result.add(convertJson(station,stRsvrR));
//        }
        return result;
    }

    @Override
    public List<StPptnR> rainfallTop10(Integer hours) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.HOUR_OF_DAY, -1 * hours);
        String table = judgeRainfallTable();
        List<AttStBase> stations = waterAndRainMapper.selectStation();
        Map<String, AttStBase> stationMap = stations.stream().collect(Collectors.toMap(AttStBase::getStCode, o -> o));
        List<StPptnR> rainfalls = waterAndRainMapper.top10Rainfall(table, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(calendar.getTime()));
        for (StPptnR rainfall : rainfalls) {
            AttStBase st = stationMap.get(rainfall.getStcd());
            if (st != null) {
                rainfall.setStationName(st.getStName());
                if (st.getStLat() != null) {
                    rainfall.setLatitude(st.getStLat().doubleValue());
                }
                if (st.getStLong() != null) {
                    rainfall.setLongitude(st.getStLong().doubleValue());
                }
            }
        }
        return rainfalls;
    }

    @Override
    public Integer beyondLimitLevelNumber() {
        List<JSONObject> list = BigAndMidReservoirWaterStation(null);
        return (int) list.stream().filter
                (jsonObject -> jsonObject.getDoubleValue("currentWaterLevel") - jsonObject.getDoubleValue("limitLevel") > 0)
                .count();
    }

    @Override
    public Integer superRainNumber() {
        List<StPptnR> rainfall = rainfallTop10(1);
        return (int) rainfall.stream().filter(stPptnR -> stPptnR.getDrp().doubleValue() > 25).count();
    }

    private JSONObject convertJson(StBase station, StRsvrR stRsvrR) {
        JSONObject jsonObject = new JSONObject();
        double waterLevel = stRsvrR.getRz().doubleValue();
        jsonObject.put("currentWaterLevel", waterLevel);
        double limitLevel = station.getMeiFloodLimitWaterLevel().doubleValue();
        jsonObject.put("limitLevel", limitLevel);
        jsonObject.put("reservoirName", station.getResName());
        jsonObject.put("distance", limitLevel - waterLevel);
        jsonObject.put("longitude", station.getStLong().doubleValue());
        jsonObject.put("latitude", station.getStLat().doubleValue());
        jsonObject.put("reservoirCode", station.getProjCode());
        jsonObject.put("stCode", station.getStCode());
        jsonObject.put("stType", station.getStType());
        return jsonObject;
    }

    private static String judgeReservoirWaterLevelTable() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        String year = String.valueOf(calendar.get(Calendar.YEAR));
        int monthIndex = calendar.get(Calendar.MONTH) + 1;
        String month;
        if (monthIndex < 10) {
            month = "0" + monthIndex;
        } else {
            month = String.valueOf(monthIndex);
        }
        String table = StationType.RESERVOIR.table().concat(year.concat(month));
        return table;
    }

    private static String judgeRainfallTable() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        String year = String.valueOf(calendar.get(Calendar.YEAR));
        int monthIndex = calendar.get(Calendar.MONTH) + 1;
        String month;
        if (monthIndex < 10) {
            month = "0" + monthIndex;
        } else {
            month = String.valueOf(monthIndex);
        }
        String table = StationType.RAINFALL.table().concat(year.concat(month));
        return table;
    }
}
