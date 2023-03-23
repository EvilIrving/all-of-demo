package com.ygkj.big.screen.drill;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.ygkj.big.screen.mapper.WaterAndRainMapper;
import com.ygkj.big.screen.mapper.AttPrevTfBaseMapper;
import com.ygkj.big.screen.mapper.ResultsWarnIndicatorMapper;
import com.ygkj.big.screen.model.*;
import com.ygkj.big.screen.vo.request.DrillParamReqVo;
import com.ygkj.big.screen.vo.request.MountainTorrentDrillReqVo;
import com.ygkj.big.screen.vo.request.MountainTorrentReqVo;
import com.ygkj.big.screen.vo.request.PrevTfQueryVo;
import com.ygkj.big.screen.vo.response.*;
import com.ygkj.enums.StationType;
import com.ygkj.service.GenerateDistributionIDService;
import com.ygkj.utils.Builder;
import com.ygkj.utils.CollectionUtils;
import com.ygkj.utils.DateUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationContext;
import ygkj.com.util.DateUtil;
import ygkj.com.util.MydateUtil;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toCollection;

/**
 * @author xq
 * @Description
 * @Date 2021/7/9
 */
public class MountainTorrentDrillStrategy extends AbstractDrillStrategy {

    private AttPrevTfBaseMapper attPrevTfBaseMapper;

    private ResultsWarnIndicatorMapper resultsWarnIndicatorMapper;

    private WaterAndRainMapper waterAndRainMapper;

    GenerateDistributionIDService idService;

//    Map<String, String> floodWarnStatus = new HashMap<>();//0:准备转移 1:立即转移
//    List<AttPrevTfBase> attPrevTfBasesAll = new ArrayList<>();

    public MountainTorrentDrillStrategy(ApplicationContext context) {
        super(context);
        attPrevTfBaseMapper = context.getBean(AttPrevTfBaseMapper.class);
        resultsWarnIndicatorMapper = context.getBean(ResultsWarnIndicatorMapper.class);
        waterAndRainMapper = context.getBean(WaterAndRainMapper.class);
        idService = context.getBean(GenerateDistributionIDService.class);
    }

    @Override
    public Map<String, List<UnitDataResVo>> buildData(DrillParamReqVo reqVo, Date endTime) {
        /**
         * 1. 查出所有的村庄的报警情况
         * 2. 统计报警
         * 3. 将报警和落点分组。
         */
        BsDrillCase drillCase = reqVo.getDrillCase();
        String dataGroupId = reqVo.getDataGroupId();
        int timeIndex = reqVo.getTimeIndex();
        List<BsDrillStcase> stcaseList = reqVo.getStcaseList();
        String stcds = stcaseList.stream().map(BsDrillStcase::getDrillStCode).collect(Collectors.joining(com.ygkj.utils.StringUtils.JOIN_DELIMITER));

        MountainTorrentDrillResVo result = new MountainTorrentDrillResVo();
        List<MountainTorrentDetailResVo> allVillages = mountainTorrentWarningInfo(Builder.of(MountainTorrentDrillReqVo::new)
                .with(MountainTorrentDrillReqVo::setDataTime, endTime)
                .with(MountainTorrentDrillReqVo::setStartTime, drillCase.getStartTime())
                .with(MountainTorrentDrillReqVo::setStcds, stcds)
                .build());

//        String status = floodWarnStatus.get(attPrevTfBase.getLat() + "_" + attPrevTfBase.getLon());
//        if ("1".equals(status)) {////0:准备转移 1:立即转移   立即转移直接返回
//            List<AttPrevTfBase> collect = attPrevTfBasesAll.stream().filter(e -> e.getLon().equals(attPrevTfBase.getLon())
//                    && e.getLat().equals(attPrevTfBase.getLat()) && "5".equals(e.getWarnGradeID())).collect(Collectors.toList());
//            if (collect.size() > 0) {
//                attPrevTfBasesCp.add(collect.get(0));
//            }
//            continue;
//        } else if ("0".equals(status)) {
//            List<AttPrevTfBase> collect = attPrevTfBasesAll.stream().filter(e -> e.getLon().equals(attPrevTfBase.getLon()) &&
//                    e.getLat().equals(attPrevTfBase.getLat()) && "4".equals(e.getWarnGradeID())).collect(Collectors.toList());
//            if (collect.size() > 0) {
//                attPrevTfBasesCp.add(collect.get(0));
//            }
//        }
        // nnd ，全是重复数据
        allVillages = allVillages.stream().collect(
                collectingAndThen(toCollection(() -> new TreeSet<>(Comparator.comparing(this::getUnitKey))), ArrayList::new)
        );
        Map<String, MountainTorrentDetailResVo> allMap = allVillages.stream().collect(Collectors.toMap(this::getUnitKey, Function.identity()));

        List<MountainTorrentDetailResVo> grad5Villages = new ArrayList<>();
        List<MountainTorrentDetailResVo> grad4Villages = new ArrayList<>();
        allMap.forEach((lngLatKey, village) -> {

//            if (StringUtils.isBlank(village.getWarnGradeID())){
//                String status = floodWarnStatus.get(lngLatKey);
//                if ("1".equals(status)){
//                    village.setWarnGradeID("5");
//                    grad5Villages.add(village);
//                    floodWarnStatus.put(lngLatKey,"1");
//                }else if ("0".equals(status)){
//                    village.setWarnGradeID("4");
//                    grad4Villages.add(village);
//                    floodWarnStatus.put(lngLatKey,"0");
//                }
//            }else if ("5".equals(village.getWarnGradeID())){
//                grad5Villages.add(village);
//                floodWarnStatus.put(lngLatKey,"1");
//            }else if ("4".equals(village.getWarnGradeID())){
//                String status = floodWarnStatus.get(lngLatKey);
//                if ("1".equals(status)){
//                    village.setWarnGradeID("5");
//                    grad5Villages.add(village);
//                    floodWarnStatus.put(lngLatKey,"1");
//                }else {
//                    grad4Villages.add(village);
//                    floodWarnStatus.put(lngLatKey,"0");
//                }
//            }
            if ("5".equals(village.getWarnGradeID())) {
                grad5Villages.add(village);
            } else if ("4".equals(village.getWarnGradeID())) {
                grad4Villages.add(village);
            }
        });
//        Map<String, List<MountainTorrentDetailResVo>> groupMap = allVillages.stream().collect(Collectors.groupingBy(o -> StringUtils.isBlank(o.getWarnGradeID()) ? "norm" : o.getWarnGradeID()));
//        List<MountainTorrentDetailResVo> normVillages = groupMap.getOrDefault("norm", Collections.emptyList());
//        List<MountainTorrentDetailResVo> grad5Villages = groupMap.getOrDefault("5", Collections.emptyList());
//        List<MountainTorrentDetailResVo> grad4Villages = groupMap.getOrDefault("4", Collections.emptyList());
        List<MountainTorrentDetailResVo> warnVillages = new ArrayList<>(grad4Villages.size() + grad5Villages.size());
        warnVillages.addAll(grad5Villages);
        warnVillages.addAll(grad4Villages);

        // 统计山洪报警数据
        MountainTorrentWarnStatsResVo warnStats = new MountainTorrentWarnStatsResVo();
        Integer grade4County = 0;   //准备转移区县
        Integer grade4villages = 0; //准备转移村落
        Integer grade4street = 0;   //准备转移乡镇
        Integer grade4Person = 0;   //准备转移人口

        Integer grade5County = 0;    //立即转移区县
        Integer grade5villages = 0;  //立即转移村落
        Integer grade5street = 0;    //立即转移乡镇
        Integer grade5Person = 0;    //立即转移人口
        if (!warnVillages.isEmpty()) {
            List<ResultsWarnVo> resultsWarnVos = new ArrayList<>();
            ResultsWarnVo resultsWarnVo = null;
            Map<String, List<AttPrevTfBase>> townGroupMap = warnVillages.stream().collect(Collectors.groupingBy(AttPrevTfBase::getTown));
            for (Map.Entry<String, List<AttPrevTfBase>> townGroupMapEntry : townGroupMap.entrySet()) {
                resultsWarnVo = new ResultsWarnVo();
                String key = townGroupMapEntry.getKey();
                List<AttPrevTfBase> value = townGroupMapEntry.getValue();
                List<Villages> villagesList = new ArrayList<>();
                Villages villages = null;
                for (AttPrevTfBase attPrevTfBase : value) {
                    List<AttPrevTfBase> collect = warnVillages.stream().filter(e -> e.getLon()
                            .equals(attPrevTfBase.getLon()) && e.getLat().equals(attPrevTfBase.getLat())).collect(Collectors.toList());
                    if (!collect.isEmpty()) {
                        AttPrevTfBase attPrevTfBase1 = collect.get(0);
                        Integer tpo = attPrevTfBase1.getTpo();
                        if ("5".equals(attPrevTfBase.getWarnGradeID())) {
                            grade5County++;
                            grade5villages++;
                            grade5street++;
                            grade5Person += tpo;
                        } else if ("4".equals(attPrevTfBase.getWarnGradeID())) {
                            grade4County++;
                            grade4villages++;
                            grade4street++;
                            grade4Person += tpo;
                        }
                        villages = new Villages();
                        villages.setThresholdTime(attPrevTfBase.getThresholdTime());
                        villages.setThresholdData(attPrevTfBase.getThresholdData());
                        villages.setAdnm(attPrevTfBase.getAvi());
                        villages.setLat(attPrevTfBase.getLat() + "");
                        villages.setLng(attPrevTfBase.getLon() + "");
                        villages.setWarnGradeID(attPrevTfBase.getWarnGradeID());
                        villages.setEffectPersonNumber(attPrevTfBase.getTpo());
                        villagesList.add(villages);
                    }
                }
                resultsWarnVo.setVillages(villagesList);
                resultsWarnVo.setAdnm(key);
                resultsWarnVos.add(resultsWarnVo);
            }
            warnStats.setTown(resultsWarnVos);
            warnStats.setPromptlyMoveVillage(StringUtils.EMPTY);
            warnStats.setEffectPeople(StringUtils.EMPTY);
            warnStats.setWarningTown(StringUtils.EMPTY);
            warnStats.setWarningMoveVillage(StringUtils.EMPTY);
            warnStats.setRows(null);
        }
        warnStats.setGrade5County(grade5County);
        warnStats.setGrade5villages(grade5villages);
        warnStats.setGrade5street(grade5street);
        warnStats.setGrade5Person(grade5Person);

        warnStats.setGrade4County(grade4County);
        warnStats.setGrade4villages(grade4villages);
        warnStats.setGrade4street(grade4street);
        warnStats.setGrade4Person(grade4Person);
        warnStats.setGradevillages(grade5villages + grade4villages);

        // 报警村庄
        result.setWarnList(warnVillages);
        result.setWarnNum(warnVillages.size());
        result.setWarnStats(warnStats);

        List<UnitDataResVo> warnStatsResult = Arrays.asList(buildUnitData(warnStats,
                "waterDisasterPrevention/floodWarning",
                null,
                timeIndex,
                endTime,
                drillCase.getId(),
                reqVo.getUnitName(),
                dataGroupId,
                idService.nextID()
        ));
        JSONObject temp = new JSONObject();
        temp.put("points", warnVillages);
        temp.put("warnNum", warnVillages.size());
        temp.put("type", "山洪");
        List<UnitDataResVo> warnMapPointsResult = Arrays.asList(buildUnitData(temp,
                "waterDisasterPrevention/warnMapPoints",
                null,
                timeIndex,
                endTime,
                drillCase.getId(),
                reqVo.getUnitName(),
                dataGroupId,
                idService.nextID()
        ));

        Map<String, List<UnitDataResVo>> resultMap = new HashMap<>();
        resultMap.put("warnStats", warnStatsResult);
        resultMap.put("warnMapPoints", warnMapPointsResult);
        return resultMap;

    }

    /**
     * 山洪报警信息
     *
     * @param reqVo
     * @return
     */
    public List<MountainTorrentDetailResVo> mountainTorrentWarningInfo(MountainTorrentDrillReqVo reqVo) {
        /**
         * 1. 查询所有的村庄，和村庄预警结果
         * 2. 将测站与peroid 关联，并按照peroid分组
         * 3.
         */
        List<MountainTorrentReqVo> presetList = reqVo.getPresetList();
        Date dataTime = reqVo.getDataTime();
        dataTime = dataTime == null ? new Date() : dataTime;
        Date finalDataTime = dataTime;
        Date startTime = reqVo.getStartTime();
        List<String> presetStcds = Collections.emptyList();
        if (StringUtils.isNotBlank(reqVo.getStcds())) {
            presetStcds = Arrays.asList(reqVo.getStcds().split(com.ygkj.utils.StringUtils.JOIN_DELIMITER));
        }

        presetList = presetList == null ? Collections.emptyList() : presetList;
        Map<BigDecimal, Map<String, StPptnR>> ignoreMap = presetList.stream()
                .filter(o -> StringUtils.isNotBlank(o.getId()) && o.getPeriod() != null && o.getRainfall() != null)
                .peek(o -> o.setPeriod(o.getPeriod().setScale(1, BigDecimal.ROUND_UP)))
                .collect(Collectors.groupingBy(MountainTorrentReqVo::getPeriod,
                        Collectors.mapping(o1 -> {
                            StPptnR val = new StPptnR();
                            // 将村庄id临时保存在测站编码字段里，方便日后取用
                            val.setMgstcd(o1.getId());
                            val.setTm(finalDataTime);
                            val.setDrp(o1.getRainfall().doubleValue());
                            return val;
                        }, Collectors.toMap(StPptnR::getMgstcd, Function.identity()))));

        // 查询所有带测站的村落
        List<MountainTorrentDetailResVo> allVillageList = attPrevTfBaseMapper.selectByCondition(Builder.of(PrevTfQueryVo::new).with(PrevTfQueryVo::setFilterHasStcd, true).build());//村落

        // 查询所有的指标
        List<MountainTorrentIndicatorResVo> allIndicatorList = resultsWarnIndicatorMapper.selectByCondition(null);//指标

        // 将指标的peroid设置精度，并去重
        allIndicatorList = allIndicatorList.stream()
                .peek(o -> o.setPeriod(o.getPeriod().setScale(1, BigDecimal.ROUND_UP)))
                .collect(
                        collectingAndThen(toCollection(() -> new TreeSet<>(Comparator.comparing(o -> o.getTown() + ";" + o.getAvi() + ";" + o.getNvi() + ";" + o.getIndexCategory() + ";" + o.getPeriod()))), ArrayList::new)
                );
        // 将指标按行政村和自然村分组
        Map<String, List<MountainTorrentIndicatorResVo>> allIndicatorMap = allIndicatorList.stream()
                .collect(Collectors.groupingBy(o -> o.getNvi().concat("_").concat(o.getAvi())));
        // 将村落根据peroid 分组 方便后面查询所有降雨
        Map<BigDecimal, List<AttPrevTfBase>> peroidGroupMap = new HashMap<>();
        for (MountainTorrentDetailResVo village : allVillageList) {
            String nvi = village.getNvi();
            String avi = village.getAvi();
            String indicatorKey = nvi.concat("_").concat(avi);

            List<MountainTorrentIndicatorResVo> indicatorList = allIndicatorMap.getOrDefault(indicatorKey, Collections.emptyList());

            if (CollectionUtils.isNotBlank(indicatorList)) {
                for (ResultsWarnIndicator indicator : indicatorList) {
                    BigDecimal peroid = indicator.getPeriod();
                    List<AttPrevTfBase> tempVillageList = peroidGroupMap.get(peroid);
                    if (tempVillageList == null) {
                        tempVillageList = new ArrayList<>(allVillageList.size());
                    }
                    tempVillageList.add(village);
                    peroidGroupMap.put(peroid, tempVillageList);
                }
            }
        }
        Map<BigDecimal, Map<String, StPptnR>> rainfallMap = new HashMap<>(peroidGroupMap.size());

        Date end = dataTime;
        // 根据时间区间，和村庄查询降雨
        List<String> finalPresetStcds = presetStcds;
        peroidGroupMap.forEach((peroid, tempVillageList) -> {
            Map<String, StPptnR> tempIgnoreMap = ignoreMap.get(peroid);
            tempIgnoreMap = tempIgnoreMap == null ? Collections.emptyMap() : tempIgnoreMap;

            List<String> stcds = tempVillageList.stream()
//                    .filter(o -> !tempIgnoreMap.containsKey(o.getId()))
                    .map(AttPrevTfBase::getStationCode)
                    .filter(StringUtils::isNotBlank)
                    .distinct()
                    .collect(Collectors.toList());
            if (CollectionUtils.isNotBlank(finalPresetStcds)) {
                stcds.retainAll(finalPresetStcds);
            }
            // 根据村庄得到测站编码，并根据，编码和时间区间查询降雨值
            if (CollectionUtils.isNotBlank(stcds)) {
                Date start = new Date(end.getTime() - peroid.multiply(new BigDecimal(3600000)).longValue());
                if (startTime != null && start.before(startTime)) {
                    start = startTime;
                }
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                List<String> tables = Arrays.asList(DateUtils.getTableNamesByTimeInterval(StationType.RAINFALL.table(), sdf.format(start), sdf.format(end)));
                List<StPptnR> tempRainfalls = waterAndRainMapper.selectSumRainfall(tables, stcds, start, end);
                Map<String, StPptnR> tempRainfallMap = tempRainfalls.stream().collect(Collectors.toMap(StPptnR::getMgstcd, Function.identity()));
                tempRainfallMap.putAll(tempIgnoreMap);
                // 将降雨值实时值和输入值合并
                rainfallMap.put(peroid, tempRainfallMap);
            }
        });

//        Set<String> existSet = new HashSet<>(allVillageList.size());
        for (MountainTorrentDetailResVo village : allVillageList) {
            String nvi = village.getNvi();
            String avi = village.getAvi();
//            String lngLatKey = village.getLon().toString()
//                    .concat(com.ygkj.utils.StringUtils.JOIN_DELIMITER)
//                    .concat(village.getLat().toString());
//            if (existSet.contains(lngLatKey)) {
//                continue;
//            }
            String indicatorKey = String.valueOf(nvi).concat("_").concat(avi);

            // 获取一个村庄下面指标
            List<MountainTorrentIndicatorResVo> originIndicatorList = allIndicatorMap.getOrDefault(indicatorKey, Collections.emptyList());

            String stcd = village.getStationCode();

            if (CollectionUtils.isNotBlank(originIndicatorList)) {

            } else {
                continue;
            }
            // 将指标列表序列化，实现深度拷贝，避免修改原始值，原始值下次循环可能用到
            List<MountainTorrentIndicatorResVo> targetIndicatorList = JSONArray.parseArray(JSONObject.toJSONString(originIndicatorList, SerializerFeature.WriteMapNullValue)).toJavaList(MountainTorrentIndicatorResVo.class);
            village.setIndicatorList(targetIndicatorList);
            // 填充各个区间的降雨值,超阈值值，以及警戒等级
            for (MountainTorrentIndicatorResVo indicator : targetIndicatorList) {
                BigDecimal period = indicator.getPeriod();
                Map<String, StPptnR> tempRainfallMap = rainfallMap.get(period);
                if (CollectionUtils.isBlank(tempRainfallMap)) {
                    continue;
                }
                // 优先使用输入的降雨数据
                StPptnR rainfall = tempRainfallMap.get(village.getId());
                if (rainfall == null) {
                    rainfall = tempRainfallMap.get(stcd);
                }
                if (rainfall == null) {
                    continue;
                }
                indicator.setTm(rainfall.getTm());
                indicator.setRealTimeRainfall(String.valueOf(rainfall.getDrp()));
                BigDecimal readyThreshold = indicator.getReadyMove();//准备转移
                BigDecimal immediateThreshold = indicator.getImmediateTransfer();//立即转移

                BigDecimal rainValue = BigDecimal.valueOf(rainfall.getDrp());
                if (immediateThreshold.doubleValue() < rainValue.doubleValue()) {
                    double overThresholdVal = rainValue.subtract(readyThreshold).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                    indicator.setOverThresholdVal(overThresholdVal);
                    indicator.setWarnGradeID(5);//5 立即转移 4 准备转移
                    continue;
                }
                if (readyThreshold.doubleValue() < rainValue.doubleValue()) {
                    double overThresholdVal = rainValue.subtract(readyThreshold).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                    indicator.setOverThresholdVal(overThresholdVal);
                    indicator.setWarnGradeID(4);//5 立即转移 4 准备转移
                }
            }
            // 取最大的的等级和降雨阈值
            Optional<MountainTorrentIndicatorResVo> opt = targetIndicatorList.stream().max((o1, o2) -> {
                if (o1.getWarnGradeID() != null && o2.getWarnGradeID() != null) {
                    int compare = o1.getWarnGradeID().compareTo(o2.getWarnGradeID());
                    if (compare == 0) {
                        return Double.compare(o1.getOverThresholdVal(), o2.getOverThresholdVal());
                    } else {
                        return compare;
                    }
                } else if (o1.getWarnGradeID() == null && o2.getWarnGradeID() != null) {
                    return -1;
                } else if (o1.getWarnGradeID() != null && o2.getWarnGradeID() == null) {
                    return 1;
                } else {
                    return Double.compare(o1.getOverThresholdVal(), o2.getOverThresholdVal());
                }
            });
            // 将最大的报警等级，和阈值设置为当前村庄的报警信息
            if (opt.isPresent()) {
                MountainTorrentIndicatorResVo indicator = opt.get();
                if (indicator.getWarnGradeID() != null) {
                    village.setThresholdData(indicator.getOverThresholdVal());
                    String tm = DateUtil.format(indicator.getTm(), "yyyy-MM-dd HH:mm:ss").substring(0, 19);
                    Date nextDay = null;
                    double v = indicator.getPeriod().setScale(1, BigDecimal.ROUND_UP).doubleValue();
                    nextDay = getDate(tm, nextDay, v);
                    village.setThresholdTime(DateUtil.format(nextDay, "yyyy-MM-dd HH:mm:ss") + "至" + tm);
                    village.setWarnGradeID(indicator.getWarnGradeID().toString());//5 立即转移 4 准备转移
                }
            }
        }
        return allVillageList;
    }

    private Date getDate(String tm, Date nextDay, double v) {
        if (v == 0.5) {
            nextDay = MydateUtil.getNextDay(DateUtil.parseDate("yyyy-MM-dd HH:mm:ss", tm), null,
                    null, null, null, -30, null);
        }
        if (v == 1.0) {
            nextDay = MydateUtil.getNextDay(DateUtil.parseDate("yyyy-MM-dd HH:mm:ss", tm), null,
                    null, null, null, -60, null);
        }
        if (v == 1.5) {
            nextDay = MydateUtil.getNextDay(DateUtil.parseDate("yyyy-MM-dd HH:mm:ss", tm), null,
                    null, null, null, -90, null);
        }
        if (v == 2.0) {
            nextDay = MydateUtil.getNextDay(DateUtil.parseDate("yyyy-MM-dd HH:mm:ss", tm), null,
                    null, null, null, -120, null);
        }
        if (v == 2.5) {
            nextDay = MydateUtil.getNextDay(DateUtil.parseDate("yyyy-MM-dd HH:mm:ss", tm), null,
                    null, null, null, -150, null);
        }
        if (v == 3.0) {
            nextDay = MydateUtil.getNextDay(DateUtil.parseDate("yyyy-MM-dd HH:mm:ss", tm), null,
                    null, null, null, -180, null);
        }
        if (v == 3.5) {
            nextDay = MydateUtil.getNextDay(DateUtil.parseDate("yyyy-MM-dd HH:mm:ss", tm), null,
                    null, null, null, -210, null);
        }
        if (v == 4.0) {
            nextDay = MydateUtil.getNextDay(DateUtil.parseDate("yyyy-MM-dd HH:mm:ss", tm), null,
                    null, null, null, -240, null);
        }
        if (v == 4.5) {
            nextDay = MydateUtil.getNextDay(DateUtil.parseDate("yyyy-MM-dd HH:mm:ss", tm), null,
                    null, null, null, -270, null);
        }
        if (v == 6.0) {
            nextDay = MydateUtil.getNextDay(DateUtil.parseDate("yyyy-MM-dd HH:mm:ss", tm), null,
                    null, null, null, -360, null);
        }
        if (v == 9.0) {
            nextDay = MydateUtil.getNextDay(DateUtil.parseDate("yyyy-MM-dd HH:mm:ss", tm), null,
                    null, null, null, -540, null);
        }
        return nextDay;
    }

    private String getUnitKey(MountainTorrentDetailResVo village) {
        String lngLatKey = village
                .getNvi()
                .concat(com.ygkj.utils.StringUtils.JOIN_DELIMITER)
                .concat(village.getAvi())
                .concat(com.ygkj.utils.StringUtils.JOIN_DELIMITER)
                .concat(village.getLon().toString())
                .concat(com.ygkj.utils.StringUtils.JOIN_DELIMITER)
                .concat(village.getLat().toString());
        return lngLatKey;
    }
}
