package com.ygkj.water.project.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ygkj.enums.StationType;
import com.ygkj.flood.model.AttTeamBase;
import com.ygkj.flood.model.AttWarehouseBase;
import com.ygkj.gragh.model.AttStBase;
import com.ygkj.gragh.vo.request.ProjectRangeQueryVo;
import com.ygkj.project.model.HikvisionCameraRecord;
import com.ygkj.project.vo.request.HikCameraQueryReqVo;
import com.ygkj.project.vo.response.HikCameraResVo;
import com.ygkj.project.vo.response.ProjectResVo;
import com.ygkj.utils.DistanceUtil;
import com.ygkj.water.project.mapper.*;
import com.ygkj.water.project.service.PeripheralAnalysisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
public class PeripheralAnalysisServiceImpl implements PeripheralAnalysisService {

    private ProjectMapper projectMapper;

    private StationMapper stationMapper;

    private HikCameraMapper hikCameraMapper;

    private FloodPointMapper floodPointMapper;

    private EmergencySupportMapper emergencySupportMapper;

    public PeripheralAnalysisServiceImpl(@Autowired ProjectMapper projectMapper,
                                         @Autowired StationMapper stationMapper,
                                         @Autowired HikCameraMapper hikCameraMapper,
                                         @Autowired FloodPointMapper floodPointMapper,
                                         @Autowired EmergencySupportMapper emergencySupportMapper) {
        this.projectMapper = projectMapper;
        this.stationMapper = stationMapper;
        this.hikCameraMapper = hikCameraMapper;
        this.floodPointMapper = floodPointMapper;
        this.emergencySupportMapper = emergencySupportMapper;
    }

    @Override
    public JSONArray analysis(ProjectRangeQueryVo queryVo) {
        JSONArray result = new JSONArray();
        //水利监控
        JSONObject monitoring = waterMonitoring(queryVo);
        //水利工程
        JSONObject projects = projects(queryVo);
        //应急救援
        JSONObject emergencyRescue = emergencyRescue(queryVo);
        //全部
        result.add(total(monitoring, projects, emergencyRescue));
        result.add(monitoring);
        result.add(projects);
        result.add(emergencyRescue);
        return result;
    }

    private JSONObject total(JSONObject... objects) {
        JSONObject result = new JSONObject();
        result.put("name", "全部");
        result.put("children", Collections.emptyList());
        AtomicInteger totalNumber = new AtomicInteger(0);
        JSONArray dataArray = new JSONArray();
        for (JSONObject jsonObject : objects) {
            JSONArray children = jsonObject.getJSONArray("children");
            children.forEach(o -> {
                JSONObject object = (JSONObject) o;
                String name = object.getString("name");
                JSONArray data = object.getJSONArray("data");
                List<JSONObject> jsonObjects = data.toJavaList(JSONObject.class);
                jsonObjects.forEach(dataObject -> {
                    dataObject.put("analysisType", name);
                });
                totalNumber.addAndGet(jsonObjects.size());
                dataArray.addAll(jsonObjects);
            });
        }
        result.put("data", dataArray);
        result.put("number", totalNumber.intValue());
        return result;
    }

    @Override
    public Collection candidateRecords(String codes, String type) {
        return PeripheralAnalysisServiceManager.candidateRecords(codes, type);
    }

    private JSONObject waterMonitoring(ProjectRangeQueryVo queryVo) {
        List<AttStBase> stations = stationMapper.stations();
        JSONObject result = new JSONObject();
        result.put("name", "水利监控");
        int distance = queryVo.getDistance().intValue();
        double longitude = queryVo.getLongitude();
        double latitude = queryVo.getLatitude();
        //找出指定范围内的所有测站
        stations = stations.stream().filter(station ->
                DistanceUtil.getDistance(station.getStLat().doubleValue(), station.getStLong().doubleValue(),
                        latitude, longitude).intValue() < distance)
                .collect(Collectors.toList());
        stations.forEach(station -> {
            station.setLongitude(station.getStLong().toString());
            station.setLatitude(station.getStLat().toString());
        });
        Map<String, List<AttStBase>> map = stations.stream().collect(Collectors.groupingBy(AttStBase::getStType));
        JSONArray array = new JSONArray();
        JSONArray allPoint = new JSONArray();
        map.forEach((type, list) -> {
            JSONObject object = new JSONObject();
            String name = "";
            switch (StationType.match(type)) {
                case RAINFALL:
                    name = "雨情";
                    break;
                case GATE:
                    name = "工情";
                    break;
                case RESERVOIR:
                    name = "水库水位";
                    break;
                case RIVER:
                    name = "河道水位";
                    break;
                case TIDE:
                    name = "潮位";
                    break;
                default:
                    break;
            }
            object.put("name", name);
            object.put("number", list.size());
            object.put("codes", list.stream().map(AttStBase::getStCode).collect(Collectors.joining(",")));
            object.put("data", list);
            object.put("children", null);
            array.add(object);
        });
        //摄像机
        List<HikCameraResVo> cameras = hikCameraMapper.selectByCondition(new HikCameraQueryReqVo());
        cameras = cameras.stream().filter(camera ->
                DistanceUtil.getDistance(Double.parseDouble(camera.getLatitude()), Double.parseDouble(camera.getLongitude())
                        , latitude, longitude).intValue() < distance).collect(Collectors.toList());
        if (!cameras.isEmpty()) {
            JSONObject object = new JSONObject();
            object.put("name", "视频");
            object.put("number", cameras.size());
            object.put("codes", cameras.stream().map(HikvisionCameraRecord::getCameraIndexCode).collect(Collectors.joining(",")));
            object.put("data", cameras);
            object.put("children", null);
            array.add(object);
        }
       /* //水质
        List<AttBswqBase> waterQualityStations = stationMapper.waterQualityStations();
        waterQualityStations = waterQualityStations.stream().filter(station ->
                DistanceUtil.getDistance(station.getLatitude().doubleValue(), station.getLongitude().doubleValue()
                        , latitude, longitude).intValue() < distance).collect(Collectors.toList());
        if (!waterQualityStations.isEmpty()) {
            JSONObject qualityObject = new JSONObject();
            qualityObject.put("name", "水质");
            qualityObject.put("number", waterQualityStations.size());
            qualityObject.put("codes", waterQualityStations.stream().map(AttBswqBase::getCode).collect(Collectors.joining(",")));
            qualityObject.put("data", waterQualityStations);
            qualityObject.put("children", null);
            array.add(qualityObject);
        }
        //积水
        List<FloodPoint> points = floodPointMapper.floodPoints();
        points = points.stream().filter(point ->
                DistanceUtil.getDistance(Double.parseDouble(point.getLatitude()), Double.parseDouble(point.getLongitude())
                        , latitude, longitude).intValue() < distance).collect(Collectors.toList());
        if (!points.isEmpty()) {
            JSONObject floodObject = new JSONObject();
            floodObject.put("name", "积水");
            floodObject.put("number", points.size());
            floodObject.put("codes", points.stream().map(point -> String.valueOf(point.getId())).collect(Collectors.joining(",")));
            floodObject.put("data", points);
            floodObject.put("children", null);
            array.add(floodObject);
        }*/
        result.put("number", array.stream().mapToInt(o -> ((JSONObject) o).getIntValue("number")).sum());
        result.put("children", array);
        result.put("data", Collections.emptyList());
        return result;
    }

    private JSONObject projects(ProjectRangeQueryVo queryVo) {
        JSONObject result = new JSONObject();
        result.put("name", "水利工程");
        JSONArray array = new JSONArray();
        List<ProjectResVo> projects = projectMapper.selectAllProject();
        int distance = queryVo.getDistance().intValue();
        double longitude = queryVo.getLongitude();
        double latitude = queryVo.getLatitude();
        //筛选范围内的工程
        projects = projects.stream().filter(project -> !project.getProjectType().equals("其他"))
                .filter(project -> DistanceUtil.getDistance(project.getLatitude().doubleValue(), project.getLongitude().doubleValue()
                        , latitude, longitude).intValue() < distance).collect(Collectors.toList());
        result.put("number", projects.size());
        Map<String, List<ProjectResVo>> map = projects.stream().collect(Collectors.groupingBy(ProjectResVo::getProjectType));
        map.forEach((projectType, list) -> {
            JSONObject object = new JSONObject();
            object.put("name", projectType);
            object.put("number", list.size());
            object.put("codes", list.stream().map(ProjectResVo::getProjectCode).collect(Collectors.joining(",")));
            object.put("data", list);
            object.put("children", null);
            array.add(object);
        });
        result.put("data", Collections.emptyList());
        result.put("children", array);
        return result;
    }

    private JSONObject emergencyRescue(ProjectRangeQueryVo queryVo) {
        JSONObject result = new JSONObject();
        result.put("name", "应急救援");
        JSONArray array = new JSONArray();
        int distance = queryVo.getDistance().intValue();
        double longitude = queryVo.getLongitude();
        double latitude = queryVo.getLatitude();
        List<AttWarehouseBase> warehouseBases = emergencySupportMapper.wareHouses();
        warehouseBases = warehouseBases.stream().filter(attWarehouseBase ->
                DistanceUtil.getDistance(Double.parseDouble(attWarehouseBase.getLatitude()), Double.parseDouble(attWarehouseBase.getLongitude())
                        , latitude, longitude).intValue() <= distance).collect(Collectors.toList());
        if (!warehouseBases.isEmpty()) {
            JSONObject object = new JSONObject();
            object.put("name", "仓库");
            object.put("number", warehouseBases.size());
            object.put("codes", warehouseBases.stream().map(AttWarehouseBase::getId).collect(Collectors.joining(",")));
            object.put("children", null);
            object.put("data", warehouseBases);
            array.add(object);
        }
        List<AttTeamBase> teams = emergencySupportMapper.teams();
        teams = teams.stream().filter(team ->
                DistanceUtil.getDistance(Double.parseDouble(team.getLatitude()), Double.parseDouble(team.getLongitude())
                        , latitude, longitude).intValue() <= distance).collect(Collectors.toList());
        if (!teams.isEmpty()) {
            JSONObject object = new JSONObject();
            object.put("name", "队伍");
            object.put("number", teams.size());
            object.put("codes", teams.stream().map(AttTeamBase::getId).collect(Collectors.joining(",")));
            object.put("children", null);
            object.put("data", teams);
            array.add(object);
        }
        result.put("number", array.stream().mapToInt(o -> ((JSONObject) o).getIntValue("number")).sum());
        result.put("data", Collections.emptyList());
        result.put("children", array);
        return result;
    }
}
