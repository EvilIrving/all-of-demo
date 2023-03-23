package com.ygkj.flood.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.ygkj.flood.controller.AttWarehouseController;
import com.ygkj.flood.feign.WaterAndRainClient;
import com.ygkj.flood.mapper.*;
import com.ygkj.flood.model.AttWarehouseAdd;
import com.ygkj.flood.model.HazardPoint;
import com.ygkj.flood.model.StPptnR;
import com.ygkj.flood.service.EmergencySupportService;
import com.ygkj.flood.service.WaterAndRainService;
import com.ygkj.flood.vo.request.AttTeamBasePageQueryVo;
import com.ygkj.flood.vo.request.EmergencyDispatchRequestVo;
import com.ygkj.flood.vo.request.MaterialDispatchRequestVo;
import com.ygkj.flood.vo.request.SynRainRequestVo;
import com.ygkj.flood.vo.response.AttTeamBaseResVo;
import com.ygkj.flood.vo.response.AttWarehouseResVo;
import com.ygkj.utils.CollectionUtils;
import com.ygkj.utils.DistanceUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service("emergencySupportService")
public class EmergencySupportServiceImpl implements EmergencySupportService {

    @Autowired
    private StationMapper stationMapper;

    @Autowired
    private AttTeamBaseMapper teamBaseMapper;

    @Autowired
    private AttMaterialBaseMapper materialBaseMapper;

    @Autowired
    private HazardPointMapper hazardPointMapper;

    @Autowired
    WaterAndRainClient waterAndRainClient;

    @Autowired
    private AttWarehouseAddMapper attWarehouseAddMapper;


    @Override
    public JSONObject emergencyDispatch(EmergencyDispatchRequestVo requestVo) {
        JSONObject result = new JSONObject();
//        WarningStationResVo station = stationMapper.load(requestVo.getStationCode());
//        result.put("station", station);
        AttTeamBasePageQueryVo queryVo = new AttTeamBasePageQueryVo();
        queryVo.setName(requestVo.getTeamName());
        List<AttTeamBaseResVo> list = teamBaseMapper.selectByCondition(queryVo);
        List<JSONObject> objects = new ArrayList<>();
        double latitude = Double.parseDouble(requestVo.getLatitude());
        double longitude = Double.parseDouble(requestVo.getLongitude());
        list = list.stream().filter(vo -> StringUtils.isNotBlank(vo.getLatitude()) &&
                StringUtils.isNotBlank(vo.getLongitude())).collect(Collectors.toList());
        for (AttTeamBaseResVo vo : list) {
            JSONObject jsonObject = (JSONObject) JSON.toJSON(vo);
            BigDecimal distance = DistanceUtil.getDistance(latitude, longitude,
                    Double.parseDouble(vo.getLatitude()), Double.parseDouble(vo.getLongitude()));
            jsonObject.put("distance", distance.doubleValue());
            objects.add(jsonObject);
        }
        //由近到远排序
        objects.sort(Comparator.comparing(jsonObject -> jsonObject.getDouble("distance")));
        result.put("teams", objects);
        return result;
    }

    @Override
    public List<String> materials() {

        List<JSONObject> strList = materialBaseMapper.findMaterials();
        List<String> res = new ArrayList<>();
        for (JSONObject jsonObject : strList) {
            String str = jsonObject.getString("name") + "(" + jsonObject.getString("num") + ")";
            res.add(str);
        }
        return res;

//        return materialBaseMapper.selectMaterials();
    }


    @Override
    public JSONObject intelligentAnalysis(String teamId, List<MaterialDispatchRequestVo> list) {
        JSONObject result = new JSONObject();
        AttTeamBaseResVo teamBase = teamBaseMapper.load(teamId);
        result.put("teamInfo", teamBase);

//        for (MaterialDispatchRequestVo vo : list) {
//            List<AttWarehouseAdd> materialList = attWarehouseAddMapper.selectList(Wrappers.lambdaQuery(AttWarehouseAdd.class)
//                    .eq(AttWarehouseAdd::getMaterialName, vo.getMaterialName())
//                    .eq(AttWarehouseAdd::getDelFlag, 0));
//
//        }
        for (MaterialDispatchRequestVo vo : list) {
            List<String> strs = Arrays.asList(vo.getMaterialName().split("\\("));
            vo.setMaterialName(strs.get(0));
        }
        List<AttWarehouseResVo> warehouses = materialBaseMapper.matchMaterialWarehouseAdd(list).stream()
                .filter(vo -> StringUtils.isNotBlank(vo.getLatitude()) && StringUtils.isNotBlank(vo.getLongitude())).collect(Collectors.toList());;

//        List<AttWarehouseResVo> warehouses = materialBaseMapper.matchMaterialWarehouses(list).stream()
//                .filter(vo -> StringUtils.isNotBlank(vo.getLatitude()) && StringUtils.isNotBlank(vo.getLongitude())).collect(Collectors.toList());
        if (warehouses.size() > 2) {
            warehouses = warehouses.subList(0, 2);
        }
        result.put("warehouses", warehouses);
        return result;
    }

    @Resource
    RainfallAnalysisMapper rainfallAnalysisMapper;

    @Autowired
    WaterAndRainService waterAndRainService;

    /**
     * 只查询雨水超警的测雨站点
     *
     * @author xq
     * @date 2021-01-26
     */
    @Override
    public List<StPptnR> rainfallWarningInfo(SynRainRequestVo synRainRequestVo) {
        List<StPptnR> tempResult = waterAndRainService.rainfall(synRainRequestVo);
        if (synRainRequestVo.getMinutes() == null) {
            return tempResult;
        }
        List<Map<String, Object>> warningThresholdList = rainfallAnalysisMapper.selectRainStationWarningThresholdByRainfallPeroid(
                transferMinutesToRainfallPeriod(synRainRequestVo.getMinutes()));
        if (CollectionUtils.isBlank(warningThresholdList)) {
            return tempResult;
        }
        Map<String, Double> warningThresholdMap = warningThresholdList.stream().collect(Collectors.toMap(map -> (String) map.get("st_code"), map -> {
            Object temp = map.get("threshold");
            if (temp != null) {
                return ((BigDecimal) temp).doubleValue();
            } else {
                return null;
            }
        }));
        // 过滤掉没有超警的站点
        List<StPptnR> result = tempResult.stream().filter(stPptnR -> {
            Double threshold = warningThresholdMap.get(stPptnR.getStcd());
            if (stPptnR.getDrp() != null && threshold != null) {
                return stPptnR.getDrp() >= threshold;
            } else {
                return false;
            }
        }).collect(Collectors.toList());
        return result;
    }

    @Override
    public List<HazardPoint> hazardPoints() {
        return hazardPointMapper.hazardPoints();
    }

    @Override
    public JSONObject emergencySupportCount() {
        List<Map<String, Object>> teamCount = teamBaseMapper.teamCount();
        List<Map<String, Object>> emergencyCount = materialBaseMapper.emergencyCount();
        JSONObject result = new JSONObject();
        result.put("materialTypes", emergencyCount.size());
        result.put("materialNumber", emergencyCount.stream().mapToLong(map -> (Long) map.get("num")).sum());
        result.put("teams", teamCount.size());
        result.put("teamMembers", teamCount.stream().mapToInt(map -> ((BigDecimal) map.get("existing_number")).intValue()).sum());
        return result;
    }

    private String transferMinutesToRainfallPeriod(int paramMinutes) {
        /**
         * 前端定义的时间
         * 10: "10min",
         * 30: "30min",
         * 60: "1h",
         * 180: "3h",
         * 360: "6h",
         * 1440: "24h",
         */
        int hours = paramMinutes / 60;
        int minutes = paramMinutes % 60;
        if (hours == 0 && minutes > 0) {
            return minutes + "min";
        } else if (hours > 0 && minutes >= 0) {
            return hours + "h";
        } else {
            return paramMinutes + "min";
        }

//        if (hours == 0 && minutes > 0) {
//            // 说明时间不满一分钟
//            return minutes + "min";
//        }else if (hours == 1 && minutes == 0) {
//            // 1h的threshold_period 是 按60min存储的
//            return paramMinutes + "min";
//        } else if (hours > 0 && minutes >= 0) {
//            // 说明时间不一定是小时整，直接取小时数
//            return hours + "h";
//        } else {
//            return paramMinutes + "min";
//        }
    }
}
