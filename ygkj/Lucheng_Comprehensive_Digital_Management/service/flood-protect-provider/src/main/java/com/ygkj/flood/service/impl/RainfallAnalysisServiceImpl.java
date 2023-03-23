package com.ygkj.flood.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.ygkj.enums.RainfallLevel;
import com.ygkj.enums.StationType;
import com.ygkj.flood.feign.MeteorologicalClient;
import com.ygkj.flood.feign.WaterAndRainClient;
import com.ygkj.flood.mapper.CollectPointMapper;
import com.ygkj.flood.mapper.ExtremRainfallMapper;
import com.ygkj.flood.mapper.RainfallAnalysisMapper;
import com.ygkj.flood.mapper.StationMapper;
import com.ygkj.flood.model.StBase;
import com.ygkj.flood.model.StPptnR;
import com.ygkj.flood.service.RainfallAnalysisService;
import com.ygkj.flood.service.WaterAndRainService;
import com.ygkj.flood.vo.request.StQueryReqVo;
import com.ygkj.flood.vo.request.SynRainRequestVo;
import com.ygkj.flood.vo.response.AdRealtimeRainResVo;
import com.ygkj.flood.vo.response.CollectPointRainfall;
import com.ygkj.flood.vo.response.RainfallExtremumResVo;
import com.ygkj.flood.vo.response.RainfallGenResVo;
import com.ygkj.jwt.JwtTokenUtil;
import com.ygkj.utils.Builder;
import com.ygkj.utils.CollectionUtils;
import com.ygkj.water.model.CommonCode;
import com.ygkj.water.model.CommonResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author xq
 * @Description 防汛形势研判-降雨分析 服务层
 * @Date 2021/1/18
 */
@Service
public class RainfallAnalysisServiceImpl implements RainfallAnalysisService {

    @Autowired
    WaterAndRainClient waterAndRainClient;

    @Resource
    RainfallAnalysisMapper rainfallAnalysisMapper;

    @Resource
    CollectPointMapper collectPointMapper;

    @Resource
    StationMapper stationMapper;

    @Autowired
    WaterAndRainService waterAndRainService;

    /**
     * 报警阈值 降雨历时 时间单位
     */
    private final static String warningThresholdTimeUnit = "h";

    private final static Double maxRainfall = 10000.0D;
    private final static Double minRainfall = -9999.0D;

    @Override
    public List<AdRealtimeRainResVo> getAllAdcdRealtimeRainfall(Integer minutes, String adnm) {
        boolean flag = StringUtils.isNotBlank(adnm);
        List<Map> wzAdSt = rainfallAnalysisMapper.selectWzAdSt(adnm);
        if (!CollectionUtils.isBlank(wzAdSt)) {
            Map<String, List<Map>> wzAdStMap = wzAdSt.stream().collect(Collectors.groupingBy(map1 -> (String) map1.getOrDefault("adcd", StringUtils.EMPTY)));
            Map<String, Double> stcdDrpMap = null;
            /**
             * 如果行政区划名称为空，则拿所有的数据
             * 否则，根据查询到的adcd水雨工情接口，降低水雨工情的负载
             */

            if (flag) {
                stcdDrpMap = new HashMap<>(wzAdSt.size());
                Set<String> adcdSet = wzAdStMap.keySet();
                for (String adcd : adcdSet) {
                    stcdDrpMap.putAll(getStationDrpMap(minutes, adcd));
                }
            } else {
                stcdDrpMap = getStationDrpMap(minutes);
            }
            Iterator<Map.Entry<String, List<Map>>> iterator = wzAdStMap.entrySet().iterator();
            List<AdRealtimeRainResVo> result = new ArrayList<>(wzAdStMap.keySet().size());
            while (iterator.hasNext()) {
                Map.Entry<String, List<Map>> entry = iterator.next();
                AdRealtimeRainResVo temp = new AdRealtimeRainResVo();
                temp.setAdnm((String) entry.getValue().get(0).get("adnm"));
                temp.setAdcd(entry.getKey());
                Double drpSum = 0.0;
                for (Map map1 : entry.getValue()) {
                    String stCode = (String) map1.get("st_code");
                    if (StringUtils.isNotBlank(stCode)) {
                        Double stDrp = stcdDrpMap.get(stCode);
                        if (stDrp != null) {
                            drpSum += stDrp;
                        }
                    }
                }
                temp.setDrpSum(drpSum);
                result.add(temp);
            }
            result.stream().sorted(Comparator.comparing(AdRealtimeRainResVo::getAdcd)).collect(Collectors.toList());
            return result;
        }
        return null;
    }

    @Override
    public List<RainfallGenResVo> getAllBasinRealtimeRainfall(Integer minutes, String basin) {
        StQueryReqVo reqVo = new StQueryReqVo();
        reqVo.setStType(StationType.RAINFALL.type());
        if (StringUtils.isNotBlank(basin)) {
            reqVo.setBas(basin);
        }
        List<StBase> stBases = this.stationMapper.loadStByCondtion(reqVo);
        if (CollectionUtils.isBlank(stBases)) {
            return Collections.emptyList();
        }
        Map<String, List<StBase>> basMap = stBases.stream().filter(o -> StringUtils.isNotBlank(o.getBas())).collect(Collectors.groupingBy(StBase::getBas));

        List<StPptnR> ppList = getAllStationRainfallByMinutes(minutes);
        ppList = ppList == null ? Collections.emptyList() : ppList;
        List<RainfallGenResVo> result = new ArrayList<>(basMap.size());
        Map<String, Optional<Double>> ppMap = ppList.stream().collect(Collectors.toMap(StPptnR::getStcd, o -> Optional.ofNullable(o.getDrp())));
        basMap.forEach((key, values) -> {
            double sum = 0D;
            for (StBase tempSt : values) {
                Optional<Double> drp = ppMap.getOrDefault(tempSt.getStCode(), Optional.ofNullable(0D));
                if (drp.isPresent()) {
                    sum += drp.get();
                }
            }
            result.add(Builder.of(RainfallGenResVo::new)
                    .with(RainfallGenResVo::setName, key)
                    .with(RainfallGenResVo::setDrp, sum)
                    .build());
        });
        return result;
    }

    @Override
    public Map<String, Integer> getRealtimeRainLevelStatistic(Integer minutes) {
        List<StPptnR> stationRainfall = getAllStationRainfallByMinutes(minutes);
        Map<String, List<StPptnR>> tempMap = null;
        if (!CollectionUtils.isBlank(stationRainfall)) {
            tempMap = stationRainfall.stream().collect(Collectors.groupingBy(stPptnR -> RainfallLevel.ensureRainfallLevel(stPptnR.getDrp()).name()));
        } else {
            tempMap = Collections.emptyMap();
        }
        Map<String, Integer> result = new HashMap<>(RainfallLevel.values().length);
        for (RainfallLevel value : RainfallLevel.values()) {
            result.put(value.name(), tempMap.getOrDefault(value.name(), Collections.emptyList()).size());
        }
        return result;
    }

    @Override
    public List<RainfallExtremumResVo> getRainfallExtremum(Integer minutes, String stName) {
        /**
         * 2021-01-25
         * 按宋亚路建议，历史极值方案还未确定，目前只需要查询历史极值就行了，不需要做和实时雨量做额外比较
         * 参考
         * 钱塘江防洪减灾数字化平台  https://qtj.zjwater.com/#/login  dd_18357782606  qtj59106
         * 极值分析-雨量极值
         */
        if (minutes != null) {
            return rainfallAnalysisMapper.selectRainfallExtremumByDurationTime(minutes, stName);
        } else {
            return rainfallAnalysisMapper.selectAllRainfallExtremum(stName);
        }
    }

    @Override
    public Integer getRainWarningStationNum(Integer hours) {
        if (hours == null) {
            return 0;
        }
        // 从报警阈值表中查询到测站阈值
        List<Map<String, Object>> warningThresholdList = rainfallAnalysisMapper.selectRainStationWarningThresholdByRainfallPeroid(hours.toString() + warningThresholdTimeUnit);
        if (CollectionUtils.isNotBlank(warningThresholdList)) {
            Map<String, Double> stcdDrpMap = getStationDrpMap(hours * 60);// 小时转换成分钟
            Map<String, Double> warningThresholdMap = warningThresholdList.stream().collect(Collectors.toMap(map -> (String) map.get("st_code"), map -> {
                Object temp = map.get("threshold");
                if (temp != null) {
                    return ((BigDecimal) temp).doubleValue();
                } else {
                    return null;
                }
            }));
            int sum = 0;
            Iterator<Map.Entry<String, Double>> iterator = warningThresholdMap.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, Double> tempWarningThreshold = iterator.next();
                Double threshold = tempWarningThreshold.getValue();
                Double realtimeVal = stcdDrpMap.get(tempWarningThreshold.getKey());
                if (threshold != null && realtimeVal != null) {
                    if (realtimeVal >= threshold) {
                        sum++;
                    }
                }
            }
            return sum;
        } else {
            return 0;
        }
    }

    @Resource
    ExtremRainfallMapper extremRainfallMapper;

    @Override
    public List<Map<String, Object>> getHistoricRainfallExtremum() {
        return extremRainfallMapper.selectExtremRainfall();
    }

    final static String[] rainfallType = new String[]{"0-10", "10-25", "25-50", "50-100", "100-150", "150"};//150 是大于150，和前端约定的
    final static String numKey = "num";
    final static String typeKey = "type";

    @Override
    public List<Map<String, Object>> getPpStNumStatistic(Integer minutes) {
        List<Map<String, Object>> result = new ArrayList<>();
        List<StPptnR> stationRainfall = getAllStationRainfallByMinutes(minutes);
        if (stationRainfall == null) {
            stationRainfall = Collections.emptyList();
        }
        Map<String, List<StPptnR>> tempMap = stationRainfall.stream().collect(Collectors.groupingBy(item -> {
            if (item.getDrp() == null) {
                return rainfallType[0];
            } else if (item.getDrp() < 10) {
                return rainfallType[0];
            } else if (item.getDrp() >= 10 && item.getDrp() < 25) {
                return rainfallType[1];
            } else if (item.getDrp() >= 25 && item.getDrp() < 50) {
                return rainfallType[2];
            } else if (item.getDrp() >= 50 && item.getDrp() < 100) {
                return rainfallType[3];
            } else if (item.getDrp() >= 100 && item.getDrp() < 150) {
                return rainfallType[4];
            } else {
                return rainfallType[5];
            }
        }));
        for (String s : rainfallType) {
            Map<String, Object> temp = new HashMap<>();
            temp.put(typeKey, s);
            List<StPptnR> tempList = tempMap.getOrDefault(s, Collections.emptyList());
            temp.put(numKey, tempList.size());
            result.add(temp);
        }
        return result;
    }

    @Autowired
    MeteorologicalClient meteorologicalClient;

    @Override
    public List<CollectPointRainfall> getCollectPointRainfall() {
        List<CollectPointRainfall> list = collectPointMapper.selectAllPoint(JwtTokenUtil.getCurrentUserId().orElse(getCurrentUserId())
                , null);
        Double zeroD = 0D;
        if (CollectionUtils.isNotBlank(list)) {
            for (CollectPointRainfall temp : list) {
                CommonResult<JSONObject> forest = meteorologicalClient.caculateRainfall(String.valueOf(temp.getLongitude()), String.valueOf(temp.getLatitude()));
                if (CommonCode.SUCCESS.code() == forest.getCode() && forest.getData() != null) {
                    JSONObject data = forest.getData();
                    try {
                        temp.setOneHourRainfall(data.getDoubleValue("rainfall_1"));
                    } catch (Exception e) {
                        temp.setOneHourRainfall(0D);
                    }
                    try {
                        temp.setThreeHourRainfall(data.getDoubleValue("rainfall_3"));
                    } catch (Exception e) {
                        temp.setThreeHourRainfall(0D);
                    }
                    try {
                        temp.setSixHourRainfall(data.getDoubleValue("rainfall_6"));
                    } catch (Exception e) {
                        temp.setSixHourRainfall(0D);
                    }
                    temp.setTwentyFourHourRainfall(zeroD);
                    temp.setSeventyTwoHourRainfall(zeroD);
                } else {
                    temp.setOneHourRainfall(zeroD);
                    temp.setThreeHourRainfall(zeroD);
                    temp.setSixHourRainfall(zeroD);
                    temp.setTwentyFourHourRainfall(zeroD);
                    temp.setSeventyTwoHourRainfall(zeroD);
                }
            }
        }
        return list;
    }

    @Override
    public List<RainfallGenResVo> getResStRainfall() {
        /**
         * 1. 先查询水库测站
         * 2. 再查询累计降雨量
         */
        List<RainfallGenResVo> result = new ArrayList<>();
        List<StBase> ppRsvrSt = stationMapper.loadRsvrSt(StationType.RAINFALL.type());
        if (CollectionUtils.isBlank(ppRsvrSt)) {
            return result;
        }
        List<StPptnR> rain1H = getAllStationRainfallByMinutes(60);
        rain1H = rain1H == null ? Collections.emptyList() : rain1H;
        Map<String, Optional<Double>> rain1Map = rain1H.stream().collect(Collectors.toMap(StPptnR::getStcd, o -> Optional.ofNullable(o.getDrp())));
        List<StPptnR> rain3H = getAllStationRainfallByMinutes(180);
        rain3H = rain3H == null ? Collections.emptyList() : rain3H;
        Map<String, Optional<Double>> rain3Map = rain3H.stream().collect(Collectors.toMap(StPptnR::getStcd, o -> Optional.ofNullable(o.getDrp())));
        List<StPptnR> rain6H = getAllStationRainfallByMinutes(360);
        rain6H = rain6H == null ? Collections.emptyList() : rain6H;
        Map<String, Optional<Double>> rain6Map = rain6H.stream().collect(Collectors.toMap(StPptnR::getStcd, o -> Optional.ofNullable(o.getDrp())));
        for (StBase temp : ppRsvrSt) {
            RainfallGenResVo tempResult = Builder.of(RainfallGenResVo::new)
                    .with(RainfallGenResVo::setName, temp.getStName())
                    .with(RainfallGenResVo::setCode, temp.getStCode())
                    .with(RainfallGenResVo::setLongitude, temp.getStLong().toString())
                    .with(RainfallGenResVo::setLatitude, temp.getStLat().toString())
                    .build();
            Optional<Double> drp1h = rain1Map.getOrDefault(temp.getStCode(), Optional.ofNullable(0D));
            Optional<Double> drp3h = rain3Map.getOrDefault(temp.getStCode(), Optional.ofNullable(0D));
            Optional<Double> drp6h = rain6Map.getOrDefault(temp.getStCode(), Optional.ofNullable(0D));
            if (drp1h.isPresent()) {
                Double pp = drp1h.get();
                tempResult.setDrp1H(pp < 0 ? 0D : pp);
            }
            if (drp3h.isPresent()) {
                Double pp = drp3h.get();
                tempResult.setDrp3H(pp < 0 ? 0D : pp);
            }
            if (drp6h.isPresent()) {
                Double pp = drp6h.get();
                tempResult.setDrp6H(pp < 0 ? 0D : pp);
            }
            result.add(tempResult);
        }
        return result;
    }

    private String getCurrentUserId() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String token = request.getHeader("authorization");
        String userId = null;
        if (StringUtils.isNotBlank(token)) {
            userId = JwtTokenUtil.getUserId(token);
        }
        return userId;
    }


    private List<StPptnR> getAllStationRainfallByMinutes(Integer minutes) {
        //模仿前端 请求参数
//        CommonResult<List<StPptnR>> result = waterAndRainClient.rainfall(minutes, maxRainfall, minRainfall);
//        if (result.getCode() == CommonCode.SUCCESS.code()) {
//            return result.getData();
//        } else {
//            return Collections.emptyList();
//        }
        SynRainRequestVo reqVo = new SynRainRequestVo();
        reqVo.setMinutes(minutes);
        reqVo.setMax(maxRainfall);
        reqVo.setMin(minRainfall);
        return waterAndRainService.rainfall(reqVo);
    }

    private List<StPptnR> getAllStationRainfallByMinutesAdcd(Integer minutes, String adcd) {
//        //模仿前端 请求参数
//        CommonResult<List<StPptnR>> result = waterAndRainClient.rainfall(minutes, maxRainfall, minRainfall, adcd);
//        if (result.getCode() == CommonCode.SUCCESS.code()) {
//            return result.getData();
//        } else {
//            return Collections.emptyList();
//        }

        SynRainRequestVo reqVo = new SynRainRequestVo();
        reqVo.setMinutes(minutes);
        reqVo.setMax(maxRainfall);
        reqVo.setMin(minRainfall);
        reqVo.setAreaCode(adcd);
        return waterAndRainService.rainfall(reqVo);
    }

    private Map<String, Double> getStationDrpMap(Integer minutes) {
        if (minutes == null) {
            return Collections.emptyMap();
        }
        List<StPptnR> stationRainfall = getAllStationRainfallByMinutes(minutes);
        Map<String, Double> stcdDrpMap;
        if (!CollectionUtils.isBlank(stationRainfall)) {
            stcdDrpMap = stationRainfall.stream().map(stPptnR -> {
                if (stPptnR.getDrp() < 0) {
                    stPptnR.setDrp(0.0);
                }
                return stPptnR;
            }).collect(Collectors.toMap(StPptnR::getStcd, st -> st.getDrp()));
            return stcdDrpMap;
        }
        return Collections.emptyMap();
    }

    private Map<String, Double> getStationDrpMap(Integer minutes, String adcd) {
        if (minutes == null) {
            return Collections.emptyMap();
        }
        List<StPptnR> stationRainfall = getAllStationRainfallByMinutesAdcd(minutes, adcd);
        Map<String, Double> stcdDrpMap;
        if (!CollectionUtils.isBlank(stationRainfall)) {
            stcdDrpMap = stationRainfall.stream().map(stPptnR -> {
                if (stPptnR.getDrp() < 0) {
                    stPptnR.setDrp(0.0);
                }
                return stPptnR;
            }).collect(Collectors.toMap(StPptnR::getStcd, st -> st.getDrp()));
            return stcdDrpMap;
        }
        return Collections.emptyMap();
    }
}
