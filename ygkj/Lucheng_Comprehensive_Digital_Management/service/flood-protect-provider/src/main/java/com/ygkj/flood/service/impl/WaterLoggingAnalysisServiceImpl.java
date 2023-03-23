package com.ygkj.flood.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.ygkj.entity.SingletonMap;
import com.ygkj.enums.StationType;
import com.ygkj.flood.feign.MeteorologicalClient;
import com.ygkj.flood.mapper.AttAdcdBaseMapper;
import com.ygkj.flood.mapper.FloodPointMapper;
import com.ygkj.flood.mapper.StationMapper;
import com.ygkj.flood.mapper.WaterAndRainMapper;
import com.ygkj.flood.model.*;
import com.ygkj.flood.service.AttDictService;
import com.ygkj.flood.service.WaterLoggingAnalysisService;
import com.ygkj.flood.vo.request.AttDictRequestVo;
import com.ygkj.flood.vo.request.FloodPointReqVo;
import com.ygkj.flood.vo.request.StQueryReqVo;
import com.ygkj.flood.vo.response.AttDictResVo;
import com.ygkj.flood.vo.response.ChartResVo;
import com.ygkj.flood.vo.response.WaterLoggingModel;
import com.ygkj.utils.*;
import com.ygkj.vo.PageVO;
import com.ygkj.water.model.CommonCode;
import com.ygkj.water.model.CommonResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author xq
 * @Description
 * @Date 2021/4/25
 */
@Service
public class WaterLoggingAnalysisServiceImpl implements WaterLoggingAnalysisService, InitializingBean {

    @Resource
    private FloodPointMapper floodPointMapper;

    @Resource
    private WaterAndRainMapper waterAndRainMapper;

    @Resource
    StationMapper stationMapper;

    @Resource
    AttAdcdBaseMapper adcdBaseMapper;

    @Autowired
    private AttDictService attDictService;

    @Autowired
    MeteorologicalClient meteorologicalClient;

    Map<String, Map<Integer, WaterLoggingModel>> waterLoggingModelMap;

    List<WaterLoggingModel> waterLoggingModelList;

    final static String defaultWaterLevelUnit = "厘米";

    @Override
    public List<AttFloodPoint> listFloodPoint(FloodPointReqVo reqVo) {
        if (StringUtils.isNotBlank(reqVo.getAreaCode())
                && !StringUtils.startsWith(reqVo.getAreaCode(), "3303")) {
            reqVo.setAreaCode(adcdBaseMapper.selectWzAdcdByAdnm(reqVo.getAreaCode()));
        }

        List<FloodPoint> realTimeList = Collections.emptyList();
        if (reqVo.getRecentDays() == null) {
            reqVo.setRecentDays(1);
            realTimeList = floodPointMapper.listNewestFloodPoint(reqVo);
        } else {
            realTimeList = floodPointMapper.listNewestFloodPoint(reqVo);
        }

        List<AttFloodPoint> stationList = floodPointMapper.selectFloodPointDefine(reqVo);

        stationList = stationList == null ? Collections.emptyList() : stationList;
        realTimeList = realTimeList == null ? Collections.emptyList() : realTimeList;
        Map<String, FloodPoint> floodPointMap = realTimeList.stream().collect(Collectors.toMap(FloodPoint::getName, Function.identity()));
        Date acquisitionTime = new Date();
        for (AttFloodPoint attFloodPoint : stationList) {
            FloodPoint realData = floodPointMap.get(attFloodPoint.getName());
            if (realData == null) {
                attFloodPoint.setWaterLevel(BigDecimal.ZERO);
                attFloodPoint.setWaterLevelUnit(defaultWaterLevelUnit);
                attFloodPoint.setAcquisitionTime(acquisitionTime);
                attFloodPoint.setNoData(true);
            } else {
                attFloodPoint.setWaterLevel(BigDecimal.ZERO);
                attFloodPoint.setWaterLevelUnit(defaultWaterLevelUnit);
                attFloodPoint.setAcquisitionTime(acquisitionTime);
            }
            attFloodPoint.setPonding(attFloodPoint.getWaterLevel().doubleValue() > 0);
        }

        // 默认根据水深排序
//        realTimeList = realTimeList.stream().sorted((o1, o2) -> o2.getWaterLevel().compareTo(o1.getWaterLevel())).collect(Collectors.toList());

        // 判断是否有积水
        if (reqVo.getPonding() != null && reqVo.getPonding()) {
            stationList = stationList.stream().filter(o -> reqVo.getPonding().equals(o.isPonding())).collect(Collectors.toList());
        }
//        if (StringUtils.isNotBlank(reqVo.getOrderColumn())) {
//            Comparator<FloodPoint> comparator = new BeanComparator<>(reqVo.getOrderColumn());
//            if (reqVo.getAscOrDesc() != null && reqVo.getAscOrDesc()) {
//                comparator = comparator.reversed();
//            }
//            realTimeList = realTimeList.stream().sorted(comparator).collect(Collectors.toList());
//        }
        return stationList;
    }

    /**
     * 补全没数据的积水点
     *
     * @param defineList
     * @param realList
     */
    public void fillLostedData(List<AttFloodPoint> defineList, List<FloodPoint> realList) {
        List<String> hasDataNameList = realList.stream().map(FloodPoint::getName).distinct().collect(Collectors.toList());
        Map<String, AttFloodPoint> totalMap = defineList.stream().collect(Collectors.toMap(AttFloodPoint::getName, Function.identity()));

        List<String> noDataNameList = CollectionUtils.getExcept(new ArrayList<>(totalMap.keySet()), hasDataNameList);
        if (CollectionUtils.isNotBlank(noDataNameList)) {
            for (String name : noDataNameList) {
                AttFloodPoint tempTotal = totalMap.get(name);
                if (tempTotal != null) {
                    realList.add(Builder.of(FloodPoint::new)
                            .with(FloodPoint::setStreetName, tempTotal.getStreetName())
                            .with(FloodPoint::setStreetCode, tempTotal.getStreetCode())
                            .with(FloodPoint::setAreaName, tempTotal.getAreaName())
                            .with(FloodPoint::setAreaCode, tempTotal.getAreaCode())
                            .with(FloodPoint::setLatitude, tempTotal.getLatitude())
                            .with(FloodPoint::setLongitude, tempTotal.getLongitude())
                            .with(FloodPoint::setName, tempTotal.getName())
                            .with(FloodPoint::setWaterLevel, BigDecimal.ZERO)
                            .with(FloodPoint::setWaterLevelUnit, "厘米")
                            .build());
                }
            }
        }
    }

    /**
     * 积水点分页
     *
     * @param reqVo
     * @return
     */
    @Override
    public PageVO<AttFloodPoint> pageFloodPoint(FloodPointReqVo reqVo) {
        return PageUtils.pageInRam(reqVo, listFloodPoint(reqVo));
    }

    /**
     * 积水点分页统计
     *
     * @param reqVo
     * @return
     */
    @Override
    public JSONObject stats4PageFloodPoint(FloodPointReqVo reqVo) {
        JSONObject result = new JSONObject();
        List<AttFloodPoint> list = listFloodPoint(reqVo);
        result.put("total", list.size());
        result.put("noWater", list.stream().filter(o -> !o.isPonding()).count());
        result.put("hasWater", list.stream().filter(AttFloodPoint::isPonding).count());
        return result;
    }

    /**
     * 积水点组件C位统计
     *
     * @param dataTime
     * @return
     */
    @Override
    public JSONObject floodPointCWindows(Date dataTime) {
        JSONObject result = new JSONObject();
//        Optional<SingletonMap<String, Long>> maxTimeValOpt = Optional.ofNullable(this.floodPointMapper.maxPondingTimes(dataTime));
//        List<AttFloodPoint> list = listFloodPoint(Builder.of(FloodPointReqVo::new).with(FloodPointReqVo::setDataTime, dataTime).build());
//        result.put("hasWater", list.stream().filter(AttFloodPoint::isPonding).count());
//        String maxPondingTimesName = StringUtils.EMPTY;
//        Long maxPondingTimes = 0L;
//        if (maxTimeValOpt.isPresent()) {
//            maxPondingTimes = maxTimeValOpt.get().getValue();
//            maxPondingTimesName = maxTimeValOpt.get().getKey();
//        }
//        result.put("maxPondingTimesName", maxPondingTimesName);
//        result.put("maxPondingTimes", maxPondingTimes);
        return result;
    }

    /**
     * 积水点积水次数热力图
     *
     * @param dataTime
     * @return
     */
    @Override
    public List<AttFloodPoint> floodPointHeatMap(Date dataTime) {
        List<AttFloodPoint> stationList = floodPointMapper.selectFloodPointDefine(Builder.of(FloodPointReqVo::new).with(FloodPointReqVo::setDataTime, dataTime).build());
        List<SingletonMap<String, Long>> pondingTimeList = this.floodPointMapper.pondingTimes(dataTime);
        Map<String, Long> map = SingletonMap.list2Map(pondingTimeList);
        for (AttFloodPoint attFloodPoint : stationList) {
            attFloodPoint.setPondingTimes(map.getOrDefault(attFloodPoint.getName(), 0L).intValue());
        }
        return stationList;
    }

    @Override
    public List<ChartResVo<Date, BigDecimal>> floodPointHistoricCurve(String pointName, Integer hours) {
        if (StringUtils.isNotBlank(pointName)) {
            Date start = null;
            Date end = null;
            hours = hours == null ? 24 : hours;
            LocalDateTime origin = LocalDateTime.now().plusHours(1).withMinute(0);
            start = DateUtils.localDateTime2Date(origin.minusHours(hours));
            end = DateUtils.localDateTime2Date(origin);
            List<FloodPoint> floodPoints = floodPointMapper.selectByName(pointName, start, end);
            AttFloodPoint floodPointDefine = floodPointMapper.loadFloodPointDefine(pointName);
            BigDecimal highestLevel = null;
            if (floodPointDefine != null) {
                highestLevel = floodPointDefine.getHighestLevel();
            }

            if (CollectionUtils.isNotBlank(floodPoints)) {
                List<ChartResVo<Date, BigDecimal>> list = new ArrayList<>(floodPoints.size());
                for (FloodPoint floodPoint : floodPoints) {
                    list.add(Builder.of(ChartResVo<Date, BigDecimal>::new)
                            .with(ChartResVo::setKey, floodPoint.getAcquisitionTime())
                            .with(ChartResVo::setValue, floodPoint.getWaterLevel())
                            .with(ChartResVo::setRemark, highestLevel)
                            .build());
                }
                return list;
            }
        }
        return Collections.emptyList();
    }

    @Override
    public List<ChartResVo<String, List<ChartResVo<Date, BigDecimal>>>> floodPointCurveList(Integer limit) {
        if (limit == null) {
            limit = 3;
        }
        List<AttFloodPoint> floodPoints = listFloodPoint(new FloodPointReqVo());
        List<String> nameList = null;
        if (CollectionUtils.isNotBlank(floodPoints)) {
            //按水深降序
            nameList = floodPoints.stream().sorted(Comparator.comparing(AttFloodPoint::getWaterLevel).reversed()).limit(limit).map(AttFloodPoint::getName).collect(Collectors.toList());
        }
        if (CollectionUtils.isBlank(nameList)) {
            return null;
        } else {
            List<ChartResVo<String, List<ChartResVo<Date, BigDecimal>>>> result = new ArrayList<>(limit);
            for (String name : nameList) {
                result.add(Builder.of(ChartResVo<String, List<ChartResVo<Date, BigDecimal>>>::new)
                        .with(ChartResVo::setKey, name)
                        .with(ChartResVo::setValue, floodPointCurve(name))
                        .build());
            }
            return result;
        }
    }

    /**
     * 积水点统计
     *
     * @param type 1. 根据行政区划统计积水点测站数量
     *             2. 根据流域统计
     *             3：统计无数据、无积水、有积水的点位数量，
     *             4.
     *             5.
     *             6 按行政区划统计，但是不带没有积水点的行政区划
     */
    @Override
    public Object floodPointStatistic(int type) {
        switch (type) {
            case 1: {
                List<AttAdcdBase> adcdList = adcdBaseMapper.selectCountyOfWz();
                List<ChartResVo<String, Long>> records = this.floodPointMapper.countByAreaName();
                List<ChartResVo<String, Long>> result = new ArrayList<>(records.size());
                Map<String, Long> recordMap = records.stream().collect(Collectors.toMap(ChartResVo::getKey, ChartResVo::getValue));
                for (AttAdcdBase adcd : adcdList) {
                    result.add(Builder.of(ChartResVo<String, Long>::new)
                            .with(ChartResVo::setKey, adcd.getAdnm())
                            .with(ChartResVo::setValue, recordMap.getOrDefault(adcd.getAdnm(), 0L))
                            .build());
                }
                return result.stream().sorted(Comparator.comparing(ChartResVo<String, Long>::getValue).reversed()).collect(Collectors.toList());
            }
            case 2: {
                List<AttDictResVo> dictList = attDictService.selectDict(Builder.of(AttDictRequestVo::new)
                        .with(AttDictRequestVo::setDictCode, "basin").build());
                List<ChartResVo<String, Long>> records = this.floodPointMapper.countByBasin();
                List<ChartResVo<String, Long>> result = new ArrayList<>(records.size());
                Map<String, Long> recordMap = records.stream().collect(Collectors.toMap(ChartResVo::getKey, ChartResVo::getValue));
                for (AttDictResVo dict : dictList) {
                    result.add(Builder.of(ChartResVo<String, Long>::new)
                            .with(ChartResVo::setKey, dict.getDictName())
                            .with(ChartResVo::setValue, recordMap.getOrDefault(dict.getDictName(), 0L))
                            .build());
                }
                return result.stream().sorted(Comparator.comparing(ChartResVo<String, Long>::getValue).reversed()).collect(Collectors.toList());
            }
            case 3: {
                List<AttFloodPoint> floodPoints = listFloodPoint(new FloodPointReqVo());
                floodPoints = floodPoints == null ? Collections.emptyList() : floodPoints;
                Map<String, List<AttFloodPoint>> map = floodPoints.stream().collect(Collectors.groupingBy(o -> {
                    if (o.isNoData()) {
                        return "无数据";
                    } else if (o.getWaterLevel() != null && o.getWaterLevel().doubleValue() > 0) {
                        return "有积水";
                    } else {
                        return "无积水";
                    }
                }));

                List<ChartResVo<String, Long>> result = new ArrayList<>(3);
                String[] title = {"无数据", "无积水", "有积水"};
                for (String key : title) {
                    List<AttFloodPoint> values = map.get(key);
                    values = values == null ? Collections.emptyList() : values;
                    result.add(Builder.of(ChartResVo<String, Long>::new)
                            .with(ChartResVo::setKey, key)
                            .with(ChartResVo::setValue, (long) values.size())
                            .with(ChartResVo::setRemark, values.stream().map(AttFloodPoint::getName).collect(Collectors.joining(",")))
                            .build());
                }
                return result;
            }
            case 4: {
                List<AttAdcdBase> adcdList = adcdBaseMapper.selectCountyOfWz();
                adcdList = adcdList == null ? Collections.emptyList() : adcdList;
                List<AttFloodPoint> floodPoints = listFloodPoint(Builder.of(FloodPointReqVo::new).with(FloodPointReqVo::setRecentDays, 14).build());
                floodPoints = floodPoints == null ? Collections.emptyList() : floodPoints;
                // 将积水点按行政区划分组
                String areaCodeEnd = "000000";
                Map<String, List<AttFloodPoint>> floodPointMap = floodPoints.stream().collect(Collectors.groupingBy(o -> {
                    if (StringUtils.isNotBlank(o.getAreaCode()) && o.getAreaCode().length() > 6) {
                        // 大数据局的洞头区和我们的洞头区行政区划编码不一致
                        if ("330305000000".equals(o.getAreaCode())) {
                            return "330322000000";
                        } else {
                            return o.getAreaCode().substring(0, 6) + areaCodeEnd;
                        }
                    } else {
                        return StringUtils.EMPTY;
                    }
                }));
                List<ChartResVo<String, Map<String, Object>>> result = new ArrayList<>(adcdList.size());
                for (AttAdcdBase adcd : adcdList) {
                    ChartResVo<String, Map<String, Object>> tempRes = new ChartResVo<>(adcd.getAdnm(), null);
                    // 拿到该行政区划下的积水点
                    List<AttFloodPoint> tempFloodPoints = floodPointMap.get(adcd.getAdcd());
                    tempFloodPoints = tempFloodPoints == null ? Collections.emptyList() : tempFloodPoints;
                    // 有积水数据
                    List<AttFloodPoint> overList = tempFloodPoints.stream().filter(o -> BigDecimal.ZERO.compareTo(o.getWaterLevel()) < 0).collect(Collectors.toList());
                    // 无积水数据
                    List<AttFloodPoint> normalList = tempFloodPoints.stream().filter(o -> BigDecimal.ZERO.compareTo(o.getWaterLevel()) >= 0).collect(Collectors.toList());
                    tempRes.setValue(Builder.of(HashMap<String, Object>::new)
                            .with(HashMap::put, "totalNum", tempFloodPoints.size())
                            .with(HashMap::put, "totalRemark", tempFloodPoints.stream().map(AttFloodPoint::getName).collect(Collectors.joining(",")))
                            .with(HashMap::put, "normalNum", normalList.size())
                            .with(HashMap::put, "normalRemark", normalList.stream().map(AttFloodPoint::getName).collect(Collectors.joining(",")))
                            .with(HashMap::put, "overNum", overList.size())
                            .with(HashMap::put, "overRemark", overList.stream().map(AttFloodPoint::getName).collect(Collectors.joining(",")))
                            .build());
                    result.add(tempRes);
                }
                return result;
            }
            case 5: {
                Map<String, Object> result = new HashMap<>();
                // 查出流域与积水点名称
                List<ChartResVo<String, String>> attRecord = this.floodPointMapper.selectNameBasinFromAttFloodPoint();
                attRecord = attRecord == null ? Collections.emptyList() : attRecord;
                // 积水点名称和流域的对应关系
                Map<String, String> nameBasinMap = attRecord.stream()
                        .filter(o -> StringUtils.isNotBlank(o.getValue()))
                        .collect(Collectors.toMap(ChartResVo::getKey, ChartResVo::getValue));


                List<AttFloodPoint> floodPoints = listFloodPoint(Builder.of(FloodPointReqVo::new).with(FloodPointReqVo::setRecentDays, 10).build());
                floodPoints = floodPoints == null ? Collections.emptyList() : floodPoints;
                // 将积水点名称根据流域分组
                Map<String, List<AttFloodPoint>> floodPointMap = floodPoints.stream().collect(Collectors.groupingBy(o -> {
                    String basin = nameBasinMap.get(o.getName());
                    if (StringUtils.isBlank(basin)) {
                        return StringUtils.EMPTY;
                    }
                    boolean normal = BigDecimal.ZERO.compareTo(o.getWaterLevel()) >= 0;
                    // 前端固定要，所以不要走数据字典
                    switch (basin.trim()) {
                        case "飞云江":
                            return normal ? "fyjNormal" : "fyjOver";
                        case "鳌江":
                            return normal ? "ajNormal" : "ajOver";
                        case "瓯江":
                            return normal ? "ojNormal" : "ojOver";
                        default:
                            return StringUtils.EMPTY;
                    }
                }));

                result.put("fyjNormalNum", floodPointMap.getOrDefault("fyjNormal", Collections.emptyList()).size());
                result.put("fyjNormalRemark", floodPointMap.getOrDefault("fyjNormal", Collections.emptyList()).stream().map(AttFloodPoint::getName).collect(Collectors.joining(",")));

                result.put("fyjOverNum", floodPointMap.getOrDefault("fyjOver", Collections.emptyList()).size());
                result.put("fyjOverRemark", floodPointMap.getOrDefault("fyjOver", Collections.emptyList()).stream().map(AttFloodPoint::getName).collect(Collectors.joining(",")));

                result.put("ajNormalNum", floodPointMap.getOrDefault("ajNormal", Collections.emptyList()).size());
                result.put("ajNormalRemark", floodPointMap.getOrDefault("ajNormal", Collections.emptyList()).stream().map(AttFloodPoint::getName).collect(Collectors.joining(",")));

                result.put("ajOverNum", floodPointMap.getOrDefault("ajOver", Collections.emptyList()).size());
                result.put("ajOverRemark", floodPointMap.getOrDefault("ajOver", Collections.emptyList()).stream().map(AttFloodPoint::getName).collect(Collectors.joining(",")));

                result.put("ojNormalNum", floodPointMap.getOrDefault("ojNormal", Collections.emptyList()).size());
                result.put("ojNormalRemark", floodPointMap.getOrDefault("ojNormal", Collections.emptyList()).stream().map(AttFloodPoint::getName).collect(Collectors.joining(",")));

                result.put("ojOverNum", floodPointMap.getOrDefault("ojOver", Collections.emptyList()).size());
                result.put("ojOverRemark", floodPointMap.getOrDefault("ojOver", Collections.emptyList()).stream().map(AttFloodPoint::getName).collect(Collectors.joining(",")));
                return result;
            }
            case 6: {
                List<AttAdcdBase> adcdList = adcdBaseMapper.selectCountyOfWz();
                adcdList = adcdList == null ? Collections.emptyList() : adcdList;
                List<AttFloodPoint> attFloodPoints = listFloodPoint(new FloodPointReqVo());
                attFloodPoints = attFloodPoints == null ? Collections.emptyList() : attFloodPoints;
                // 将积水点按行政区划分组
                String areaCodeEnd = "000000";
                Map<String, List<AttFloodPoint>> attFloodPointMap = attFloodPoints.stream().collect(Collectors.groupingBy(o -> {
                    if (StringUtils.isNotBlank(o.getAreaCode()) && o.getAreaCode().length() > 6) {
                        // 大数据局的洞头区和我们的洞头区行政区划编码不一致
                        if ("330305000000".equals(o.getAreaCode())) {
                            return "330322000000";
                        } else {
                            return o.getAreaCode().substring(0, 6) + areaCodeEnd;
                        }
                    } else {
                        return StringUtils.EMPTY;
                    }
                }));
                List<ChartResVo<String, Map<String, Object>>> result = new ArrayList<>(adcdList.size());
                for (AttAdcdBase adcd : adcdList) {
                    ChartResVo<String, Map<String, Object>> tempRes = new ChartResVo<>(adcd.getAdnm(), null);
                    // 拿到该行政区划下的积水点
                    List<AttFloodPoint> tempDefineList = attFloodPointMap.get(adcd.getAdcd());
                    if (CollectionUtils.isBlank(tempDefineList)) {
                        continue;
                    }
                    // 有积水数据
                    List<AttFloodPoint> overList = tempDefineList.stream().filter(o -> BigDecimal.ZERO.compareTo(o.getWaterLevel()) < 0).collect(Collectors.toList());
                    // 无积水数据
                    List<AttFloodPoint> normalList = tempDefineList.stream().filter(o -> BigDecimal.ZERO.compareTo(o.getWaterLevel()) >= 0).collect(Collectors.toList());
                    tempRes.setValue(Builder.of(HashMap<String, Object>::new)
                            .with(HashMap::put, "totalNum", tempDefineList.size())
                            .with(HashMap::put, "totalRemark", tempDefineList.stream().map(AttFloodPoint::getName).collect(Collectors.joining(",")))
                            .with(HashMap::put, "normalNum", normalList.size())
                            .with(HashMap::put, "normalRemark", normalList.stream().map(AttFloodPoint::getName).collect(Collectors.joining(",")))
                            .with(HashMap::put, "overNum", overList.size())
                            .with(HashMap::put, "overRemark", overList.stream().map(AttFloodPoint::getName).collect(Collectors.joining(",")))
                            .build());
                    result.add(tempRes);
                }
                return result;
            }
            default:
                return Collections.emptyList();
        }
    }

    @Override
    public List<ChartResVo<Date, BigDecimal>> floodPointForecastCurve(String name) {
        List<ChartResVo<Date, BigDecimal>> result = new ArrayList<>(3);
        String expression = StringUtils.EMPTY;
        switch (name) {
            case "过境路口":
                expression = "0.000000448201*x^3-0.000479403*x^2+0.154888*x+0.740239";
                break;
            case "鹿城路高架":
                expression = "0.000000167153*x^3-0.000329659*x^2+0.152187*x+0.522169";
                break;
            case "望江西路鞋都大道口":
                expression = "0.000000464332*x^3-0.000541490*x^2+0.176124*x+1.039134";
                break;
            case "奋飞路口":
                expression = "-0.0000337737*x^3+0.00750831*x^2-0.302187*x+3.463351";
                break;
            default:
                break;
        }
        if (StringUtils.isBlank(expression)) {
            return Collections.emptyList();
        }
        FloodPoint floodPoint = this.floodPointMapper.selectNewestFloodPointByName(name);
        if (floodPoint == null) {
            return Collections.emptyList();
        }
        CommonResult<JSONObject> resp = meteorologicalClient.caculateRainfall(floodPoint.getLongitude(), floodPoint.getLatitude());
        LocalDateTime now = LocalDateTime.now();
        if (CommonCode.SUCCESS.code() == resp.getCode() && resp.getData() != null) {
            JSONObject data = resp.getData();
            try {
                result.add(Builder.of(ChartResVo<Date, BigDecimal>::new)
                        .with(ChartResVo::setKey, DateUtils.localDateTime2Date(now.plusHours(1)))
                        .with(ChartResVo::setValue, data.getBigDecimal("rainfall_1"))
//                        .with(ChartResVo::setValue, new BigDecimal(1.0))
                        .build());
            } catch (Exception e) {
            }
            try {
                result.add(Builder.of(ChartResVo<Date, BigDecimal>::new)
                        .with(ChartResVo::setKey, DateUtils.localDateTime2Date(now.plusHours(3)))
                        .with(ChartResVo::setValue, data.getBigDecimal("rainfall_3"))
//                        .with(ChartResVo::setValue, new BigDecimal(1.5))
                        .build());
            } catch (Exception e) {
            }
            try {
                result.add(Builder.of(ChartResVo<Date, BigDecimal>::new)
                        .with(ChartResVo::setKey, DateUtils.localDateTime2Date(now.plusHours(6)))
                        .with(ChartResVo::setValue, data.getBigDecimal("rainfall_6"))
//                        .with(ChartResVo::setValue, new BigDecimal(2.005))
                        .build());
            } catch (Exception e) {
            }
        }

        for (ChartResVo<Date, BigDecimal> val : result) {
            BigDecimal value = val.getValue();
            value = value == null ? BigDecimal.ZERO : value;
            String expr = expression.replace("x^3", String.valueOf(Math.pow(value.doubleValue(), 3))).replace("x^2", String.valueOf(Math.pow(value.doubleValue(), 2))).replace("x", value.toString());
            val.setValue(new BigDecimal(Calculator.conversion(expr)));
        }
        return result;
    }

    /**
     * @param name
     * @return
     */
    @Override
    public List<ChartResVo<Date, BigDecimal>> waterLoggingForecastCurve(String name) {
        List<ChartResVo<Date, BigDecimal>> result = new ArrayList<>(3);
        Integer rowIndex = null;
        Integer columnIndex = null;
        Map<Integer, WaterLoggingModel> modelMap = waterLoggingModelMap.get(name);
        switch (name) {
            case "过境路口":
//                Integer id=7905;
                rowIndex = 259;
                columnIndex = 594;
                break;
            case "鹿城路高架":
//                Integer id=7907;
                rowIndex = 259;
                columnIndex = 596;
                break;
            case "望江西路鞋都大道口":
//                Integer id=7907;
                rowIndex = 259;
                columnIndex = 596;
                break;
            case "奋飞路口":
//                Integer id=7906;
                rowIndex = 259;
                columnIndex = 595;
                break;
            case "帆影广场":
//                Integer id=7908;
                rowIndex = 259;
                columnIndex = 597;
                break;
            default:
                break;
        }
        if (rowIndex == null || columnIndex == null || CollectionUtils.isBlank(modelMap)) {
            return Collections.emptyList();
        }
        CommonResult<JSONObject> resp = meteorologicalClient.rainfallPredictByIndex(rowIndex, columnIndex);
        LocalDateTime now = LocalDateTime.now();
        if (CommonCode.SUCCESS.code() == resp.getCode() && resp.getData() != null) {
            JSONObject data = resp.getData();
            data.forEach((key, value) -> {
                LocalDateTime predictTime = null;
                if (key.endsWith("1")) {
                    predictTime = now.plusHours(1);
                } else if (key.endsWith("3")) {
                    predictTime = now.plusHours(3);
                } else if (key.endsWith("6")) {
                    predictTime = now.plusHours(6);
                }
                if (predictTime != null) {
                    Integer month = predictTime.getMonthValue();
                    WaterLoggingModel model = modelMap.get(month);
                    BigDecimal predictValue = doPredictByModel(model, predictTime, new BigDecimal(String.valueOf(value)));
                    if (predictValue != null) {
                        result.add(Builder.of(ChartResVo<Date, BigDecimal>::new)
                                .with(ChartResVo::setKey, DateUtils.localDateTime2Date(predictTime))
                                .with(ChartResVo::setValue, predictValue)
                                .build());
                    }
                }
            });
        }
        // 最后按时刻升序
        return result.stream().sorted(Comparator.comparing(ChartResVo<Date, BigDecimal>::getKey)).collect(Collectors.toList());
    }

    public BigDecimal doPredictByModel(WaterLoggingModel model, LocalDateTime predictTime, BigDecimal predictValue) {
        if (model == null || predictTime == null) {
            return null;
        }
        int effectMonth = model.getEffectMonth();
        String expression = model.getExpression();
        if (predictTime.getMonthValue() != effectMonth || StringUtils.isBlank(expression)) {
            return null;
        }

        BigDecimal value = predictValue == null ? BigDecimal.ZERO : predictValue;
        if (value.doubleValue() <= model.getEffectThreshold()) {
            return BigDecimal.ZERO;
        }
        String expr = expression.replace("x^3", "*" + String.valueOf(Math.pow(value.doubleValue(), 3))).replace("x^2", "*" + String.valueOf(Math.pow(value.doubleValue(), 2))).replace("x", "*" + value.toString());
        double temp = Calculator.conversion(expr);
        System.out.println(temp);
        return new BigDecimal(temp);
    }


    @Override
    public List<ChartResVo<Date, BigDecimal>> floodPointCurve(String pointName) {
        if (StringUtils.isNotBlank(pointName)) {
            List<FloodPoint> floodPoints = floodPointMapper.selectByName(pointName, null, null);
            if (CollectionUtils.isNotBlank(floodPoints)) {
                List<ChartResVo<Date, BigDecimal>> result = new ArrayList<>(floodPoints.size());
                for (FloodPoint floodPoint : floodPoints) {
                    result.add(Builder.of(ChartResVo<Date, BigDecimal>::new)
                            .with(ChartResVo::setKey, floodPoint.getAcquisitionTime())
                            .with(ChartResVo::setValue, floodPoint.getWaterLevel())
                            .build());
                }
                return result;
            }
        }
        return Collections.emptyList();
    }

    DateTimeFormatter dftTable = DateTimeFormatter.ofPattern("yyyyMM");

    @Override
    public List<StBase> riverWaterLevel(String basin) {
//        List<StBase> stBases = this.stationMapper.loadStByMark("内河", null);
        List<StBase> stBases = stationMapper.loadStByCondtion(Builder.of(StQueryReqVo::new)
                .with(StQueryReqVo::setStType, StationType.RIVER.type())
                .with(StQueryReqVo::setBas, basin)
                .build());
        stBases = stBases.stream().filter(o -> StationType.RIVER.type().equals(o.getStType())).collect(Collectors.toList());
        Map<String, Double> wlMap = new HashMap<>(stBases.size());
        Map<String, String> areaMap = Collections.EMPTY_MAP;
        if (CollectionUtils.isNotBlank(stBases)) {
            String yyyyMM = LocalDate.now().format(dftTable);
            String tableName = StationType.RIVER.table() + yyyyMM;
            List<String> stcds = stBases.stream().map(StBase::getStCode).collect(Collectors.toList());
            List<StRiverR> realWaterLevels = waterAndRainMapper.selectNewestRiverLevel(Collections.singletonList(tableName), stcds);
            Map<String, Double> tempMap = realWaterLevels.stream().filter(rwl -> StringUtils.isNotBlank(rwl.getStcd()))
                    .collect(Collectors.toMap(StRiverR::getStcd, StRiverR::getZ));
            wlMap.putAll(tempMap);
            String areaCodes = stBases.stream().map(StBase::getAreaCode).filter(StringUtils::isNotBlank).collect(Collectors.joining(","));
            if (StringUtils.isNotBlank(areaCodes)) {
                List<AttAdcdBase> temp = this.adcdBaseMapper.selectAdcdNameByAdcds(areaCodes);
                if (CollectionUtils.isNotBlank(tempMap)) {
                    areaMap = temp.stream().collect(Collectors.toMap(AttAdcdBase::getAdcd, AttAdcdBase::getAdnm));
                }
            }
        }
        for (StBase stBase : stBases) {
            stBase.setWaterLevel(wlMap.getOrDefault(stBase.getStCode(), 0D));
            if (stBase.getWarningLevel() != null) {
                stBase.setOverWarnLevel(stBase.getWaterLevel() - stBase.getWarningLevel());
                stBase.setWarning(stBase.getOverWarnLevel() >= 0);
            } else {
                stBase.setWarning(false);
            }
            stBase.setOverGuarantee(stBase.getGuaranteeLevel() != null && stBase.getWaterLevel() >= stBase.getGuaranteeLevel());
            if (StringUtils.isNotBlank(stBase.getAreaCode())) {
                stBase.setAreaName(areaMap.getOrDefault(stBase.getAreaCode(), StringUtils.EMPTY));
            }
        }
        return stBases.stream()
                .sorted((o1, o2) -> {
                    if (o1.getOverWarnLevel() != null && o2.getOverWarnLevel() != null) {
                        return o2.getOverWarnLevel().compareTo(o1.getOverWarnLevel());
                    } else if (o1.getOverWarnLevel() == null && o2.getOverWarnLevel() != null) {
                        return -1;
                    } else if (o1.getOverWarnLevel() != null && o2.getOverWarnLevel() == null) {
                        return 1;
                    } else {
                        return 0;
                    }
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<StBase> wagaList(String basin) {
        List<StBase> stBases = stationMapper.loadStByCondtion(Builder.of(StQueryReqVo::new)
                .with(StQueryReqVo::setStType, StationType.GATE.type())
                .with(StQueryReqVo::setBas, basin)
                .build());
        Map<String, StWasR> wlMap = new HashMap<>(stBases.size());
        if (CollectionUtils.isNotBlank(stBases)) {
            String yyyyMM = LocalDate.now().format(dftTable);
            String tableName = StationType.GATE.table() + yyyyMM;
            List<String> stcds = stBases.stream().map(StBase::getStCode).collect(Collectors.toList());
            List<StWasR> realWaterLevels = waterAndRainMapper.selectNewestWagaWaterLevel(tableName, stcds);
            Map<String, StWasR> tempMap = realWaterLevels.stream().filter(rwl -> StringUtils.isNotBlank(rwl.getStcd()))
                    .collect(Collectors.toMap(StWasR::getStcd, o -> o));
            wlMap.putAll(tempMap);
        }
        for (StBase stBase : stBases) {
            StWasR value = wlMap.get(stBase.getStCode());
            if (value != null) {
                stBase.setUpz(value.getUpz());
                stBase.setDwz(value.getUpz());
                stBase.setOpenGate(value.getOvs());
                stBase.setFlow(value.getMxgtq());
                if (stBase.getWarningLevel() == null) {
                    // 这个设置为0，使得先显示超警闸站再显示温瑞平
                    stBase.setOverWarnLevel(0D);
                    stBase.setWarning(false);
                } else {
                    stBase.setOverWarnLevel(stBase.getUpz() - stBase.getWarningLevel());
                    stBase.setWarning(stBase.getOverWarnLevel() >= 0);
                }
            } else {
                stBase.setOverWarnLevel(Double.MIN_VALUE);
                stBase.setWarning(false);
            }
            if (stBase.getOpenGate() == null) {
                stBase.setOpenGate(StringUtils.EMPTY);
            }
        }
        return stBases.stream()
                .sorted(Comparator.comparing(StBase::getOpenGate).reversed().thenComparing(StBase::getOverWarnLevel))
                .collect(Collectors.toList());
    }

    @Override
    public CommonResult floodRisk() {
        return null;
    }

    @Override
    public JSONObject warnNum() {
        JSONObject result = new JSONObject();
        List<AttFloodPoint> floodPoints = listFloodPoint(new FloodPointReqVo());
        floodPoints = floodPoints == null ? Collections.emptyList() : floodPoints;
        int pondingNum = (int) floodPoints.stream().filter(AttFloodPoint::isPonding).count();
        List<StBase> rivers = riverWaterLevel(null);
        int riverWarnNum = (int) rivers.stream().filter(StBase::isWarning).count();
        List<StBase> wagas = wagaList(null);
        int wagaWarnNum = (int) wagas.stream().filter(StBase::isWarning).count();
        result.put("pondingNum", pondingNum);
        result.put("riverWarnNum", riverWarnNum);
        result.put("wagaWarnNum", wagaWarnNum);
        return result;
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        waterLoggingModelList = waterLoggingModelList();
        waterLoggingModelMap = waterLoggingModelList.stream().collect(Collectors.groupingBy(WaterLoggingModel::getWaterLoggingName, Collectors.toMap(WaterLoggingModel::getEffectMonth, Function.identity())));
    }

    private List<WaterLoggingModel> waterLoggingModelList() {
        List<WaterLoggingModel> list = new ArrayList<>();
        list.add(new WaterLoggingModel("过境路口", "-0.007696x^3+0.485327x^2-8.666248x+47.540552", 7, 13.12D));
        list.add(new WaterLoggingModel("过境路口", "-0.00005741x^3+0.002259x^2+0.356675x-1.119357", 8, 3.08D));
        list.add(new WaterLoggingModel("鹿城路高架", "-0.000501x^3+0.039228x^2-0.747735x+4.614896", 6, 12.55D));
        list.add(new WaterLoggingModel("鹿城路高架", "-0.000598x^3+0.055819x^2-0.87061x+3.878954", 7, 9.14D));
        list.add(new WaterLoggingModel("鹿城路高架", "-0.000129x^3+0.01152x^2+0.070516x+0.206446", 8, 8.5D));
        list.add(new WaterLoggingModel("望江西路鞋都大道口", "-0.0000954x^3+0.011455x^2-0.366812x+4.467876", 6, 22.13D));
        list.add(new WaterLoggingModel("望江西路鞋都大道口", "-0.000118x^3+0.006024x^2+0.056966x+0.230532", 7, 7.5D));
        list.add(new WaterLoggingModel("望江西路鞋都大道口", "-0.00017x^3+0.02071x^2-0.361851x+2.271139", 8, 9.96D));
        list.add(new WaterLoggingModel("奋飞路口", "-0.00009945x^3+0.00772x^2-0.104675x+1.262431", 6, 8.02D));
        list.add(new WaterLoggingModel("奋飞路口", "-0.00354x^3+0.207229x^2-2.8452x+10.564043", 7, 11.75D));
        list.add(new WaterLoggingModel("奋飞路口", "-0.000114x^3+0.000861x^2+0.232423x-0.118358", 8, 6.12D));
        list.add(new WaterLoggingModel("帆影广场", "-0.000006855x^3+0.000678x^2+0.022471x+0.831903", 6, 12D));
        list.add(new WaterLoggingModel("帆影广场", "0.00007483x^3-0.01125x^2+0.450763x-1.404767", 7, 3.40D));
        list.add(new WaterLoggingModel("帆影广场", "-0.000652x^3+0.08749x^2-3.310176x+39.350029", 8, 27.17D));
        return list;
    }
}
