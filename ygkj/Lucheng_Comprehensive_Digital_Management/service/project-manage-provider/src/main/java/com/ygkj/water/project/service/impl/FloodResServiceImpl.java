package com.ygkj.water.project.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.ygkj.enums.StationType;
import com.ygkj.gragh.model.*;
import com.ygkj.gragh.vo.request.HourRainfallQueryVo;
import com.ygkj.project.model.AttAllRealProjectBase;
import com.ygkj.project.model.StPptnR;
import com.ygkj.project.vo.request.*;
import com.ygkj.project.vo.response.*;
import com.ygkj.utils.Builder;
import com.ygkj.water.project.mapper.AttAdcdBaseMapper;
import com.ygkj.water.project.mapper.FloodResMapper;
import com.ygkj.water.project.mapper.ProjectMapper;
import com.ygkj.water.project.service.FloodResService;
import com.ygkj.water.project.utils.DistanceUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service("floodResService")
public class FloodResServiceImpl implements FloodResService {

    private final static String pptnName = "st_pptn_r_";

    private static DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMM");

    private static DateTimeFormatter normalDtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private static ZoneOffset CN = ZoneOffset.ofHours(8);

    @Resource
    private FloodResMapper floodResMapper;
    @Resource
    private ProjectMapper projectMapper;
    @Resource
    private AttAdcdBaseMapper adcdBaseMapper;

    /**
     * 雨量列表
     *
     * @param reqVo
     * @return
     */
    @Override
    public List<RainResVo> listRain(RainReqVo reqVo) {
        List<RainResVo> result = new ArrayList<>();
        List<String> tableNames = new ArrayList<>();
        LocalDate now = LocalDate.now();
        String format = dtf.format(now);
        tableNames.add(pptnName + format);
        //计算起始时间
        String startTime = getMinusTime(reqVo.getMinutes());
        String otherTable = startTime.substring(0, 7).replace("-", "");
        if (format.equals(otherTable)) {
            tableNames.add(pptnName + otherTable);
        }
        reqVo.setAreaCode(trimAdcd(reqVo.getAreaCode()));
        List<StPptnR> stPptnRS = floodResMapper.listRain(tableNames, startTime);
        //根据条件查询雨量测站
        List<StResVo> list = floodResMapper.listRainStation(reqVo);
        Map<String, StResVo> stcdMap = list.stream().collect(Collectors.toMap(StResVo::getStCode, Function.identity(), (k1, k2) -> k2));
        boolean flag = reqVo.getType() == 1;

        for (StPptnR stPptnR : stPptnRS) {
            String stcd = stPptnR.getStcd();
            if (stcdMap.containsKey(stcd)) {
                RainResVo rainResVo = new RainResVo();
                StResVo stResVo = stcdMap.get(stcd);
                if (flag) {
                    Double drp = stPptnR.getDrp();
                    Double threshold = stResVo.getThreshold();
                    //没超过阈值的不需要
                    if (drp == null || threshold == null || drp < threshold) {
                        continue;
                    }
                }
                rainResVo.setStCode(stcd);
                rainResVo.setStName(stResVo.getStName());
                rainResVo.setDrp(stPptnR.getDrp());
                rainResVo.setThreshold(stResVo.getThreshold());
                rainResVo.setStLong(stResVo.getStLong());
                rainResVo.setStLat(stResVo.getStLat());
                result.add(rainResVo);
            }
        }

        return result;
    }

    private String trimAdcd(String areaCode) {
        if (!StringUtils.isEmpty(areaCode)) {
            if (areaCode.endsWith("00000000")) {
                areaCode = areaCode.substring(0, 8);
            } else if (areaCode.endsWith("000000")) {
                areaCode = areaCode.substring(0, 6);
            }
            areaCode += "%";
        }
        return areaCode;
    }

    private String getMinusTime(Integer minutes) {
        LocalDateTime now = LocalDateTime.now();
        return normalDtf.format(now.minusMinutes(minutes));
    }


    /**
     * 雨量柱状图
     *
     * @param stCode
     * @return
     */
    @Override
    public List<StPptnR> rainHistogram(String stCode, Integer days) {
        if (days == null) {
            days = new Integer(1);
        }
        HourRainfallQueryVo hourRainfallQueryVo = new HourRainfallQueryVo();
        hourRainfallQueryVo.setStationCode(stCode);
        hourRainfallQueryVo.setDays(days);
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMM");
        List<String> tables = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();
        int month = now.getMonthValue();
        tables.add(pptnName + dtf.format(now));
        LocalDateTime yesterday = now.minusDays(days);
        if (yesterday.getMonthValue() != month) {
            tables.add(pptnName + dtf.format(yesterday));
        }
        hourRainfallQueryVo.setStart(Date.from(yesterday.toInstant(CN)));
        hourRainfallQueryVo.setEnd(Date.from(now.toInstant(CN)));
        List<StPptnR> rainfall = floodResMapper.selectHourRainfall(tables, hourRainfallQueryVo);
        return rainfall;
    }

    /**
     * 关联工程列表
     *
     * @param reqVo
     * @return
     */
    @Override
    public List<ResProjectResVo> listResProject(ResProjectReqVo reqVo) {
        Double lon = reqVo.getStLong();
        Double lat = reqVo.getStLat();
        if (lon != null && lat != null) {
            Double dis = reqVo.getDistance();
            //查询所有工程
            List<ResProjectResVo> list = floodResMapper.listResProject(reqVo);
            //去掉距离不符合条件的工程
            if (!CollectionUtils.isEmpty(list)) {
                Iterator<ResProjectResVo> it = list.iterator();
                while (it.hasNext()) {
                    ResProjectResVo data = it.next();
                    if (DistanceUtil.getDistance(data.getLatitude(), data.getLongitude(), lat, lon).doubleValue() > dis) {
                        it.remove();
                    }
                }
                return list;
            }
        }
        return null;
    }

    /**
     * 水位列表
     *
     * @param reqVo
     * @return
     */
    @Override
    public List<WaterResVo> listWater(WaterReqVo reqVo) {
        String projectType = reqVo.getProjectType();
        if (!StringUtils.isEmpty(projectType)) {
            reqVo.setTypeList(Arrays.asList(projectType.split(",").clone()));
        }
        //获取和测站有关的工程
        List<WaterResVo> list = floodResMapper.listWater(reqVo);
        //获取所有水位站的最新数据
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMM");
        String tableTime = dtf.format(LocalDate.now());
        List<WaterResVo> waterLevelList = floodResMapper.listWaterLevel(tableTime, getMinusTime(120));
        if (!CollectionUtils.isEmpty(list) && !CollectionUtils.isEmpty(waterLevelList)) {
//            Map<String, WaterResVo> map = waterLevelList.stream().collect(Collectors.toMap(WaterResVo::getStCode, Function.identity(), (k1, k2) -> k1));
//            Iterator<WaterResVo> it = list.iterator();
//            while (it.hasNext()) {
//                WaterResVo waterResVo = it.next();
//                String stCode = waterResVo.getStCode();
//                BigDecimal warningLevel = waterResVo.getWarningLevel();
//                if (map.containsKey(stCode)) {
//                    WaterResVo level = map.get(stCode);
//                    BigDecimal waterLevel = level.getWaterLevel();
//                    if (isGreater(waterLevel, warningLevel)) {
//                        waterResVo.setTm(level.getTm());
//                        waterResVo.setWaterLevel(level.getWaterLevel());
//                        continue;
//                    }
//                }
//                it.remove();
//            }
            return list;
        }
        return null;
    }

    /**
     * 判断当前水位是否比警戒水位/汛限水位高
     *
     * @param waterLevel   当前水位
     * @param warningLevel 警戒水位/汛限水位
     * @return
     */
    private boolean isGreater(BigDecimal waterLevel, BigDecimal warningLevel) {
        if (warningLevel != null && warningLevel != null) {
            return waterLevel.doubleValue() > warningLevel.doubleValue();
        }
        return false;
    }

//    /**
//     * 水位曲线
//     *
//     * @param stCode
//     * @return
//     */
//    @Override
//    public WaterCurveResVo waterCurve(String stCode) {
//        return null;
//    }
//
//    /**
//     * 视频列表
//     *
//     * @param stCode
//     * @return
//     */
//    @Override
//    public List<CameraResVo> listCamera(String stCode) {
//        return null;
//    }

    /**
     * 台风关联工程列表
     *
     * @param reqVo
     * @return
     */
    @Override
    public List<ResProjectResVo> listTyphoonProject(TyphoonProjectReqVo reqVo) {
        if (reqVo.getType() == 1) {
            return floodResMapper.listResProject(new ResProjectReqVo());
        }
        Double lng = reqVo.getLng();
        Double lat = reqVo.getLat();
        if (lng == null || lat == null) {
            return null;
        }
        //首先获取所有工程
        ResProjectReqVo resProjectReqVo = new ResProjectReqVo();
        resProjectReqVo.setProjectType(reqVo.getProjectType());
        resProjectReqVo.setProjGrad(reqVo.getProjGrad());
        List<ResProjectResVo> list = floodResMapper.listResProject(resProjectReqVo);
        //计算和几个风圈的距离
        Iterator<ResProjectResVo> it = list.iterator();
        while (it.hasNext()) {
            ResProjectResVo resProjectResVo = it.next();
            Double longitude = resProjectResVo.getLongitude();
            Double latitude = resProjectResVo.getLatitude();
            if (longitude == null || latitude == null) {
                it.remove();
            } else {
                //根据象限判断距离
                double distance = latitude >= lat ? longitude <= lng ? reqVo.getNw() : reqVo.getNe() : longitude <= lng ? reqVo.getSw() : reqVo.getSe();
                if (distance < DistanceUtil.getDistance(latitude, longitude, lat, lng).doubleValue()) {
                    it.remove();
                }
            }
        }

        return list;
    }

    /**
     * 台风关联工程列表
     *
     * @param reqVo
     * @return
     */
    @Override
    public List<? extends AttAllRealProjectBase> listTyphoonAllRealProject(TyphoonProjectReqVo reqVo) {
        if (reqVo.getType() == 1) {
            return projectMapper.listTenProject(new AllRealProjectReqVo());
        }
        Double lng = reqVo.getLng();
        Double lat = reqVo.getLat();
        if (lng == null || lat == null) {
            return Collections.emptyList();
        }
        if (reqVo.getNe() == null) {
            reqVo.setNe(0D);
        }
        if (reqVo.getSw() == null) {
            reqVo.setSw(0D);
        }
        if (reqVo.getNw() == null) {
            reqVo.setNw(0D);
        }
        if (reqVo.getSe() == null) {
            reqVo.setSe(0D);
        }
        //首先获取所有工程
        AllRealProjectReqVo projectReqVo = new AllRealProjectReqVo();
        projectReqVo.setProjectType(reqVo.getProjectType());
//        resProjectReqVo.setEngGrad(reqVo.getProjGrad());
//        resProjectReqVo.setEngGrad(reqVo.getProjGrad());
        List<AttAllRealProjectBase> list = projectMapper.listTenProject(projectReqVo);
        //计算和几个风圈的距离
        Iterator<AttAllRealProjectBase> it = list.iterator();
        while (it.hasNext()) {
            AttAllRealProjectBase resProjectResVo = it.next();

            if (resProjectResVo.getLongitude() == null || resProjectResVo.getLatitude() == null) {
                it.remove();
            } else {
                Double longitude = resProjectResVo.getLongitude().doubleValue();
                Double latitude = resProjectResVo.getLatitude().doubleValue();
                //根据象限判断距离
                double distance = latitude >= lat ? longitude <= lng ? reqVo.getNw() : reqVo.getNe() : longitude <= lng ? reqVo.getSw() : reqVo.getSe();
                if (distance < DistanceUtil.getDistance(latitude, longitude, lat, lng).doubleValue()) {
                    it.remove();
                }
            }
        }
        if (reqVo.getWithProjInfo() != null && reqVo.getWithProjInfo()) {
            List<TyphoonProjectResVo> result = new ArrayList<>(list.size());
            Map<String, List<TyphoonProjectResVo>> groupMap = list.stream().map(o -> {
                TyphoonProjectResVo target = new TyphoonProjectResVo();
                BeanUtils.copyProperties(o, target);
                return target;
            }).collect(Collectors.groupingBy(TyphoonProjectResVo::getProjectType));
            groupMap.forEach((projectType, projectList) -> {
                List<String> codes = projectList.stream().map(TyphoonProjectResVo::getProjectCode).filter(org.apache.commons.lang3.StringUtils::isNotBlank).collect(Collectors.toList());
                switch (projectType) {
                    case "水库": {
                        if (!CollectionUtils.isEmpty(codes)) {
                            List<AttResBase> tempList = projectMapper.selectReservoirByCondition(Builder
                                    .of(ProjectManageQueryVo::new)
                                    .with(ProjectManageQueryVo::setCodes, codes)
                                    .build());
                            if (!CollectionUtils.isEmpty(tempList)) {
                                Map<String, AttResBase> tempMap = tempList.stream().collect(Collectors.toMap(AttResBase::getResCode, Function.identity()));
                                for (TyphoonProjectResVo temp : projectList) {
                                    temp.setRsvrInfo(tempMap.get(temp.getProjectCode()));
                                }
                            }
                        }
                    }
                    break;
                    case "山塘": {
                        if (!CollectionUtils.isEmpty(codes)) {
                            List<AttMnpdBase> tempList = projectMapper.selectMountainPoolByCondition(Builder
                                    .of(ProjectManageQueryVo::new)
                                    .with(ProjectManageQueryVo::setCodes, codes)
                                    .build());
                            if (!CollectionUtils.isEmpty(tempList)) {
                                Map<String, AttMnpdBase> tempMap = tempList.stream().collect(Collectors.toMap(AttMnpdBase::getMnpdCode, Function.identity()));
                                for (TyphoonProjectResVo temp : projectList) {
                                    temp.setMnpdInfo(tempMap.get(temp.getProjectCode()));
                                }
                            }
                        }
                    }
                    break;
                    case "堤防": {
                        if (!CollectionUtils.isEmpty(codes)) {
                            List<AttDikeBase> tempList = projectMapper.selectDikeByCondition(Builder
                                    .of(ProjectManageQueryVo::new)
                                    .with(ProjectManageQueryVo::setCodes, codes)
                                    .build());
                            if (!CollectionUtils.isEmpty(tempList)) {
                                Map<String, AttDikeBase> tempMap = tempList.stream().collect(Collectors.toMap(AttDikeBase::getDikeCode, Function.identity()));
                                for (TyphoonProjectResVo temp : projectList) {
                                    temp.setDikeInfo(tempMap.get(temp.getProjectCode()));
                                }
                            }
                        }
                    }
                    break;
                    case "海塘": {
                        if (!CollectionUtils.isEmpty(codes)) {
                            List<AttSeawallBase> tempList = projectMapper.selectSeawallByCondition(Builder
                                    .of(ProjectManageQueryVo::new)
                                    .with(ProjectManageQueryVo::setCodes, codes)
                                    .build());
                            if (!CollectionUtils.isEmpty(tempList)) {
                                Map<String, AttSeawallBase> tempMap = tempList.stream().collect(Collectors.toMap(AttSeawallBase::getSeawallCode, Function.identity()));
                                for (TyphoonProjectResVo temp : projectList) {
                                    temp.setSeawallInfo(tempMap.get(temp.getProjectCode()));
                                }
                            }
                        }
                    }
                    break;
                    case "泵站": {
                        if (!CollectionUtils.isEmpty(codes)) {
                            List<AttPustBase> tempList = projectMapper.selectPumpByCondition(Builder
                                    .of(ProjectManageQueryVo::new)
                                    .with(ProjectManageQueryVo::setCodes, codes)
                                    .build());
                            if (!CollectionUtils.isEmpty(tempList)) {
                                Map<String, AttPustBase> tempMap = tempList.stream().collect(Collectors.toMap(AttPustBase::getPustCode, Function.identity()));
                                for (TyphoonProjectResVo temp : projectList) {
                                    temp.setPustInfo(tempMap.get(temp.getProjectCode()));
                                }
                            }
                        }
                    }
                    break;
                    case "水闸": {
                        if (!CollectionUtils.isEmpty(codes)) {
                            List<AttWagaBase> tempList = projectMapper.selectWagaByCondition(Builder
                                    .of(ProjectManageQueryVo::new)
                                    .with(ProjectManageQueryVo::setCodes, codes)
                                    .build());
                            if (!CollectionUtils.isEmpty(tempList)) {
                                Map<String, AttWagaBase> tempMap = tempList.stream().collect(Collectors.toMap(AttWagaBase::getWagaCode, Function.identity()));
                                for (TyphoonProjectResVo temp : projectList) {
                                    temp.setWagaInfo(tempMap.get(temp.getProjectCode()));
                                }
                            }
                        }
                    }
                    break;
                    case "闸站": {
                        if (!CollectionUtils.isEmpty(codes)) {
                            List<AttGateBase> tempList = projectMapper.selectGateProjectByCondition(Builder
                                    .of(ProjectManageQueryVo::new)
                                    .with(ProjectManageQueryVo::setCodes, codes)
                                    .build());
                            if (!CollectionUtils.isEmpty(tempList)) {
                                Map<String, AttGateBase> tempMap = tempList.stream().collect(Collectors.toMap(AttGateBase::getGateCode, Function.identity()));
                                for (TyphoonProjectResVo temp : projectList) {
                                    temp.setGateInfo(tempMap.get(temp.getProjectCode()));
                                }
                            }
                        }
                    }
                    break;
                    case "灌区": {
                        if (!CollectionUtils.isEmpty(codes)) {
                            List<AttIrrBase> tempList = projectMapper.selectIrrProjectByCondition(Builder
                                    .of(ProjectManageQueryVo::new)
                                    .with(ProjectManageQueryVo::setCodes, codes)
                                    .build());
                            if (!CollectionUtils.isEmpty(tempList)) {
                                Map<String, AttIrrBase> tempMap = tempList.stream().collect(Collectors.toMap(AttIrrBase::getIrrCode, Function.identity()));
                                for (TyphoonProjectResVo temp : projectList) {
                                    temp.setIrrInfo(tempMap.get(temp.getProjectCode()));
                                }
                            }
                        }
                    }
                    break;
                    case "农饮水": {
                        if (!CollectionUtils.isEmpty(codes)) {
                            List<AttCwsBase> tempList = projectMapper.selectCwsProjectByCondition(Builder
                                    .of(ProjectManageQueryVo::new)
                                    .with(ProjectManageQueryVo::setCodes, codes)
                                    .build());
                            if (!CollectionUtils.isEmpty(tempList)) {
                                Map<String, AttCwsBase> tempMap = tempList.stream().collect(Collectors.toMap(AttCwsBase::getCwsCode, Function.identity()));
                                for (TyphoonProjectResVo temp : projectList) {
                                    temp.setCwsInfo(tempMap.get(temp.getProjectCode()));
                                }
                            }
                        }
                    }
                    break;
                    case "水电站": {
                        if (!CollectionUtils.isEmpty(codes)) {
                            List<AttHystBase> tempList = projectMapper.selectHydristByCondition(Builder
                                    .of(ProjectManageQueryVo::new)
                                    .with(ProjectManageQueryVo::setCodes, codes)
                                    .build());
                            if (!CollectionUtils.isEmpty(tempList)) {
                                Map<String, AttHystBase> tempMap = tempList.stream().collect(Collectors.toMap(AttHystBase::getHystCode, Function.identity()));
                                for (TyphoonProjectResVo temp : projectList) {
                                    temp.setHystInfo(tempMap.get(temp.getProjectCode()));
                                }
                            }
                        }
                    }
                    break;
                    default:
                        break;
                }
                result.addAll(projectList);
            });
            return result;
        }
        return list;
    }

    @Override
    public JSONObject listTyphoonAllRealProjectNewestVersion(TyphoonProjectReqVo reqVo) {
        JSONObject result = new JSONObject();
        List<? extends AttAllRealProjectBase> projects = listTyphoonAllRealProject(reqVo);
        //根据工程类型分组
        Map<String, ? extends List<? extends AttAllRealProjectBase>> projectTypeMap = projects.stream().collect(Collectors.groupingBy(AttAllRealProjectBase::getProjectType));
        result.put("groupByProjectType", projectTypeMap.entrySet().stream().map(entry -> {
            JSONObject object = new JSONObject();
            object.put("projectType", entry.getKey());
            object.put("projects", entry.getValue());
            return object;
        }).collect(Collectors.toList()));
        //根据行政区划分组
        Map<String, ? extends List<? extends AttAllRealProjectBase>> areaMap = projects.stream()
                .filter(project -> StringUtils.isNotBlank(project.getAdcd()))
                .collect(Collectors.groupingBy(project -> {
                    return project.getAdcd().substring(0, 6).concat("000000");
                }));
        Map<String, String> adcdMap = adcdBaseMapper.selectWZCounty().stream().collect(Collectors.toMap(AttAdcdBase::getAdcd, AttAdcdBase::getAdnm));
        result.put("groupByAdcd", areaMap.entrySet().stream()
                .filter(entry -> adcdMap.containsKey(entry.getKey()))
                .map(entry -> {
                    JSONObject object = new JSONObject();
                    object.put("areaName", adcdMap.get(entry.getKey()));
                    object.put("projects", entry.getValue());
                    return object;
                }).collect(Collectors.toList()));
        return result;
    }

    /**
     * 台风关联工程统计
     *
     * @param reqVo
     * @return
     */
    @Override
    public Integer countTyphoonAllRealProject(TyphoonProjectReqVo reqVo) {
        return listTyphoonAllRealProject(reqVo).size();
    }

    @Override
    public JSONObject reservoirWaterLevel(String code) {
        JSONObject result = new JSONObject();
        List<AttStBase> list = floodResMapper.selectStationsByProjectCode(code);
        if (!list.isEmpty()) {
            String stationCode = list.get(0).getStCode();
            String table = StationType.RESERVOIR.table();
            Calendar instance = Calendar.getInstance();
            int year = instance.get(Calendar.YEAR);
            int month = instance.get(Calendar.MONTH) + 1;
            String zero = month < 10 ? "0" : "";
            table = table.concat("" + year + zero + month);
            StRsvrR stRsvrR = floodResMapper.selectNewestReserviorWaterLevelFromTable(table, stationCode);
            result = (JSONObject) JSONObject.toJSON(stRsvrR);
        }
        return result;
    }
}
