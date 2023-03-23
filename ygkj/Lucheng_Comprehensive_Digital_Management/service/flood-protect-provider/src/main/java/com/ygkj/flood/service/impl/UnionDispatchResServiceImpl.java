package com.ygkj.flood.service.impl;

import com.github.pagehelper.PageHelper;
import com.ygkj.enums.StationType;
import com.ygkj.flood.feign.WaterAndRainClient;
import com.ygkj.flood.mapper.RainfallAnalysisMapper;
import com.ygkj.flood.mapper.StationMapper;
import com.ygkj.flood.mapper.UnionDispatchResMapper;
import com.ygkj.flood.mapper.WaterAndRainMapper;
import com.ygkj.flood.model.*;
import com.ygkj.flood.service.UnionDispatchResService;
import com.ygkj.flood.service.WaterAndRainService;
import com.ygkj.flood.vo.request.StQueryReqVo;
import com.ygkj.flood.vo.request.SynRainRequestVo;
import com.ygkj.flood.vo.response.ProResInfo;
import com.ygkj.flood.vo.response.WarnInfoResVo;
import com.ygkj.flood.vo.response.WlaOverWarnNumRespVo;
import com.ygkj.utils.Builder;
import com.ygkj.utils.CollectionUtils;
import com.ygkj.utils.PageUtils;
import com.ygkj.vo.PageVO;
import com.ygkj.water.model.CommonResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UnionDispatchResServiceImpl implements UnionDispatchResService {

    @Resource
    private UnionDispatchResMapper unionDispatchResMapper;

    @Autowired
    WaterAndRainService waterAndRainService;

    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMM");

    @Resource
    RainfallAnalysisMapper rainfallAnalysisMapper;

    @Resource
    StationMapper stationMapper;

    @Resource
    WaterAndRainMapper waterAndRainMapper;

    @Override
    public CommonResult<WlaOverWarnNumRespVo> warnNum() {

        WlaOverWarnNumRespVo respVo = new WlaOverWarnNumRespVo();

        List<ProResInfo> proResInfos = getProResInfoList(null);
        if (CollectionUtils.isBlank(proResInfos)) {
            return CommonResult.success(respVo);
        }

        int count = (int) proResInfos.stream().filter(res -> StringUtils.isNotBlank(res.getWarnStatus()) && "1".equals(res.getWarnStatus())).count();
        respVo.setLimitNum(count);

        return CommonResult.success(respVo);
    }

    final String shanxiResName = "珊溪";

    @Override
    public CommonResult<List<ProResInfo>> resProList(String projectCode) {
        List<ProResInfo> proResInfoList = getProResInfoList(projectCode);
        if (CollectionUtils.isBlank(proResInfoList)) {
            return CommonResult.success(Collections.emptyList());
        }
        ProResInfo shanxi = null;
        Iterator<ProResInfo> iterator = proResInfoList.iterator();
        while (iterator.hasNext()) {
            ProResInfo temp = iterator.next();
            if (temp.getProjectName().contains(shanxiResName)) {
                shanxi = temp;
                iterator.remove();
            }
        }
        if (shanxi != null) {
            proResInfoList.add(0, shanxi);
        }
        return CommonResult.success(proResInfoList);
    }

    @Override
    public PageVO<FloodPoint> newestFloodPoint(Integer pageNum, Integer pageSize) {
        if (pageNum != null && pageNum > 0) {

        } else {
            pageNum = 1;
        }
        if (pageSize != null && pageSize > 0) {

        } else {
            pageSize = 10;
        }
        PageHelper.startPage(pageNum, pageSize);
        return PageUtils.getPageVO(unionDispatchResMapper.listNewestFloodPoint(10));
    }

    @Override
    public Map<String, Object> rainfallWarn() {
        Map<String, Object> result = new HashMap<>();
        // 查询最大雨量站点
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime oneDayAgo = now.plusDays(-1);
        // 查询1小时累计降雨量
        int oneHourWarningSum = 0;
        List<Map<String, Object>> oneHourWarningThresholdList = rainfallAnalysisMapper.selectRainStationWarningThresholdByRainfallPeroid(1 + "h");
        List<WarnInfoResVo> oneHourWarningInfo = new ArrayList<>();
        if (CollectionUtils.isNotBlank(oneHourWarningThresholdList)) {
            // 查询1小时累计降雨量
            SynRainRequestVo reqVo = new SynRainRequestVo();
            reqVo.setMinutes(60);
            reqVo.setMin(-1D);
            reqVo.setMax(10000D);
            List<StPptnR> oneHourRainfall = waterAndRainService.rainfall(reqVo);
            if (oneHourRainfall != null) {
                oneHourRainfall = Collections.emptyList();
            }
            Map<String, StPptnR> stcdDrpMap = oneHourRainfall.stream().collect(Collectors.toMap(StPptnR::getStcd, o -> o));
            Map<String, Double> warningThresholdMap = oneHourWarningThresholdList.stream().collect(Collectors.toMap(map -> (String) map.get("st_code"), map -> {
                Object temp = map.get("threshold");
                if (temp != null) {
                    return ((BigDecimal) temp).doubleValue();
                } else {
                    return null;
                }
            }));
            Iterator<Map.Entry<String, Double>> iterator = warningThresholdMap.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, Double> tempWarningThreshold = iterator.next();
                Double threshold = tempWarningThreshold.getValue();
                StPptnR rainfall = stcdDrpMap.get(tempWarningThreshold.getKey());
                if (threshold != null && rainfall != null && rainfall.getDrp() != null) {
                    if (rainfall.getDrp() >= threshold) {
                        oneHourWarningSum++;
                        WarnInfoResVo temp = new WarnInfoResVo();
                        temp.setValue(rainfall.getDrp());
                        temp.setTime(rainfall.getTm());
                        temp.setThreshold(threshold);
                        temp.setCode(rainfall.getStcd());
                        temp.setOverThreshold(rainfall.getDrp() - threshold);
                        temp.setName(rainfall.getStationName());
                        oneHourWarningInfo.add(temp);
                    }
                }
            }
            oneHourWarningInfo = oneHourWarningInfo.stream().sorted((o1, o2) -> o2.getOverThreshold().compareTo(o1.getOverThreshold())).collect(Collectors.toList());
        }
        result.put("oneHourWarningInfo", oneHourWarningInfo);
        result.put("oneHourWarningSum", oneHourWarningSum);

        // 查询3小时累计降雨量
        int threeHourWarningSum = 0;
        List<Map<String, Object>> threeHourWarningThresholdList = rainfallAnalysisMapper.selectRainStationWarningThresholdByRainfallPeroid(3 + "h");
        List<WarnInfoResVo> threeHourWarningInfo = new ArrayList<>();
        if (CollectionUtils.isNotBlank(threeHourWarningThresholdList)) {
            // 查询3小时累计降雨量
            SynRainRequestVo reqVo = new SynRainRequestVo();
            reqVo.setMinutes(60);
            reqVo.setMin(-1D);
            reqVo.setMax(10000D);
            List<StPptnR> threeHourRainfall = waterAndRainService.rainfall(reqVo);
            if (threeHourRainfall != null) {
                threeHourRainfall = Collections.emptyList();
            }
            if (threeHourRainfall != null) {
                threeHourRainfall = Collections.emptyList();
            }
            Map<String, StPptnR> stcdDrpMap = threeHourRainfall.stream().collect(Collectors.toMap(StPptnR::getStcd, o -> o));
            Map<String, Double> warningThresholdMap = threeHourWarningThresholdList.stream().collect(Collectors.toMap(map -> (String) map.get("st_code"), map -> {
                Object temp = map.get("threshold");
                if (temp != null) {
                    return ((BigDecimal) temp).doubleValue();
                } else {
                    return null;
                }
            }));
            Iterator<Map.Entry<String, Double>> iterator = warningThresholdMap.entrySet().iterator();
            List<WarnInfoResVo> warnInfoRainfallResVos = new ArrayList<>();
            while (iterator.hasNext()) {
                Map.Entry<String, Double> tempWarningThreshold = iterator.next();
                Double threshold = tempWarningThreshold.getValue();
                StPptnR rainfall = stcdDrpMap.get(tempWarningThreshold.getKey());
                if (threshold != null && rainfall != null && rainfall.getDrp() != null) {
                    if (rainfall.getDrp() >= threshold) {
                        threeHourWarningSum++;
                        WarnInfoResVo temp = new WarnInfoResVo();
                        temp.setValue(rainfall.getDrp());
                        temp.setTime(rainfall.getTm());
                        temp.setThreshold(threshold);
                        temp.setCode(rainfall.getStcd());
                        temp.setOverThreshold(rainfall.getDrp() - threshold);
                        temp.setName(rainfall.getStationName());
                        warnInfoRainfallResVos.add(temp);
                    }
                }
            }
            threeHourWarningInfo = threeHourWarningInfo.stream().sorted((o1, o2) -> o2.getOverThreshold().compareTo(o1.getOverThreshold())).collect(Collectors.toList());
        }
        result.put("threeHourWarningInfo", threeHourWarningInfo);
        result.put("threeHourWarningSum", threeHourWarningSum);
        return result;
    }

    @Override
    public Map<String, Object> riverWagaWarn() {
        Map<String, Object> result = new HashMap<>();
        // 江河水情
        StQueryReqVo reqVo = new StQueryReqVo();
        reqVo.setStType(StationType.RIVER.type());
        List<StBase> riverStList = stationMapper.loadStByCondtion(reqVo);
        if (CollectionUtils.isBlank(riverStList)) {
            riverStList = Collections.emptyList();
        }
        Set<String> tables = new HashSet<>(2);
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime oneDayAgo = now.plusDays(-1);
        tables.clear();
        tables.add(StationType.RIVER.table() + dtf.format(now));
        tables.add(StationType.RIVER.table() + dtf.format(oneDayAgo));
        Map<String, StRiverR> riverMap = new HashMap<>();
        List<String> riverStCodeList = riverStList.stream().map(StBase::getStCode).collect(Collectors.toList());
        List<StRiverR> resWaterLevel = waterAndRainMapper.selectNewestRiverLevel(new ArrayList<>(tables), riverStCodeList);
        if (CollectionUtils.isNotBlank(resWaterLevel)) {
            riverMap = resWaterLevel.stream().collect(Collectors.toMap(StRiverR::getStcd, o -> o));
        }
        int riverWarnSum = 0;
        List<WarnInfoResVo> riverWarnInfo = new ArrayList<>();
        for (StBase station : riverStList) {
            Double warningLevel = station.getWarningLevel();
            String stCode = station.getStCode();
            StRiverR waterLevel = riverMap.get(stCode);
            if (warningLevel != null && waterLevel != null && waterLevel.getZ() != null) {
                Double threshold = warningLevel;
                if (waterLevel.getZ() >= threshold) {
                    riverWarnSum++;
                    WarnInfoResVo temp = new WarnInfoResVo();
                    temp.setValue(waterLevel.getZ());
                    temp.setTime(waterLevel.getTm());
                    temp.setThreshold(warningLevel);
                    temp.setCode(waterLevel.getStcd());
                    temp.setOverThreshold(waterLevel.getZ() - threshold);
                    temp.setName(station.getStName());
                    riverWarnInfo.add(temp);
                }
            }
        }
        riverWarnInfo = riverWarnInfo.stream().sorted((o1, o2) -> o2.getOverThreshold().compareTo(o1.getOverThreshold())).collect(Collectors.toList());
        result.put("riverWarnInfo", riverWarnInfo);
        result.put("riverWarnSum", riverWarnSum);
        return result;
    }

    @Override
    public Map<String, Object> largeMidRsvrWarn() {
        Map<String, Object> result = new HashMap<>();
        // 大中型水库水情
        List<Map<String, Object>> largeMidResData = stationMapper.selectLargeMidResData();
        if (CollectionUtils.isBlank(largeMidResData)) {
            largeMidResData = Collections.emptyList();
        }
        Set<String> tables = new HashSet<>(2);
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime oneDayAgo = now.plusDays(-1);
        tables.clear();
        tables.add(StationType.RESERVOIR.table() + dtf.format(now));
        tables.add(StationType.RESERVOIR.table() + dtf.format(oneDayAgo));
        Map<String, Map<String, Object>> largeMidResDataMap = largeMidResData.stream().collect(Collectors.toMap(map -> (String) map.get("st_code"), o -> o));
        List<String> stCodeList = new ArrayList<>(largeMidResDataMap.keySet());
        Map<String, StRsvrR> resMap = new HashMap<>(stCodeList.size());
        List<StRsvrR> resWaterLevel = waterAndRainMapper.selectNewestRsvrWaterLevel(new ArrayList<>(tables), stCodeList);
        if (CollectionUtils.isNotBlank(resWaterLevel)) {
            resMap = resWaterLevel.stream().collect(Collectors.toMap(StRsvrR::getStcd, o -> o));
        }
        int largeMidResWarnSum = 0;
//        a.st_code,b.res_name,b.low_left_long,b.low_left_lat,b.res_code,b.mei_flood_limit_water_level
        List<WarnInfoResVo> largeMidResWarnInfo = new ArrayList<>();
        for (Map<String, Object> largeMidRes : largeMidResData) {
            Object meiFloodLimitWaterLevel = largeMidRes.get("mei_flood_limit_water_level");
            String stCode = (String) largeMidRes.get("st_code");
            StRsvrR waterLevel = resMap.get(stCode);
            if (meiFloodLimitWaterLevel != null && waterLevel != null && waterLevel.getRz() != null) {
                Double threshold = ((BigDecimal) meiFloodLimitWaterLevel).doubleValue();
                if (waterLevel.getRz() >= threshold) {
                    largeMidResWarnSum++;
                    WarnInfoResVo temp = new WarnInfoResVo();
                    temp.setValue(waterLevel.getRz());
                    temp.setTime(waterLevel.getTm());
                    temp.setThreshold(threshold);
                    temp.setCode(waterLevel.getStcd());
                    temp.setOverThreshold(waterLevel.getRz() - threshold);
                    temp.setName((String) largeMidRes.get("res_name"));
                    largeMidResWarnInfo.add(temp);
                }
            }
        }
        largeMidResWarnInfo = largeMidResWarnInfo.stream().sorted((o1, o2) -> o2.getOverThreshold().compareTo(o1.getOverThreshold())).collect(Collectors.toList());
        result.put("largeMidResWarnInfo", largeMidResWarnInfo);
        result.put("largeMidResWarnSum", largeMidResWarnSum);
        return result;
    }

    @Override
    public Map<String, Object> floodPointWarn() {
        Map<String, Object> result = new HashMap<>();
        List<FloodPoint> tempList = unionDispatchResMapper.listNewestFloodPoint(1);
        tempList = tempList == null ? Collections.emptyList() : tempList;
        List<WarnInfoResVo> floodPointWarnInfo = tempList.stream().map(o -> Builder.of(WarnInfoResVo::new)
                .with(WarnInfoResVo::setCode, String.valueOf(o.getId()))
                .with(WarnInfoResVo::setName, o.getName())
                .with(WarnInfoResVo::setValue, o.getWaterLevel().doubleValue())
                .with(WarnInfoResVo::setTime, o.getAcquisitionTime())
                .build()).collect(Collectors.toList());
        result.put("floodPointWarnInfo", floodPointWarnInfo);
        result.put("floodPointWarnSum", tempList.size());
        return result;
    }

    private List<ProResInfo> getProResInfoList(String projectCode) {
        // 查询19座大中型水库
        List<ProResInfo> proResInfos = unionDispatchResMapper.getResList(null);
        if (CollectionUtils.isBlank(proResInfos)) {
            return proResInfos;
        }

        // 查询水库水位（近3天内最新水位）
        String yyyyMM = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMM"));
        String tableName = StationType.RESERVOIR.table() + yyyyMM;
        List<StRiverRYyyymm> waterLevels = unionDispatchResMapper.getResLevels(tableName);

        if (CollectionUtils.isNotBlank(waterLevels)) {
            Map<String, Double> levelMap = waterLevels.stream().filter(resLevel -> resLevel.getZ() != null)
                    .collect(Collectors.toMap(res -> res.getStcd(), res -> res.getZ()));
            for (ProResInfo proResInfo : proResInfos) {
                String stCode = proResInfo.getStCode();
                if (levelMap.containsKey(stCode)) {
                    Double z = levelMap.get(stCode);
                    proResInfo.setZ(z);
                    Double limitLevel = proResInfo.getLimitLevel();
                    if (limitLevel != null && z.compareTo(limitLevel) > 0) {
                        proResInfo.setWarnStatus("1");
                    }
                }
            }
        }

        return proResInfos;
    }
}
