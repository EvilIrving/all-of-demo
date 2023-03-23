package com.ygkj.big.screen.unit;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.github.heqiao2010.lunar.LunarCalendar;
import com.ygkj.big.screen.feign.GraphClient;
import com.ygkj.big.screen.mapper.AttAdcdBaseMapper;
import com.ygkj.big.screen.mapper.AttStBaseMapper;
import com.ygkj.big.screen.mapper.WaterAndRainMapper;
import com.ygkj.big.screen.model.AttAdcdBase;
import com.ygkj.big.screen.model.AttStBase;
import com.ygkj.big.screen.model.StAstrotdDay;
import com.ygkj.big.screen.vo.request.*;
import com.ygkj.big.screen.vo.response.MultiWaterRainResVo;
import com.ygkj.big.screen.vo.response.StPrrwtResVo;
import com.ygkj.enums.StationType;
import com.ygkj.utils.Builder;
import com.ygkj.utils.CollectionUtils;
import com.ygkj.utils.DateUtils;
import com.ygkj.utils.MapComparator;
import com.ygkj.water.model.CommonCode;
import com.ygkj.water.model.CommonResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author xq
 * @Description
 * @Date 2021/7/13
 */
@Component
@Slf4j
public class TideManager extends AbstractUnitManager {

    @Autowired
    private GraphClient graphClient;

    @Resource
    private AttStBaseMapper stBaseMapper;

    @Resource
    private AttAdcdBaseMapper adcdBaseMapper;

    @Resource
    private WaterAndRainMapper waterAndRainMapper;

    @Value("#{${dict.astrotd}}")
    private Map<String, String> astrotdMap;

    private ConcurrentHashMap<String, Object> cache;

    public TideManager(ApplicationContext context) {
        super(context);
        cache = new ConcurrentHashMap<>();
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDestroy() {
        cache.clear();
    }

    @Override
    public void doSubscribe() {

    }

    @Override
    public void doPublish() {

    }

    @Override
    public Object getData(UnitDataReqVo reqVo) {
        switch (reqVo.getStrategy()) {
            case "tideCurve": {
                String tideStaName = reqVo.getParam("tideStaName", String.class);
                Date dataTime = reqVo.getParam("dataTime", Date.class);
                Object result = null;
                if ((result = cache.get(buildDefaultCacheKey("tideCurve", tideStaName, dataTime))) == null) {
                    result = tideCurve(tideStaName, dataTime);
                }
                return result;
            }
            case "tideReplayCurve": {
                String tideStaName = reqVo.getParam("tideStaName", String.class);
                Date start = reqVo.getParam("start", Date.class);
                Date end = reqVo.getParam("end", Date.class);
                return tideReplayCurve(tideStaName, start, end);
            }
            case "tideList": {
                TideListQueryReqVo reqVoParam = reqVo.getParam("reqVo", TideListQueryReqVo.class);
                return tideList(reqVoParam);
            }
            case "astroTideTable": {
                String tideStaName = reqVo.getParam("tideStaName", String.class);
                Date dataTime = reqVo.getParam("dataTime", Date.class);
                Object result = null;
                if ((result = cache.get(buildDefaultCacheKey("astroTideTable", tideStaName, dataTime))) == null) {
                    result = astroTideTable(tideStaName, dataTime);
                }
                return result;
            }
            case "mainTideRealtimeLevel": {
                Object result = null;
                if ((result = cache.get(buildDefaultCacheKey("mainTideRealtimeLevel"))) == null) {
                    result = mainTideRealtimeLevel();
                }
                return result;
            }
            case "tideWarnStatistic": {
                Object result = null;
                if ((result = cache.get(buildDefaultCacheKey("tideWarnStatistic"))) == null) {
                    result = tideWarnStatistic();
                }
                return result;
            }
            default:
                break;
        }
        return null;
    }

    @Override
    public void buildData(UnitDataBuilderReqVo reqVo) {
        log.info("提交了一项潮位缓存任务");
        String tideStaName = "温州";
        Date dataTime = null;
        cache.put(buildDefaultCacheKey("tideCurve", tideStaName, dataTime), tideCurve(tideStaName, dataTime));
        cache.put(buildDefaultCacheKey("astroTideTable", tideStaName, dataTime), astroTideTable(tideStaName, dataTime));
        cache.put(buildDefaultCacheKey("tideWarnStatistic"), tideWarnStatistic());
        cache.put(buildDefaultCacheKey("mainTideRealtimeLevel"), mainTideRealtimeLevel());
    }

    @Override
    public ExecutorService getExecutor() {
        return getDefaultConfigExecutor();
    }

    /**
     * 每5分钟提交一次任务
     */
    @Scheduled(cron = "0 1/5 * * * ? ")
    public void updateCacheTask() {
        doBuild(null);
    }

    public JSONObject tideCurve(String tideStaName, Date dataTime) {
        LocalDate dateStamp = null;
        if (dataTime == null) {
            dateStamp = LocalDate.now();
        } else {
            dateStamp = DateUtils.date2LocalDate(dataTime);
        }
        Date start = DateUtils.localDate2Date(dateStamp.minusDays(1));
        // 并不包含end那天的
        Date end = DateUtils.localDate2Date(dateStamp.plusDays(2));

        return tideReplayCurve(tideStaName, start, end);
    }


    public JSONObject tideReplayCurve(String tideStaName, Date start, Date end) {
        JSONObject result = new JSONObject();
        result.put("astroTide", Collections.emptyList());
        result.put("realTide", Collections.emptyList());
        result.put("subTide", Collections.emptyList());
        result.put("warnTide", 0D);

        if ("龙湾".equals(tideStaName)) {
            tideStaName = "灵昆";
        }
        String stcd = astrotdMap.get(tideStaName);
        if (StringUtils.isBlank(stcd)) {
            return null;
        }
        CommonResult<List<LinkedHashMap>> realTideResp = this.graphClient.multiWaterRains(Builder.of(MultiWaterRainReqVo::new)
                .with(MultiWaterRainReqVo::setStcds, stcd)
                .with(MultiWaterRainReqVo::setStart, start)
                .with(MultiWaterRainReqVo::setEnd, end)
                .build());
        CommonResult<List<LinkedHashMap>> astroTideResp = this.graphClient.multiWaterRains(Builder.of(MultiWaterRainReqVo::new)
                .with(MultiWaterRainReqVo::setStcds, stcd)
                .with(MultiWaterRainReqVo::setStart, start)
                .with(MultiWaterRainReqVo::setEnd, end)
                .with(MultiWaterRainReqVo::setAstrotd, true)
                .build());
        List<StPrrwtResVo> realTempList = null;
        if (realTideResp.getCode() == CommonCode.SUCCESS.code() && CollectionUtils.isNotBlank(realTideResp.getData())) {
            List<MultiWaterRainResVo> data = JSONArray.parseArray(JSONArray.toJSONString(realTideResp.getData()), MultiWaterRainResVo.class);
            MultiWaterRainResVo tide = data.get(0);
            result.put("warnTide", tide.getWarningLevel() == null ? 0D : tide.getWarningLevel());
            realTempList = tide.getWaterRains();
        }
        realTempList = realTempList == null ? Collections.emptyList() : realTempList;
        Map<Date, StPrrwtResVo> realMap = realTempList.stream().collect(Collectors.groupingBy(StPrrwtResVo::getTm)).entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, o -> o.getValue().get(0)));

        List<StPrrwtResVo> astroTempList = null;
        if (astroTideResp.getCode() == CommonCode.SUCCESS.code() && CollectionUtils.isNotBlank(astroTideResp.getData())) {
            List<MultiWaterRainResVo> data = JSONArray.parseArray(JSONArray.toJSONString(astroTideResp.getData()), MultiWaterRainResVo.class);
            astroTempList = data.get(0).getWaterRains();
        }
        astroTempList = astroTempList == null ? Collections.emptyList() : astroTempList;

        // 将天文潮变成每5分钟一个点
        List<StPrrwtResVo> finalAstroList = new ArrayList<>(astroTempList.size() * 12);
        int astroLength = astroTempList.size();
        int nextIndex = 0;
        for (int i = 0; i < astroLength; i++) {
            StPrrwtResVo current = astroTempList.get(i);
            nextIndex = i + 1;
            if (nextIndex < astroLength) {
                StPrrwtResVo next = astroTempList.get(nextIndex);
                int j = 0;
                double avg = (next.getTdz() - current.getTdz()) / 12;
                while (j < 12) {
                    if (j == 0) {
                        finalAstroList.add(current);
                    } else {
                        StPrrwtResVo temp = new StPrrwtResVo();
                        BeanUtils.copyProperties(current, temp);
                        temp.setTm(new Date(current.getTm().getTime() + (long) j * 5 * 60 * 1000));
                        temp.setTdz(Double.parseDouble(String.format("%.2f", temp.getTdz() + j * avg)));
                        finalAstroList.add(temp);
                    }
                    j++;
                }
            } else {
                finalAstroList.add(current);
            }
        }

//        // 将实时潮位和天文潮位按时间一个个匹配
//        List<StPrrwtResVo> finalRealList = null;
//        List<StPrrwtResVo> subTideList = Collections.emptyList();
//        if (CollectionUtils.isNotBlank(finalAstroList)) {
//            finalRealList = new ArrayList<>(realTempList.size());
//            subTideList = new ArrayList<>(realTempList.size());
//            for (StPrrwtResVo astroTemp : finalAstroList) {
//                StPrrwtResVo realTemp = realMap.get(astroTemp.getTm());
//                if (realTemp != null) {
//                    finalRealList.add(realTemp);
//                    subTideList.add(Builder.of(StPrrwtResVo::new).with(StPrrwtResVo::setTm, astroTemp.getTm()).with(StPrrwtResVo::setTdz, Double.parseDouble(String.format("%.2f", realTemp.getTdz() - astroTemp.getTdz()))).build());
//                } else {
//                    realTemp = Builder.of(StPrrwtResVo::new).with(StPrrwtResVo::setTm, astroTemp.getTm()).build();
//                    subTideList.add(realTemp);
//                }
//            }
//        } else {
//            finalRealList = realTempList;
//        }
//        result.put("astroTide", finalAstroList);
//        result.put("realTide", finalRealList);
//        result.put("subTide", subTideList);
        Map<Date, StPrrwtResVo> astroMap = finalAstroList.stream().peek(o -> o.setTm(DateUtils.clearDateTimePrecision(o.getTm(), Calendar.SECOND, Calendar.MILLISECOND))).collect(Collectors.toMap(StPrrwtResVo::getTm, Function.identity()));

        LocalDateTime tempStartLd = DateUtils.date2LocalDateTime(start);
        int startMinutesTemp = tempStartLd.getMinute();
        LocalDateTime tempEndLd = DateUtils.date2LocalDateTime(end);
        int endMinutesTemp = tempEndLd.getMinute();
        int startLeft = startMinutesTemp % 5;
        int startMinutes = 0;
        if (startLeft == 0) {
            startMinutes = startMinutesTemp;
        } else {
            startMinutes = (startMinutesTemp / 5 + 1) * 5;
        }
        int endLeft = endMinutesTemp % 5;
        int endMinutes = 0;
        if (endLeft == 0) {
            endMinutes = endMinutesTemp;
        } else {
            endMinutes = (endMinutesTemp / 5 - 1) * 5;
        }
        Date tempStart = DateUtils.localDateTime2Date(tempStartLd.withNano(0).withMinute(startMinutes).withSecond(0));
        Date tempEnd = DateUtils.localDateTime2Date(tempEndLd.withNano(0).withMinute(endMinutes).withSecond(0));
        int size = (int) ((tempEnd.getTime() - tempStart.getTime()) / (5 * 60 * 1000));

        // 将实时潮位和天文潮位按时间一个个匹配
        List<StPrrwtResVo> resultRealList = new ArrayList<>(size);
        List<StPrrwtResVo> resultAstroList = new ArrayList<>(size);
        List<StPrrwtResVo> subTideList = new ArrayList<>(size);
        Date timeIndex = tempStart;
        while (!timeIndex.after(tempEnd)) {
            StPrrwtResVo realTemp = realMap.get(timeIndex);
            StPrrwtResVo astroTemp = astroMap.get(timeIndex);
            if (realTemp != null && astroTemp != null) {
                subTideList.add(Builder.of(StPrrwtResVo::new).with(StPrrwtResVo::setTm, tempEnd).with(StPrrwtResVo::setTdz, Double.parseDouble(String.format("%.2f", realTemp.getTdz() - astroTemp.getTdz()))).build());
            } else {
                if (realTemp == null) {
                    realTemp = Builder.of(StPrrwtResVo::new).with(StPrrwtResVo::setTm, tempEnd).build();
                }
                if (astroTemp == null) {
                    astroTemp = Builder.of(StPrrwtResVo::new).with(StPrrwtResVo::setTm, tempEnd).build();
                }
                subTideList.add(Builder.of(StPrrwtResVo::new).with(StPrrwtResVo::setTm, astroTemp.getTm()).build());
            }
            resultRealList.add(realTemp);
            resultAstroList.add(astroTemp);
            timeIndex = new Date(timeIndex.getTime() + 5 * 60 * 1000);
        }
        result.put("astroTide", resultAstroList);
        result.put("realTide", resultRealList);
        result.put("subTide", subTideList);
        return result;
    }

    public JSONObject tideList(TideListQueryReqVo reqVo) {
        JSONObject result = new JSONObject();
        int overWarnNum = 0;
        int totalNum = 0;
        List<JSONObject> list = Collections.emptyList();
        result.put("overWarnNum", overWarnNum);
        result.put("totalNum", totalNum);
        result.put("list", list);
        Date dataTime = reqVo.getDataTime();
        if (dataTime == null) {
            dataTime = new Date();
        }
        List<AttStBase> stations = this.stBaseMapper.selectByCondition(Builder
                .of(HashMap<String, Object>::new)
                .with(HashMap::put, "areaCode", reqVo.getAdcd())
                .with(HashMap::put, "bas", reqVo.getBas())
                .with(HashMap::put, "stName", reqVo.getStName())
                .build());
        if (CollectionUtils.isBlank(stations)) {
            return result;
        }

        CommonResult<List<AttStBase>> realResp = this.graphClient.waterStationsBody(Builder.of(SynWaterRequestVo::new)
                .with(SynWaterRequestVo::setStCodes, stations.stream().map(AttStBase::getStCode).filter(StringUtils::isNotBlank).collect(Collectors.joining(",")))
                .with(SynWaterRequestVo::setDataTime, dataTime)
                .with(SynWaterRequestVo::setStationType, StationType.TIDE.type())
                .build());
        List<AttStBase> realData = Collections.emptyList();
        if (realResp.getCode() == CommonCode.SUCCESS.code() && CollectionUtils.isNotBlank(realResp.getData())) {
            realData = realResp.getData();
        }
        if (CollectionUtils.isBlank(realData)) {
            return result;
        }
        list = new ArrayList<>(stations.size());

        Set<String> astrotdStcds = new HashSet<>(astrotdMap.values());
        CommonResult<List<AttStBase>> astrotdResp = this.graphClient.waterStationsBody(Builder.of(SynWaterRequestVo::new)
                .with(SynWaterRequestVo::setStCodes, String.join(com.ygkj.utils.StringUtils.JOIN_DELIMITER, astrotdStcds))
                .with(SynWaterRequestVo::setDataTime, dataTime)
                .with(SynWaterRequestVo::setStationType, StationType.TIDE.type())
                .with(SynWaterRequestVo::setStationType, StationType.TIDE.type())
                .build());
        List<AttStBase> astrotdData = null;
        if (astrotdResp.getCode() == CommonCode.SUCCESS.code() && CollectionUtils.isNotBlank(astrotdResp.getData())) {
            astrotdData = astrotdResp.getData();
        }
        astrotdData = astrotdData == null ? Collections.emptyList() : astrotdData;
        Map<String, AttStBase> astrotdMap = astrotdData.stream().collect(Collectors.toMap(AttStBase::getStCode, Function.identity()));
        Map<String, String> adcdMap = selectAdnmByAdcd(stations.stream().filter(o -> StringUtils.isNotBlank(o.getAreaCode())).map(AttStBase::getAreaCode).collect(Collectors.toList()));
        totalNum = realData.size();
//        List<JSONObject> tempList = new ArrayList<>(4);
        for (AttStBase temp : realData) {
            if (StringUtils.isNotBlank(temp.getAreaCode())) {
                temp.setAreaName(adcdMap.get(temp.getAreaCode()));
            }
            JSONObject e = JSONObject.parseObject(JSONObject.toJSONString(temp, SerializerFeature.WriteMapNullValue));
            String overWarn = null;
            if (temp.getWarningLevel() != null && temp.getTdz() > temp.getWarningLevel()) {
                overWarn = String.format("%.2f", temp.getTdz() - temp.getWarningLevel());
                overWarnNum++;
            }
            e.put("overWarn", overWarn);
            String subTide = null;
            String astroTide = null;
            if (astrotdStcds.contains(temp.getStCode())) {
                AttStBase astrotd = astrotdMap.get(temp.getStCode());
                if (astrotd != null && astrotd.getTdz() != Double.MIN_VALUE) {
                    astroTide = String.format("%.2f", astrotd.getTdz());
                    subTide = String.format("%.2f", temp.getTdz() - astrotd.getTdz());
                }
            }
            e.put("astroTide", astroTide);
            e.put("subTide", subTide);
            // 大屏上的4个站点需要放前面
            switch (temp.getStCode()) {
                case "SWZ_06896":
                    list.add(0, e);
                    break;
                case "SWZ_10303":
                    list.add(0, e);
                    break;
                case "SWZ_01252":
                    list.add(0, e);
                    break;
                case "SWZ_01664":
                    list.add(0, e);
                    break;
                default:
                    list.add(e);
            }
        }
        if (StringUtils.isNotBlank(reqVo.getOrderColumn())) {
            Comparator<JSONObject> comparator = new MapComparator<>(reqVo.getOrderColumn());
            if (reqVo.getAscOrDesc() != null && reqVo.getAscOrDesc()) {
                comparator = comparator.reversed();
            }
            list = list.stream().sorted(comparator).collect(Collectors.toList());
        }
        result.put("overWarnNum", overWarnNum);
        result.put("totalNum", totalNum);
        result.put("list", list);
        return result;
    }

    public List<JSONObject> astroTideTable(String tideStaName, Date dataTime) {
        if ("龙湾".equals(tideStaName)) {
            tideStaName = "灵昆";
        }
        String stcd = astrotdMap.get(tideStaName);
        if (StringUtils.isBlank(stcd)) {
            return Collections.emptyList();
        }

        LocalDate dateStamp = null;
        if (dataTime == null) {
            dateStamp = LocalDate.now();
        } else {
            dateStamp = DateUtils.date2LocalDate(dataTime);
        }
        Date start = DateUtils.localDate2Date(dateStamp);
        // 并不包含end那天的
        Date end = DateUtils.localDate2Date(dateStamp.plusDays(3));

        List<StAstrotdDay> tempList = this.waterAndRainMapper.selectAstrotdDayByDate(tideStaName, start, end);
        tempList = tempList == null ? Collections.emptyList() : tempList;
        tempList.stream().sorted(Comparator.comparing(StAstrotdDay::getDate)).limit(3).collect(Collectors.toList());
        List<JSONObject> result = new ArrayList<>();
        for (StAstrotdDay temp : tempList) {
            JSONObject e = new JSONObject();
            e.putIfAbsent("amMax", Builder.of(StPrrwtResVo::new).with(StPrrwtResVo::setTm, temp.getAmHighTime() == null ? null : new Date(temp.getAmHighTime().getTime() + temp.getDate().getTime() + 8 * 3600 * 1000)).with(StPrrwtResVo::setTdz, StringUtils.isNotBlank(temp.getAmHighValue()) ? Double.valueOf(temp.getAmHighValue()) : null).build());
            e.putIfAbsent("amMin", Builder.of(StPrrwtResVo::new).with(StPrrwtResVo::setTm, temp.getAmLowTime() == null ? null : new Date(temp.getAmLowTime().getTime() + temp.getDate().getTime() + 8 * 3600 * 1000)).with(StPrrwtResVo::setTdz, StringUtils.isNotBlank(temp.getAmLowValue()) ? Double.valueOf(temp.getAmLowValue()) : null).build());
            e.putIfAbsent("pmMax", Builder.of(StPrrwtResVo::new).with(StPrrwtResVo::setTm, temp.getPmHighTime() == null ? null : new Date(temp.getPmHighTime().getTime() + temp.getDate().getTime() + 8 * 3600 * 1000)).with(StPrrwtResVo::setTdz, StringUtils.isNotBlank(temp.getPmHighValue()) ? Double.valueOf(temp.getPmHighValue()) : null).build());
            e.putIfAbsent("pmMin", Builder.of(StPrrwtResVo::new).with(StPrrwtResVo::setTm, temp.getPmLowTime() == null ? null : new Date(temp.getPmLowTime().getTime() + temp.getDate().getTime() + 8 * 3600 * 1000)).with(StPrrwtResVo::setTdz, StringUtils.isNotBlank(temp.getPmLowValue()) ? Double.valueOf(temp.getPmLowValue()) : null).build());
            e.put("date", temp.getDate());
            Calendar theCalendar = Calendar.getInstance();
            theCalendar.setTime(temp.getDate());
            LunarCalendar lunar = LunarCalendar.solar2Lunar(theCalendar);
            e.put("lunar", LunarCalendar.getMonthName(lunar.getLunarMonth()) + "月" + LunarCalendar.getDayName(lunar.getDayOfLunarMonth()));
            result.add(e);
        }
        return result.stream().sorted(Comparator.comparing(o -> o.getDate("date"))).limit(3).collect(Collectors.toList());
    }

    /**
     * 潮位报警统计
     *
     * @return
     */
    public JSONObject tideWarnStatistic() {
        JSONObject result = new JSONObject();
        List<AttStBase> overWarnList = Collections.emptyList();

        CommonResult<List<AttStBase>> resp = graphClient.waterStationsBody(Builder.of(SynWaterRequestVo::new)
                .with(SynWaterRequestVo::setStationType, StationType.TIDE.type())
                .with(SynWaterRequestVo::setStCodes, String.join(",", this.astrotdMap.get("鳌江"), astrotdMap.get("瑞安"), astrotdMap.get("灵昆"), astrotdMap.get("温州")))
                .build());
        if (resp != null && resp.getCode() == CommonCode.SUCCESS.code() && CollectionUtils.isNotBlank(resp.getData())) {
            List<AttStBase> list = resp.getData();
            overWarnList = list.stream().filter(o -> {
                try {
                    return o.getWaterLevel() > o.getWarningLevel();
                } catch (Exception e) {
                    return false;
                }
            }).peek(o -> o.setStName(astrotdMap.get(o.getStCode()))).collect(Collectors.toList());
        }
        overWarnList = overWarnList == null ? Collections.emptyList() : overWarnList;
        result.put("points", overWarnList);
        result.put("warnNum", overWarnList.size());
        return result;
    }

    /**
     * 根据行政区划编码查询行政区划名称并组成以编码为key的map
     *
     * @param adcdList
     * @return
     */
    private Map<String, String> selectAdnmByAdcd(List<String> adcdList) {
        if (org.springframework.util.CollectionUtils.isEmpty(adcdList)) {
            return Collections.emptyMap();
        }
        List<AttAdcdBase> records = this.adcdBaseMapper.selectAdnmByAdcdList(String.join(com.ygkj.utils.StringUtils.JOIN_DELIMITER, adcdList));
        if (org.springframework.util.CollectionUtils.isEmpty(records)) {
            return Collections.emptyMap();
        }
        return records.stream().collect(Collectors.toMap(AttAdcdBase::getAdcd, AttAdcdBase::getAdnm));
    }

    public Object mainTideRealtimeLevel() {
        return graphClient.waterStationsBody(Builder.of(SynWaterRequestVo::new)
                .with(SynWaterRequestVo::setStationType, StationType.TIDE.type())
                .with(SynWaterRequestVo::setStCodes, String.join(",", this.astrotdMap.get("鳌江"), astrotdMap.get("瑞安"), astrotdMap.get("灵昆"), astrotdMap.get("温州")))
                .build()).getData();
    }

}
