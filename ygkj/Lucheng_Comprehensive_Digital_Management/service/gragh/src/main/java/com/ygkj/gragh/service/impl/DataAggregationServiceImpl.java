package com.ygkj.gragh.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ygkj.gragh.enums.ClassificationStatisticType;
import com.ygkj.gragh.enums.EquipmentType;
import com.ygkj.gragh.enums.StationType;
import com.ygkj.gragh.mapper.AttStBaseMapper;
import com.ygkj.gragh.mapper.CameraRecordMapper;
import com.ygkj.gragh.mapper.DataManagementMapper;
import com.ygkj.gragh.mapper.SynRainAndWaterMapper;
import com.ygkj.gragh.model.AttStBase;
import com.ygkj.gragh.model.HikvisionCameraRecord;
import com.ygkj.gragh.service.DataAggregationService;
import com.ygkj.gragh.vo.request.CameraQueryVo;
import com.ygkj.gragh.vo.request.StationRequestVo;
import com.ygkj.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service("dataAggregationService")
public class DataAggregationServiceImpl implements DataAggregationService {

    private SynRainAndWaterMapper rainAndWaterMapper;

    private AttStBaseMapper stationMapper;

    private CameraRecordMapper cameraRecordMapper;

    private DataManagementMapper dataManagementMapper;

    public DataAggregationServiceImpl(@Autowired SynRainAndWaterMapper rainAndWaterMapper,
                                      @Autowired AttStBaseMapper attStBaseMapper,
                                      @Autowired CameraRecordMapper cameraRecordMapper,
                                      @Autowired DataManagementMapper dataManagementMapper) {
        this.rainAndWaterMapper = rainAndWaterMapper;
        this.stationMapper = attStBaseMapper;
        this.cameraRecordMapper = cameraRecordMapper;
        this.dataManagementMapper = dataManagementMapper;
    }

    @Override
    public JSONArray ClassificationStatistics(String type) {
        if (Arrays.asList(ClassificationStatisticType.values()).stream()
                .noneMatch(classificationStatisticType -> classificationStatisticType.type().equals(type))) {
            throw new IllegalArgumentException("统计类型有误！");
        }
        String[] typeStrs = new String[]{"雨情", "水情", "工情"};
        List<String> types = Arrays.asList(typeStrs);
        List<Map<String, Object>> list = stationMapper.stationTypeStatistic(types);
        List<HikvisionCameraRecord> cameras = cameraRecordMapper.selectByCondition(new CameraQueryVo());
        Map<String, Object> cameraMap = new HashMap<>();
        cameraMap.put("type", "视频");
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date now = new Date();
        String end = format.format(now);
        Calendar instance = Calendar.getInstance();
        instance.setTime(now);
        instance.add(Calendar.HOUR_OF_DAY, -1);
        String start = format.format(instance.getTime());
        if (type.equals(ClassificationStatisticType.OFFLINE.type())) {
            //查找离线的数量
            cameraMap.put("num", cameras.size() -
                    cameras.stream().filter(camera -> camera.getStatus() != null && camera.getStatus().intValue() == 0).count());
            //查找近一小时测站离线记录
            List<Map<String, Object>> offlineList = dataManagementMapper.countOfflineNumbers(start, end);
            Map<String, Integer> offlineMap = offlineList.stream()
                    .collect(Collectors.toMap(map -> String.valueOf(map.get("type")), map -> Integer.valueOf(String.valueOf(map.get("num")))));
            list.forEach(map -> {
                String stationType = map.get("type").toString();
                map.put("num", offlineMap.containsKey(stationType) ? offlineMap.get(stationType).intValue() : 0);
            });
        } else if (type.equals(ClassificationStatisticType.ONLINE.type())) {
            //查找在线的数量
            cameraMap.put("num", cameras.size() -
                    cameras.stream().filter(camera -> camera.getStatus() != null && camera.getStatus().intValue() == 1).count());
            //查找近一小时测站离线记录
            List<Map<String, Object>> offlineList = dataManagementMapper.countOfflineNumbers(start, end);
            Map<String, Integer> offlineMap = offlineList.stream()
                    .collect(Collectors.toMap(map -> String.valueOf(map.get("type")), map -> Integer.valueOf(String.valueOf(map.get("num")))));
            list.forEach(map -> {
                String stationType = map.get("type").toString();
                Integer num = Integer.valueOf(map.get("num").toString());
                map.put("num", offlineMap.containsKey(stationType) ? num - offlineMap.get(stationType).intValue() : num);
            });
        } else {
            //总数
            cameraMap.put("num", cameras.size());
        }
        JSONArray result = new JSONArray();
        result.addAll(list);
        result.add(new JSONObject(cameraMap));
        return result;
    }

    @Override
    public JSONObject onlineRateStatistics(String type, String year) {
        if (Arrays.asList(EquipmentType.values()).stream().noneMatch(equipmentType -> equipmentType.type().equals(type))) {
            throw new IllegalArgumentException("统计设备类型有误！");
        }
        JSONObject result;
        if (type.equals(EquipmentType.CAMERA.type())) {
            result = cameraOnlineStatistic(year);
        } else {
            result = stationOnlineStatistic(type, year);
        }
        return result;
    }

    @Override
    public JSONArray cBit() {
        JSONArray result = new JSONArray();
        String[] typeStrs = new String[]{"雨情", "水情", "工情"};
        List<String> types = Arrays.asList(typeStrs);
        List<Map<String, Object>> list = stationMapper.stationTypeStatistic(types);
        List<HikvisionCameraRecord> cameras = cameraRecordMapper.selectByCondition(new CameraQueryVo());
        //查找近一小时测站离线记录
        Date now = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String end = format.format(now);
        Calendar instance = Calendar.getInstance();
        instance.setTime(now);
        instance.add(Calendar.HOUR_OF_DAY, -1);
        String start = format.format(instance.getTime());
        List<Map<String, Object>> offlineList = dataManagementMapper.countOfflineNumbers(start, end);
        Map<String, Integer> offlineMap = offlineList.stream()
                .collect(Collectors.toMap(map -> String.valueOf(map.get("type")), map -> Integer.valueOf(String.valueOf(map.get("num")))));
        list.forEach(map -> {
            String stationType = map.get("type").toString();
            Integer num = Integer.valueOf(map.get("num").toString());
            map.put("rate", offlineMap.containsKey(stationType) ? (num - offlineMap.get(stationType).doubleValue()) / num : 1);
        });
        result.addAll(list);
        JSONObject object = new JSONObject();
        object.put("type", "视频");
        object.put("rate", (double) cameras.stream().filter(camera -> camera.getStatus() != null && camera.getStatus().intValue() == 1).count() / cameras.size());
        result.add(object);
        return result;
    }

    private JSONObject stationOnlineStatistic(String type, String year) {
        JSONObject result = new JSONObject();
        StationRequestVo requestVo = new StationRequestVo();
        if (type.equals(EquipmentType.RAINFALL.type())) {
            //雨情
            requestVo.setIsRainStation(Boolean.TRUE);
        } else if (type.equals(EquipmentType.WATER_LEVEL.type())) {
            //水库水位&河道水位
            String types = Arrays.asList(StationType.RESERVOIR.type(), StationType.RIVER.type()).stream().collect(Collectors.joining(","));
            requestVo.setStationType(types);
        } else if (type.equals(EquipmentType.GATE.type())) {
            //闸门工情
            requestVo.setStationType(StationType.GATE.type());
        }
        List<AttStBase> stations = stationMapper.selectByCondition(requestVo);
        int size = stations.size();
        result.put("total", size);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date now = new Date();
        String end = format.format(now);
        Calendar instance = Calendar.getInstance();
        instance.setTime(now);
        instance.add(Calendar.HOUR_OF_DAY, -1);
        String start = format.format(instance.getTime());
        Map<String, Integer> offlineMap = dataManagementMapper.countOfflineNumbers(start, end).stream()
                .collect(Collectors.toMap(map -> String.valueOf(map.get("type")), map -> Integer.valueOf(String.valueOf(map.get("num")))));
        result.put("onlineNumber", offlineMap.containsKey(type) ? size - offlineMap.get(type).intValue() : size);
        result.put("offlineNumber", offlineMap.containsKey(type) ? offlineMap.get(type).intValue() : 0);
        //图表
        result.put("chart", candidateStatisticChart(type, size));
        return result;
    }

    private JSONObject cameraOnlineStatistic(String year) {
        JSONObject result = new JSONObject();
        List<HikvisionCameraRecord> cameras = cameraRecordMapper.selectByCondition(new CameraQueryVo());
        int size = cameras.size();
        result.put("total", size);
        result.put("onlineNumber", cameras.stream().filter(camera -> camera.getStatus() != null && camera.getStatus().intValue() == 1).count());
        result.put("offlineNumber", cameras.stream().filter(camera -> camera.getStatus() != null && camera.getStatus().intValue() == 0).count());
        result.put("chart", candidateCameraStatisticChart(size));
        return result;
    }

    private JSONArray candidateStatisticChart(String type, Integer total) {
        JSONArray result = new JSONArray();
        List<Map<String, Object>> list = dataManagementMapper.countCurrentYearOfflineNumbersByMonth(type);
        Map<String, Integer> monthMap = list.stream().collect(Collectors.toMap(map -> String.valueOf(map.get("ym")), map -> Integer.valueOf(String.valueOf(map.get("num")))));
        try {
            List<String> months = DateUtils.getMonthBetween(DateUtils.getCurrentYearFirstDay(), new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            for (String month : months) {
                JSONObject object = new JSONObject();
                object.put("month", month);
                if (monthMap.containsKey(month)) {
                    double offlineNumber = monthMap.get(month).doubleValue();
                    double onlineNumber = (total - offlineNumber);
                    object.put("rate", onlineNumber / total);
                    object.put("offlineNumber", offlineNumber);
                    object.put("onlineNumber", onlineNumber);
                } else {
                    object.put("rate", 1d);
                    object.put("offlineNumber", 0);
                    object.put("onlineNumber", total);
                }
                result.add(object);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return result;
    }

    private JSONArray candidateCameraStatisticChart(Integer total) {
        JSONArray result = new JSONArray();
        Calendar instance = Calendar.getInstance();
        instance.setTime(new Date());
        String year = String.valueOf(instance.get(Calendar.YEAR));
        Map<String, Integer> offlineMap = cameraRecordMapper.selectCameraOfflineEachMonth(year)
                .stream().collect(Collectors.toMap(map -> String.valueOf(map.get("year_month")), map -> Integer.valueOf(String.valueOf(map.get("offline_number")))));
        try {
            List<String> months = DateUtils.getMonthBetween(DateUtils.getCurrentYearFirstDay(), new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            for (String month : months) {
                JSONObject object = new JSONObject();
                object.put("month", month);
                if (offlineMap.containsKey(month)) {
                    double offlineNumber = offlineMap.get(month).doubleValue();
                    double onlineNumber = (total - offlineNumber);
                    object.put("rate", onlineNumber / total);
                    object.put("offlineNumber", offlineNumber);
                    object.put("onlineNumber", onlineNumber);
                } else {
                    object.put("rate", 1d);
                    object.put("offlineNumber", 0);
                    object.put("onlineNumber", total);
                }
                result.add(object);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return result;
    }
}
