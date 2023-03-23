package com.ygkj.gragh.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.ygkj.enums.EngScalEnum;
import com.ygkj.gragh.enums.StationType;
import com.ygkj.gragh.feign.ProjectManageClient;
import com.ygkj.gragh.mapper.WaterRainAnalysisMapper;
import com.ygkj.gragh.model.AttStBase;
import com.ygkj.gragh.model.StPptnR;
import com.ygkj.gragh.service.SynWaterAndRainService;
import com.ygkj.gragh.service.WaterRainAnalysisService;
import com.ygkj.gragh.vo.request.SynRainRequestVo;
import com.ygkj.gragh.vo.request.SynWaterRequestVo;
import com.ygkj.gragh.vo.response.WarnInfoResVo;
import com.ygkj.project.vo.request.RsvrStationWaterReqVo;
import com.ygkj.project.vo.response.RsvrStationWaterResVo;
import com.ygkj.utils.Builder;
import com.ygkj.utils.CollectionUtils;
import com.ygkj.utils.StringUtils;
import com.ygkj.vo.PageVO;
import com.ygkj.water.model.CommonCode;
import com.ygkj.water.model.CommonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author xq
 * @Date 2021/2/22
 */
@Service
public class WaterRainAnalysisServiceImpl implements WaterRainAnalysisService {

    @Resource
    WaterRainAnalysisMapper waterRainAnalysisMapper;

    @Resource
    SynWaterAndRainService waterAndRainService;

    @Autowired
    ProjectManageClient projectManageClient;

    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMM");

    @Override
    public Map<String, Object> selectWaterRainSimpleReport() {

        Map<String, Object> result = new HashMap<>();
        result.putAll(rainfallOverview());
        result.putAll(riverOverview());
        result.putAll(largeMidResOverview());
        // 天气直接调之前的接口算了
        // 服务 MeteorologicalVisualization
        // weatherPredict/weatherPredict
        return result;
    }

    @Override
    public Map<String, Object> rainfallOverview() {
        Map<String, Object> result = new HashMap<>();
        // 查询最大雨量站点
        Set<String> tables = new HashSet<>(2);
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime oneDayAgo = now.plusDays(-1);
        tables.clear();
        tables.add(StationType.RAINFALL.table() + dtf.format(now));
        tables.add(StationType.RAINFALL.table() + dtf.format(oneDayAgo));
        StPptnR maxRainfall = waterRainAnalysisMapper.selectMaxRainfallOver24Hours(new ArrayList<>(tables));
        if (maxRainfall != null) {
            List<Map<String, String>> tempList = waterRainAnalysisMapper.selectStcdStName(Arrays.asList(maxRainfall.getStcd()));
            if (CollectionUtils.isNotBlank(tempList)) {
                maxRainfall.setStationName(tempList.get(0).get("st_name"));
            }
        }
        result.put("maxRainfallOver24Hours", maxRainfall);
//
//        // 查询1小时累计降雨量
//        int oneHourWarningSum = 0;
//        List<Map<String, Object>> oneHourWarningThresholdList = waterRainAnalysisMapper.selectRainStationWarningThresholdByRainfallPeroid(1 + "h");
//        List<WarnInfoResVo> oneHourWarningInfo = new ArrayList<>();
//        if (CollectionUtils.isNotBlank(oneHourWarningThresholdList)) {
//            // 查询1小时累计降雨量
//            SynRainRequestVo reqVo=new  SynRainRequestVo();
//            reqVo.setMinutes(60);
//            reqVo.setMin(-1D);
//            reqVo.setMax(10000D);
//            List<StPptnR> oneHourRainfall=waterAndRainService.rainfall(reqVo);
//            if (oneHourRainfall != null) {
//                oneHourRainfall = Collections.emptyList();
//            }
//            Map<String, StPptnR> stcdDrpMap = oneHourRainfall.stream().collect(Collectors.toMap(StPptnR::getStcd, o -> o));
//            Map<String, Double> warningThresholdMap = oneHourWarningThresholdList.stream().collect(Collectors.toMap(map -> (String) map.get("st_code"), map -> {
//                Object temp = map.get("threshold");
//                if (temp != null) {
//                    return ((BigDecimal) temp).doubleValue();
//                } else {
//                    return null;
//                }
//            }));
//            Iterator<Map.Entry<String, Double>> iterator = warningThresholdMap.entrySet().iterator();
//            while (iterator.hasNext()) {
//                Map.Entry<String, Double> tempWarningThreshold = iterator.next();
//                Double threshold = tempWarningThreshold.getValue();
//                StPptnR rainfall = stcdDrpMap.get(tempWarningThreshold.getKey());
//                if (threshold != null && rainfall != null && rainfall.getDrp() != null) {
//                    if (rainfall.getDrp() >= threshold) {
//                        oneHourWarningSum++;
//                        WarnInfoResVo temp = new WarnInfoResVo();
//                        temp.setDrp(rainfall.getDrp());
//                        temp.setTm(rainfall.getTm());
//                        temp.setThreshold(threshold);
//                        temp.setStCode(rainfall.getStcd());
//                        temp.setOverThreshold(rainfall.getDrp() - threshold);
//                        temp.setStName(rainfall.getStationName());
//                        oneHourWarningInfo.add(temp);
//                    }
//                }
//            }
//            oneHourWarningInfo = oneHourWarningInfo.stream().sorted((o1, o2) -> o2.getOverThreshold().compareTo(o1.getOverThreshold())).limit(3).collect(Collectors.toList());
//        }
//        result.put("oneHourWarningInfo", oneHourWarningInfo);
//        result.put("oneHourWarningSum", oneHourWarningSum);
//
//        // 查询3小时累计降雨量
//        int threeHourWarningSum = 0;
//        List<Map<String, Object>> threeHourWarningThresholdList = waterRainAnalysisMapper.selectRainStationWarningThresholdByRainfallPeroid(3 + "h");
//        List<WarnInfoResVo> threeHourWarningInfo = new ArrayList<>();
//        if (CollectionUtils.isNotBlank(threeHourWarningThresholdList)) {
//            // 查询3小时累计降雨量
//            SynRainRequestVo reqVo=new  SynRainRequestVo();
//            reqVo.setMinutes(180);
//            reqVo.setMin(-1D);
//            reqVo.setMax(10000D);
//            List<StPptnR> threeHourRainfall=waterAndRainService.rainfall(reqVo);
//            if (threeHourRainfall != null) {
//                threeHourRainfall = Collections.emptyList();
//            }
//            if (threeHourRainfall != null) {
//                threeHourRainfall = Collections.emptyList();
//            }
//            Map<String, StPptnR> stcdDrpMap = threeHourRainfall.stream().collect(Collectors.toMap(StPptnR::getStcd, o -> o));
//            Map<String, Double> warningThresholdMap = threeHourWarningThresholdList.stream().collect(Collectors.toMap(map -> (String) map.get("st_code"), map -> {
//                Object temp = map.get("threshold");
//                if (temp != null) {
//                    return ((BigDecimal) temp).doubleValue();
//                } else {
//                    return null;
//                }
//            }));
//            Iterator<Map.Entry<String, Double>> iterator = warningThresholdMap.entrySet().iterator();
//            List<WarnInfoResVo> warnInfoRainfallResVos = new ArrayList<>();
//            while (iterator.hasNext()) {
//                Map.Entry<String, Double> tempWarningThreshold = iterator.next();
//                Double threshold = tempWarningThreshold.getValue();
//                StPptnR rainfall = stcdDrpMap.get(tempWarningThreshold.getKey());
//                if (threshold != null && rainfall != null && rainfall.getDrp() != null) {
//                    if (rainfall.getDrp() >= threshold) {
//                        threeHourWarningSum++;
//                        WarnInfoResVo temp = new WarnInfoResVo();
//                        temp.setDrp(rainfall.getDrp());
//                        temp.setTm(rainfall.getTm());
//                        temp.setThreshold(threshold);
//                        temp.setStCode(rainfall.getStcd());
//                        temp.setOverThreshold(rainfall.getDrp() - threshold);
//                        temp.setStName(rainfall.getStationName());
//                        warnInfoRainfallResVos.add(temp);
//                    }
//                }
//            }
//            threeHourWarningInfo = threeHourWarningInfo.stream().sorted((o1, o2) -> o2.getOverThreshold().compareTo(o1.getOverThreshold())).limit(3).collect(Collectors.toList());
//        }

        // 查询1小时累计降雨量
        SynRainRequestVo hour1ReqVo = new SynRainRequestVo();
        hour1ReqVo.setMinutes(60);
        hour1ReqVo.setMin(-1D);
        hour1ReqVo.setMax(10000D);
        result.putAll(rainfallWarnInfo(hour1ReqVo, "oneHourWarningInfo", "oneHourWarningSum", 30D));

        // 查询3小时累计降雨量
        SynRainRequestVo hour3ReqVo = new SynRainRequestVo();
        hour3ReqVo.setMinutes(180);
        hour3ReqVo.setMin(-1D);
        hour3ReqVo.setMax(10000D);
        result.putAll(rainfallWarnInfo(hour3ReqVo, "threeHourWarningInfo", "threeHourWarningSum", 50D));
        return result;
    }

    public Map<String, Object> rainfallWarnInfo(SynRainRequestVo reqVo, String infoKey, String sumKey, double threshold) {
        int warningSum = 0;
        List<WarnInfoResVo> warningInfo = null;
        List<StPptnR> oneHourRainfall = waterAndRainService.rainfall(reqVo);
        if (CollectionUtils.isBlank(oneHourRainfall)) {
            warningInfo = Collections.emptyList();
        } else {
            Date now = new Date();
            warningInfo = oneHourRainfall.stream()
                    .filter(o -> o.getDrp() != null && o.getDrp() > threshold)
                    .map(o -> Builder.of(WarnInfoResVo::new)
                            .with(WarnInfoResVo::setDrp, o.getDrp())
                            .with(WarnInfoResVo::setTm, now)
                            .with(WarnInfoResVo::setThreshold, threshold)
                            .with(WarnInfoResVo::setStCode, o.getStcd())
                            .with(WarnInfoResVo::setOverThreshold, o.getDrp() - threshold)
                            .with(WarnInfoResVo::setStName, o.getStationName())
                            .build())
                    .sorted(Comparator.comparing(WarnInfoResVo::getOverThreshold).reversed()).collect(Collectors.toList());
        }
        warningSum = warningInfo.size();
        if (warningSum > 3) {
            warningInfo = warningInfo.subList(0, 3);
        }
        Map<String, Object> result = new HashMap<>();
        result.put(infoKey, warningInfo);
        result.put(sumKey, warningSum);
        return result;
    }

    @Override
    public Map<String, Object> riverOverview() {
//        Map<String, Object> result = new HashMap<>();
//        // 江河水情
//        List<AttStBase> riverStList = waterRainAnalysisMapper.selectStationByType(StationType.RIVER.type());
//        if (CollectionUtils.isBlank(riverStList)) {
//            riverStList = Collections.emptyList();
//        }
//        Set<String> tables = new HashSet<>(2);
//        LocalDateTime now = LocalDateTime.now();
//        LocalDateTime oneDayAgo = now.plusDays(-1);
//        tables.clear();
//        tables.add(StationType.RIVER.table() + dtf.format(now));
//        tables.add(StationType.RIVER.table() + dtf.format(oneDayAgo));
//        Map<String, StRiverR> riverMap = new HashMap<>();
//        List<String> riverStCodeList = riverStList.stream().map(AttStBase::getStCode).collect(Collectors.toList());
//        if (tables.size() == 1) {
//            List<StRiverR> resWaterLevel = waterRainAnalysisMapper.selectNewestRiverLevel1Hour(new ArrayList<>(tables).get(0), riverStCodeList);
//            if (CollectionUtils.isNotBlank(resWaterLevel)) {
//                riverMap = resWaterLevel.stream().collect(Collectors.toMap(StRiverR::getStcd, o -> o));
//            }
//        } else if (tables.size() == 2) {
//            List<String> tableList = new ArrayList<>(tables);
//            List<StRiverR> water1 = waterRainAnalysisMapper.selectNewestRiverLevel1Hour(tableList.get(0), riverStCodeList);
//            List<StRiverR> water2 = waterRainAnalysisMapper.selectNewestRiverLevel1Hour(tableList.get(1), riverStCodeList);
//            if (CollectionUtils.isNotBlank(water1)) {
//                riverMap = water1.stream().collect(Collectors.toMap(StRiverR::getStcd, o -> o));
//                if (CollectionUtils.isNotBlank(water2)) {
//                    for (StRiverR stRsvrR : water2) {
//                        riverMap.putIfAbsent(stRsvrR.getStcd(), stRsvrR);
//                    }
//                }
//            } else if (CollectionUtils.isNotBlank(water2)) {
//                riverMap = water2.stream().collect(Collectors.toMap(StRiverR::getStcd, o -> o));
//            }
//        }
//        int riverWarnSum = 0;
//        List<WarnInfoResVo> riverWarnInfo = new ArrayList<>();
//        for (AttStBase station : riverStList) {
//            Double warningLevel = station.getWarningLevel();
//            String stCode = station.getStCode();
//            StRiverR waterLevel = riverMap.get(stCode);
//            if (warningLevel != null && waterLevel != null && waterLevel.getZ() != null) {
//                Double threshold = warningLevel;
//                if (waterLevel.getZ() >= threshold) {
//                    riverWarnSum++;
//                    WarnInfoResVo temp = new WarnInfoResVo();
//                    temp.setWaterLevel(waterLevel.getZ());
//                    temp.setTm(waterLevel.getTm());
//                    temp.setThreshold(warningLevel);
//                    temp.setStCode(waterLevel.getStcd());
//                    temp.setOverThreshold(waterLevel.getZ() - threshold);
//                    temp.setStName(station.getStName());
//                    riverWarnInfo.add(temp);
//                }
//            }
//        }
//        riverWarnInfo = riverWarnInfo.stream().sorted((o1, o2) -> o2.getOverThreshold().compareTo(o1.getOverThreshold())).limit(3).collect(Collectors.toList());
//        result.put("riverWarnInfo", riverWarnInfo);
//        result.put("riverWarnSum", riverWarnSum);
//
//        return result;
        Map<String, Object> result = new HashMap<>();
        SynWaterRequestVo reqVo = new SynWaterRequestVo();
        reqVo.setStationType(StationType.RIVER.type());
        reqVo.setRiverStationType("平原河网");
        List<AttStBase> plainRiverList = this.waterAndRainService.stations(reqVo);
        plainRiverList = plainRiverList == null ? Collections.emptyList() : plainRiverList;

        reqVo.setRiverStationType("山区河道");
        List<AttStBase> mountainRiverList = this.waterAndRainService.stations(reqVo);
        mountainRiverList = mountainRiverList == null ? Collections.emptyList() : mountainRiverList;

        List<AttStBase> riverList = new ArrayList<>();
        riverList.addAll(plainRiverList);
        riverList.addAll(mountainRiverList);

        List<WarnInfoResVo> riverWarnInfo = riverList.stream()
                .filter(o -> o.getWarningLevel() != null && o.getWaterLevel() > o.getWarningLevel())
                .map(waterLevel ->
                        Builder.of(WarnInfoResVo::new)
                                .with(WarnInfoResVo::setWaterLevel, waterLevel.getWaterLevel())
                                .with(WarnInfoResVo::setTm, waterLevel.getTime())
                                .with(WarnInfoResVo::setThreshold, waterLevel.getWarningLevel())
                                .with(WarnInfoResVo::setStCode, waterLevel.getStCode())
                                .with(WarnInfoResVo::setOverThreshold, waterLevel.getWaterLevel() - waterLevel.getWarningLevel())
                                .with(WarnInfoResVo::setStName, waterLevel.getStName())
                                .build())
                .sorted(Comparator.comparing(WarnInfoResVo::getOverThreshold)
                        .reversed())
                .collect(Collectors.toList());

        int riverWarnSum = riverWarnInfo.size();
        if (riverWarnSum > 3) {
            riverWarnInfo = riverWarnInfo.subList(0, 3);
        }
        result.put("riverWarnInfo", riverWarnInfo);
        result.put("riverWarnSum", riverWarnSum);
        return result;
    }

    @Override
    public Map<String, Object> largeMidResOverview() {
//        Map<String, Object> result = new HashMap<>();
//        // 大中型水库水情
//        List<Map<String, Object>> largeMidResData = waterRainAnalysisMapper.selectLargeMidResData();
//        if (CollectionUtils.isBlank(largeMidResData)) {
//            largeMidResData = Collections.emptyList();
//        }
//        Set<String> tables=new HashSet<>(2);
//        LocalDateTime now=LocalDateTime.now();
//        LocalDateTime oneDayAgo=now.plusDays(-1);
//        tables.clear();
//        tables.add(StationType.RESERVOIR.table() +dtf.format(now));
//        tables.add(StationType.RESERVOIR.table() + dtf.format(oneDayAgo));
//        Map<String, Map<String, Object>> largeMidResDataMap = largeMidResData.stream().collect(Collectors.toMap(map -> (String) map.get("st_code"), o -> o));
//        List<String> stCodeList = new ArrayList<>(largeMidResDataMap.keySet());
//        Map<String, StRsvrR> resMap = new HashMap<>(stCodeList.size());
//        if (tables.size() == 1) {
//            List<StRsvrR> resWaterLevel = waterRainAnalysisMapper.selectNewestReserviorWaterLevel1Hour(new ArrayList<>(tables).get(0), stCodeList);
//            if (CollectionUtils.isNotBlank(resWaterLevel)) {
//                resMap = resWaterLevel.stream().collect(Collectors.toMap(StRsvrR::getStcd, o -> o));
//            }
//        } else if (tables.size() == 2) {
//            List<String> tableList = new ArrayList<>(tables);
//            List<StRsvrR> water1 = waterRainAnalysisMapper.selectNewestReserviorWaterLevel1Hour(tableList.get(0), stCodeList);
//            List<StRsvrR> water2 = waterRainAnalysisMapper.selectNewestReserviorWaterLevel1Hour(tableList.get(1), stCodeList);
//            if (CollectionUtils.isNotBlank(water1)) {
//                resMap = water1.stream().collect(Collectors.toMap(StRsvrR::getStcd, o -> o));
//                if (CollectionUtils.isNotBlank(water2)) {
//                    for (StRsvrR stRsvrR : water2) {
//                        resMap.putIfAbsent(stRsvrR.getStcd(), stRsvrR);
//                    }
//                }
//            } else if (CollectionUtils.isNotBlank(water2)) {
//                resMap = water2.stream().collect(Collectors.toMap(StRsvrR::getStcd, o -> o));
//            }
//        }
//        int largeMidResWarnSum = 0;
////        a.st_code,b.res_name,b.low_left_long,b.low_left_lat,b.res_code,b.mei_flood_limit_water_level
//        List<WarnInfoResVo> largeMidResWarnInfo = new ArrayList<>();
//        for (Map<String, Object> largeMidRes : largeMidResData) {
//            Object meiFloodLimitWaterLevel = largeMidRes.get("mei_flood_limit_water_level");
//            String stCode = (String) largeMidRes.get("st_code");
//            StRsvrR waterLevel = resMap.get(stCode);
//            if (meiFloodLimitWaterLevel != null && waterLevel != null && waterLevel.getRz() != null) {
//                Double threshold = ((BigDecimal) meiFloodLimitWaterLevel).doubleValue();
//                if (waterLevel.getRz() >= threshold) {
//                    largeMidResWarnSum++;
//                    WarnInfoResVo temp = new WarnInfoResVo();
//                    temp.setWaterLevel(waterLevel.getRz());
//                    temp.setTm(waterLevel.getTm());
//                    temp.setThreshold(threshold);
//                    temp.setStCode(waterLevel.getStcd());
//                    temp.setOverThreshold(waterLevel.getRz() - threshold);
//                    temp.setResName((String) largeMidRes.get("res_name"));
//                    largeMidResWarnInfo.add(temp);
//                }
//            }
//        }
//        largeMidResWarnInfo = largeMidResWarnInfo.stream().sorted((o1, o2) -> o2.getOverThreshold().compareTo(o1.getOverThreshold())).limit(3).collect(Collectors.toList());
//        result.put("largeMidResWarnInfo", largeMidResWarnInfo);
//        result.put("largeMidResWarnSum", largeMidResWarnSum);
//        return result;
        Map<String, Object> result = new HashMap<>();
        List<WarnInfoResVo> largeMidResWarnInfo = Collections.emptyList();
        CommonResult<PageVO<LinkedHashMap>> resp = projectManageClient.rsvrWaterLevel(Builder.of(RsvrStationWaterReqVo::new)
                .with(RsvrStationWaterReqVo::setEngScales, String.join(StringUtils.JOIN_DELIMITER, EngScalEnum.L1.getDesc(), EngScalEnum.L2.getDesc(), EngScalEnum.MID.getDesc()))
                .build());
        if (resp != null && resp.getCode() == CommonCode.SUCCESS.code() && CollectionUtils.isNotBlank(resp.getData().getList())) {
            List<RsvrStationWaterResVo> data = JSONArray.parseArray(JSONArray.toJSONString(resp.getData().getList()), RsvrStationWaterResVo.class);
            largeMidResWarnInfo = data.stream()
                    .filter(o -> o.getOverLimitVal() != null && o.getOverLimitVal() > 0)
                    .map(o -> Builder.of(WarnInfoResVo::new)
                            .with(WarnInfoResVo::setWaterLevel, o.getWaterLevel())
                            .with(WarnInfoResVo::setTm, o.getDataTime())
                            .with(WarnInfoResVo::setThreshold, o.getCurrentLimitLevel().doubleValue())
                            .with(WarnInfoResVo::setOverThreshold, o.getOverLimitVal())
                            .with(WarnInfoResVo::setResName, o.getResName())
                            .build())
                    .sorted(Comparator.comparing(WarnInfoResVo::getOverThreshold)
                            .reversed())
                    .collect(Collectors.toList());
        }
        int largeMidResWarnSum = largeMidResWarnInfo.size();
        if (largeMidResWarnSum > 3) {
            largeMidResWarnInfo = largeMidResWarnInfo.subList(0, 3);
        }
        result.put("largeMidResWarnInfo", largeMidResWarnInfo);
        result.put("largeMidResWarnSum", largeMidResWarnSum);
        return result;
    }

    @Override
    public List<Map<String, Object>> selectHistoryExtrem(String type, String stName) {
        if ("1".equals(type)) {
            return waterRainAnalysisMapper.selectRainfallExtrem(stName);
        } else if ("2".equals(type)) {
            return waterRainAnalysisMapper.selectWaterExtrem(stName);
        } else {
            return Collections.emptyList();
        }
    }

}
