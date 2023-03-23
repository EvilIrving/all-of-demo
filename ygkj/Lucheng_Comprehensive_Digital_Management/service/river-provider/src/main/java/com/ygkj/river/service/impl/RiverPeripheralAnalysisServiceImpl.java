package com.ygkj.river.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ygkj.river.enums.RiverPeripheralAnalysisQueryTableEnum;
import com.ygkj.river.mapper.AttRiverPeripheralAnalysisMapper;
import com.ygkj.river.service.RiverPeripheralAnalysisService;
import com.ygkj.entity.StatisticEntity;
import com.ygkj.enums.ProjectType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service("riverPeripheralAnalysis")
public class RiverPeripheralAnalysisServiceImpl implements RiverPeripheralAnalysisService {

    private AttRiverPeripheralAnalysisMapper riverPeripheralAnalysisMapper;

    public RiverPeripheralAnalysisServiceImpl(@Autowired AttRiverPeripheralAnalysisMapper riverPeripheralAnalysisMapper) {
        this.riverPeripheralAnalysisMapper = riverPeripheralAnalysisMapper;
    }

    @Override
    public JSONArray peripheralAnalysisTree(Integer riverSegId, Integer distance) {
        JSONArray result = new JSONArray();
        List<StatisticEntity> list = riverPeripheralAnalysisMapper.RiverAnalysisObjectTypeStatistic(riverSegId, distance);
        Set<String> set = Arrays.stream(ProjectType.values()).map(ProjectType::type).collect(Collectors.toSet());
        List<StatisticEntity> projectList = list.stream().filter(statisticEntity -> set.contains(statisticEntity.getType())).collect(Collectors.toList());
        JSONObject object = new JSONObject();
        object.put("type", "水利工程");
        object.put("num", projectList.stream().mapToInt(StatisticEntity::getNum).sum());
        object.put("children", projectList);
        result.add(object);
        List<StatisticEntity> others = list.stream().filter(statisticEntity -> !set.contains(statisticEntity.getType())).collect(Collectors.toList());
        for (StatisticEntity entity : others) {
            result.add(JSONObject.toJSON(entity));
        }
        return result;
    }

    @Override
    public List<Map<String, Object>> selectPoints(String type, Integer riverSegId, Integer distance) {
        RiverPeripheralAnalysisQueryTableEnum tableEnum = RiverPeripheralAnalysisQueryTableEnum.judge(type);
        if (null == tableEnum) {
            return Collections.emptyList();
        }
        List<String> ids = riverPeripheralAnalysisMapper.riverAnalysisObjectIds(riverSegId, type, distance);
        return riverPeripheralAnalysisMapper.selectPoints(tableEnum.getTable(), tableEnum.getPrimary(), ids);
    }
}
