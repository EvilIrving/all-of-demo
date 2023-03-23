package com.ygkj.big.screen.unit;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.ygkj.big.screen.feign.WarnServiceClient;
import com.ygkj.big.screen.mapper.WaterAndRainMapper;
import com.ygkj.big.screen.mapper.AttPrevTfBaseMapper;
import com.ygkj.big.screen.mapper.MountainFloodMapper;
import com.ygkj.big.screen.mapper.ResultsWarnIndicatorMapper;
import com.ygkj.big.screen.model.*;
import com.ygkj.big.screen.vo.request.*;
import com.ygkj.big.screen.vo.response.*;
import com.ygkj.enums.StationType;
import com.ygkj.utils.Builder;
import com.ygkj.utils.CollectionUtils;
import com.ygkj.utils.DateUtils;
import com.ygkj.utils.HttpClientUtil;
import com.ygkj.warning.factory.SysSendMessageLogCreator;
import com.ygkj.warning.model.SysSendMessageLog;
import com.ygkj.water.model.CommonCode;
import com.ygkj.water.model.CommonResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ygkj.com.util.DateUtil;
import ygkj.com.util.MydateUtil;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toCollection;

/**
 * @author xq
 * @Description 山洪组件
 * @Date 2021/7/20
 */
@Component
@Slf4j
public class MountainTorrentManager extends AbstractUnitManager {

    @Resource
    private AttPrevTfBaseMapper attPrevTfBaseMapper;

    @Resource
    private ResultsWarnIndicatorMapper resultsWarnIndicatorMapper;

    @Resource
    private WaterAndRainMapper waterAndRainMapper;

    @Autowired
    WarnServiceClient warnServiceClient;

    // 立即转移
    final static String warnGradeID5Str = "5";

    // 立即转移
    final static Integer warnGradeID5Int = 5;

    // 准备转移
    final static String warnGradeID4Str = "4";

    // 准备转移
    final static Integer warnGradeID4Int = 4;

    @Resource
    private MountainFloodMapper mountainFloodMapper;

    private ConcurrentHashMap<String, Object> cache;

    public MountainTorrentManager(@Autowired ApplicationContext context) {
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
            case "floodWarning4Predict": {
                Object result = null;
                if ((result = cache.get(buildDefaultCacheKey("floodWarning4Predict"))) == null) {
                    result = floodWarning4Predict();
                }
                return result;
            }
            case "mountainTorrentWarningInfo": {
                Object result = null;
                MountainTorrentDrillReqVo reqParam = reqVo.getParam("reqParam", MountainTorrentDrillReqVo.class);
                if ((result = cache.get(buildDefaultCacheKey("mountainTorrentWarningInfo", reqParam))) == null) {
                    result = mountainTorrentWarningInfo(reqParam);
                }
                return result;
            }
            case "floodWarning4Monitor": {
                Object result = null;
                Date endTime = reqVo.getParam("endTime", Date.class);
                if ((result = cache.get(buildDefaultCacheKey("floodWarning4Monitor", endTime))) == null) {
                    result = floodWarning4Monitor(endTime);
                }
                return result;
            }
            case "mountainTorrentCWindow": {
                Object result = null;
                Date dataTime = reqVo.getParam("dataTime", Date.class);
                if ((result = cache.get(buildDefaultCacheKey("mountainTorrentCWindow", dataTime))) == null) {
                    result = mountainTorrentCWindow(dataTime);
                }
                return result;
            }
            case "mountainTorrentCWindowTable": {
                Date dataTime = reqVo.getParam("dataTime", Date.class);
                Integer warnType = reqVo.getParam("warnType", Integer.class);
                return mountainTorrentCWindowTable(dataTime, warnType);
            }
            case "mountainTorrentWarnMapPoint": {
                Date dataTime = reqVo.getParam("dataTime", Date.class);
                return mountainTorrentWarnMapPoint(dataTime);
            }
            case "mountainTorrentBaseStats": {
                return mountainTorrentBaseStats();
            }
            default:
                return null;
        }
    }

    @Override
    public void buildData(UnitDataBuilderReqVo reqVo) {
        log.info("提交了一项山洪缓存任务");
        // 缓存预报并持久化
        cache.put(buildDefaultCacheKey("floodWarning4Predict"), floodWarning4Predict());

        // 缓存监测并持久化
        MountainTorrentDrillReqVo reqParam = new MountainTorrentDrillReqVo();
        List<MountainTorrentDetailResVo> mountainTorrentWarningInfo = mountainTorrentWarningInfo(reqParam);
        cache.put(buildDefaultCacheKey("mountainTorrentWarningInfo", reqParam), mountainTorrentWarningInfo);
        Runnable persistRun = () -> persistWarnInfo(mountainTorrentWarningInfo);
        getExecutor().submit(persistRun);

        Date endTime = null;
        // 实时统计一定要在山洪报警点数据缓存后面，
        cache.put(buildDefaultCacheKey("floodWarning4Monitor", endTime), floodWarning4Monitor(endTime));


        // C位统计
        cache.put(buildDefaultCacheKey("mountainTorrentCWindow", endTime), mountainTorrentCWindow(endTime));
    }

    @Override
    public ExecutorService getExecutor() {
        return getDefaultConfigExecutor();
    }

    /**
     * 每5分钟提交一次任务
     */
//    @Scheduled(cron = "0 1/5 * * * ? ")
    public void updateCacheTask() {
        doBuild(null);
    }


    /**
     * 大禹洪水预报统计
     *
     * @return
     */
    public JSONObject floodWarning4Predict() {
        try {
            String url = "http://122.224.94.107:8034/FloodWarning/WarnCount";
            Map<String, Object> params = new HashMap<>();
            params.put("autoCalc", "false");
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = new Date();
            params.put("searchTm", format.format(date));
            params.put("searchTm2", format.format(date));
            params.put("regionCode", 3303);
            params.put("adcdCode", 3303);
            params.put("format", "json");
            Map<String, String> headers = new HashMap<>();
            headers.put("Accept", "application/json");
            String s = HttpClientUtil.sendGet(url, headers, params, "UTF-8");
            JSONObject object = JSONObject.parseObject(s);
            JSONArray town = object.getJSONArray("town");
            JSONObject result = new JSONObject();
            Map<String, List<Object>> map = town.stream().collect(Collectors.groupingBy(json -> ((JSONObject) json).getString("countyName")));
            map.forEach((areaName, list) -> {
                Set<String> set4 = new HashSet<>();
                Set<String> set5 = new HashSet<>();
                list.stream().forEach(o -> {
                    JSONArray villages = ((JSONObject) o).getJSONArray("villages");
                    long count4 = villages.stream().map(village -> ((JSONObject) village).getIntValue("warnGradeID")).filter(integer -> integer == 4).count();
                    result.put("grade4villages", result.getIntValue("grade4villages") + count4);
                    long count5 = villages.stream().map(village -> ((JSONObject) village).getIntValue("warnGradeID")).filter(integer -> integer == 5).count();
                    result.put("grade5villages", result.getIntValue("grade5villages") + count5);
                    if (count4 > 0) {
                        result.put("grade4street", result.getIntValue("grade4street") + 1);
                        if (!set4.contains(areaName)) {
                            set4.add(areaName);
                            result.put("grade4County", result.getIntValue("grade4County") + 1);
                        }
                        result.put("grade4Person", result.getIntValue("grade4Person") + ((JSONObject) o).getIntValue("effectPeople"));
                    }
                    if (count5 > 0) {
                        result.put("grade5street", result.getIntValue("grade5street") + 1);
                        if (!set5.contains(areaName)) {
                            set5.add(areaName);
                            result.put("grade5County", result.getIntValue("grade5County") + 1);
                        }
                        result.put("grade4Person", result.getIntValue("grade4Person") + ((JSONObject) o).getIntValue("effectPeople"));
                    }

                });
            });
            result.put("data", object);
            result.put("involveCounty", map.keySet().size());
            if ((result.getIntValue("grade4villages") + result.getIntValue("grade5villages")) > 0) {
                attPrevTfBaseMapper.insertMountainTorrentPredict(DateUtils.clearDateTimePrecision(new Date(), Calendar.MILLISECOND, Calendar.SECOND), result.toJSONString());
            }
            return result;

        } catch (Exception exception) {
            log.error("山洪预警请求报错:{}", exception);
//            exception.printStackTrace();
            return new JSONObject();
        }
    }


    /**
     * 山洪实时监控
     *
     * @param endTime
     * @return
     */
    public JSONObject floodWarning4Monitor(Date endTime) {
        List<MountainTorrentDetailResVo> allVillages = (List<MountainTorrentDetailResVo>) getData(Builder.of(UnitDataReqVo::new)
                .with(UnitDataReqVo::setStrategy, "mountainTorrentWarningInfo")
                .with(UnitDataReqVo::putParam, "reqParam", Builder.of(MountainTorrentDrillReqVo::new).with(MountainTorrentDrillReqVo::setDataTime, endTime).build())
                .build());
        Map<String, MountainTorrentDetailResVo> allMap = allVillages.stream().collect(Collectors.toMap(this::getUnitKey, Function.identity()));

        List<MountainTorrentDetailResVo> grad5Villages = new ArrayList<>();
        List<MountainTorrentDetailResVo> grad4Villages = new ArrayList<>();
        allMap.forEach((lngLatKey, village) -> {
            if ("5".equals(village.getWarnGradeID())) {
                grad5Villages.add(village);
            } else if ("4".equals(village.getWarnGradeID())) {
                grad4Villages.add(village);
            }
        });
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
        return JSONObject.parseObject(JSONObject.toJSONString(warnStats, SerializerFeature.WriteMapNullValue, SerializerFeature.WriteDateUseDateFormat));
    }


//    /**
//     * 胡聪写的山洪报警统计，我只是搬运工
//     * @author hucong
//     * @param endTime
//     * @return
//     */
//    public JSONObject floodWarning4Monitor(String endTime) {
//
//        JSONObject result = new JSONObject();
//
//        Integer grade4County = 0;   //准备转移区县
//        Integer grade4villages = 0; //准备转移村落
//        Integer grade4street = 0;   //准备转移乡镇
//        Integer grade4Person = 0;   //准备转移人口
//
//        Integer grade5County = 0;    //立即转移区县
//        Integer grade5villages = 0;  //立即转移村落
//        Integer grade5street = 0;    //立即转移乡镇
//        Integer grade5Person = 0;    //立即转移人口
//
//        List<AttPrevTfBase> attPrevTfBases = attPrevTfBaseMapper.selectList(null);//村落
//        List<ResultsWarnIndicator> resultsWarnIndicatorList = resultsWarnIndicatorService.selectList(null);//指标
//        List<AttPrevTfBase> attPrevTfBasesCp = new ArrayList<>();
//
//
//        String tableName = StationType.RAINFALL.table() + endTime.substring(0, 7).replaceAll("-", "");
//        //查找每个测站的离endTime最近的水位数据
//        List<StPptnR> stPptnRS = attPrevTfBaseMapper.recentWaterLevelData(tableName,
//                attPrevTfBases.stream().map(e -> e.getStationCode()).collect(Collectors.toList()), endTime);
//
//        for (AttPrevTfBase attPrevTfBase : attPrevTfBases) {
//            // 村落名称
//            String nvi = attPrevTfBase.getNvi();
//            // 村落人口
//            Integer tpo = attPrevTfBase.getTpo();//村子人数
//            // 自然村
//            String avi = attPrevTfBase.getAvi();
//            String stationCode = attPrevTfBase.getStationCode();
//
//            // 筛选除当前防治对象的预警成果
//            List<ResultsWarnIndicator> collect = resultsWarnIndicatorList.stream()
//                    .filter(e -> nvi.equals(e.getNvi()) && avi.equals(e.getAvi())).collect(Collectors.toList());
//
//            // 遍历预警成果
//            for (ResultsWarnIndicator resultsWarnIndicator : collect) {
//                BigDecimal readyMove = resultsWarnIndicator.getReadyMove();//准备转移
//                BigDecimal immediateTransfer = resultsWarnIndicator.getImmediateTransfer();//立即转移
//                // 筛选绑定测站
//                List<StPptnR> collect1 = stPptnRS.stream().filter(e -> e.getStcd().equals(stationCode)).collect(Collectors.toList());
//                if (!collect1.isEmpty()) {
//                    BigDecimal period = resultsWarnIndicator.getPeriod();//时段
//                    String tm = DateUtil.format(collect1.get(0).getTm(), "yyyy-MM-dd HH:mm:ss");
//                    double v = period.setScale(1, BigDecimal.ROUND_UP).doubleValue();
//                    Date nextDay = null;
//                    nextDay = getDate(tm, nextDay, v);
//
//                    Double data = mountainFloodWarnDataMapper.findRainDataByCondition(stationCode,
//                            DateUtil.format(nextDay, "yyyy-MM-dd HH:mm:ss"), tm, tableName);
//
//
//                    if (immediateTransfer.doubleValue() < data) {
//                        double thresholdData = new BigDecimal(data).subtract(readyMove).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
//                        attPrevTfBase.setThresholdData(thresholdData);
//                        attPrevTfBase.setThresholdTime(DateUtil.format(nextDay, "yyyy-MM-dd HH:mm:ss") + "至" + tm);
//                        attPrevTfBase.setWarnGradeID("5");//5 立即转移 4 准备转移
//                        grade5County++;
//                        grade5villages++;
//                        grade5street++;
//                        grade5Person += tpo;
//                        attPrevTfBasesCp.add(attPrevTfBase);
//                    }
//                    if (readyMove.doubleValue() < data) {
//                        double thresholdData = new BigDecimal(data).subtract(readyMove).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
//                        attPrevTfBase.setThresholdData(thresholdData);
//                        attPrevTfBase.setThresholdTime(DateUtil.format(nextDay, "yyyy-MM-dd HH:mm:ss") + "至" + tm);
//                        attPrevTfBase.setWarnGradeID("4");//5 立即转移 4 准备转移
//                        grade4County++;
//                        grade4villages++;
//                        grade4street++;
//                        grade4Person += tpo;
//                        attPrevTfBasesCp.add(attPrevTfBase);
//                    }
//                }
//            }
//        }
//
//
//        JSONObject data = new JSONObject();
//        attPrevTfBasesCp = attPrevTfBasesCp.stream().collect(
//                collectingAndThen(toCollection(() -> new TreeSet<>(Comparator.comparing(o -> o.getLat() + ";" + o.getLon()))), ArrayList::new)
//        );
//        if (!attPrevTfBasesCp.isEmpty()) {
//            List<ResultsWarnVo> resultsWarnVos = new ArrayList<>();
//            ResultsWarnVo resultsWarnVo = null;
//            Map<String, List<AttPrevTfBase>> stringListMap = attPrevTfBasesCp.stream().collect(Collectors.groupingBy(AttPrevTfBase::getTown));
//            for (Map.Entry<String, List<AttPrevTfBase>> stringListEntry : stringListMap.entrySet()) {
//                resultsWarnVo = new ResultsWarnVo();
//                String key = stringListEntry.getKey();
//                List<AttPrevTfBase> value = stringListEntry.getValue();
//
//
//                List<Villages> villagesList = new ArrayList<>();
//                Villages villages = null;
//                for (AttPrevTfBase attPrevTfBase : value) {
//                    List<AttPrevTfBase> collect = attPrevTfBases.stream().filter(e -> e.getLon()
//                            .equals(attPrevTfBase.getLon()) && e.getLat().equals(attPrevTfBase.getLat())).collect(Collectors.toList());
//                    if (!collect.isEmpty()) {
//                        AttPrevTfBase attPrevTfBase1 = collect.get(0);
//                        Integer tpo = attPrevTfBase1.getTpo();
//                        if ("5".equals(attPrevTfBase.getWarnGradeID())) {
//                            grade5County++;
//                            grade5villages++;
//                            grade5street++;
//                            grade5Person += tpo;
//                        } else if ("4".equals(attPrevTfBase.getWarnGradeID())) {
//                            grade4County++;
//                            grade4villages++;
//                            grade4street++;
//                            grade4Person += tpo;
//                        }
//                        villages = new Villages();
//                        villages.setThresholdTime(attPrevTfBase.getThresholdTime());
//                        villages.setThresholdData(attPrevTfBase.getThresholdData());
//                        villages.setAdnm(attPrevTfBase.getAvi());
//                        villages.setLat(attPrevTfBase.getLat() + "");
//                        villages.setLng(attPrevTfBase.getLon() + "");
//                        villages.setWarnGradeID(attPrevTfBase.getWarnGradeID());
//                        villages.setEffectPersonNumber(attPrevTfBase.getTpo());
//                        villagesList.add(villages);
//                    }
//
//                }
//                resultsWarnVo.setVillages(villagesList);
//                resultsWarnVo.setAdnm(key);
//                resultsWarnVos.add(resultsWarnVo);
//            }
//
//            data.put("town", resultsWarnVos);
//            data.put("promptlyMoveVillage", "");
//            data.put("effectPeople", "");
//            data.put("warningTown", "");
//            data.put("warningMoveVillage", "");
//            data.put("rows", null);
//
//            result.put("data", data);
//        } else {
//            result.put("data", null);
//        }
//        data.put("grade5County", grade5County);
//        data.put("grade5villages", grade5villages);
//        data.put("grade5street", grade5street);
//        data.put("grade5Person", grade5Person);
//
//        data.put("grade4County", grade4County);
//        data.put("grade4villages", grade4villages);
//        data.put("grade4street", grade4street);
//        data.put("grade4Person", grade4Person);
//        //2021.06.19 发送山洪预警短信 huang.zh
//        JSONArray town = data.getJSONArray("town");
//        if (town != null) {
////                List<MessageRecord> records = warningMessageManager.submit(MessageTypes.MOUNTAIN_FLOOD.type(), town);
////                messageRecordMapper.batchInsert(records);
//        }
//        return data;
//
//    }

    public Date getDate(String tm, Date nextDay, double v) {
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


    /**
     * 山洪基础信息统计
     *
     * @return
     */
    MountainTorrentBaseInfoStats mountainTorrentBaseStats() {
        return attPrevTfBaseMapper.baseInfoStats();
    }

    /**
     * 山洪报警信息,我自己实现的一套山洪逻辑
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
        // nnd ，全是重复数据，去重
        allVillageList = (List<MountainTorrentDetailResVo>) removeDuplicates(allVillageList);
        return allVillageList;
    }

    /**
     * 对村庄去重
     *
     * @param allVillageList
     * @return
     */
    private List<? extends AttPrevTfBase> removeDuplicates(List<? extends AttPrevTfBase> allVillageList) {
        if (CollectionUtils.isBlank(allVillageList)) {
            return Collections.emptyList();
        }
        allVillageList = allVillageList.stream().collect(
                collectingAndThen(toCollection(() -> new TreeSet<>(Comparator.comparing(this::getUnitKey))), ArrayList::new)
        );
        return allVillageList;
    }

    /**
     * 村庄的唯一键值对
     *
     * @param village
     * @return
     */
    public String getUnitKey(AttPrevTfBase village) {
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

    /**
     * 报警记录的唯一键
     *
     * @param record
     * @return
     */
    public static String getUnitKey(MountainTorrentMonitorWarnRecord record) {
        return record
                .getNvi()
                .concat(com.ygkj.utils.StringUtils.JOIN_DELIMITER)
                .concat(record.getAvi())
                .concat(com.ygkj.utils.StringUtils.JOIN_DELIMITER)
                .concat(record.getLon().toString())
                .concat(com.ygkj.utils.StringUtils.JOIN_DELIMITER)
                .concat(record.getLat().toString());
    }

    /**
     * 村庄key
     *
     * @param county  区县
     * @param town    乡镇
     * @param village 自然村
     * @return
     */
    public static String getVillageKey(String county, String town, String village) {
        return com.ygkj.utils.StringUtils.EMPTY
                .concat(String.valueOf(county)).concat(com.ygkj.utils.StringUtils.JOIN_DELIMITER)
                .concat(String.valueOf(town)).concat(com.ygkj.utils.StringUtils.JOIN_DELIMITER)
                .concat(String.valueOf(village));
    }


    /**
     * 预报时间、预报降雨量、超预警指标解析
     * 例子 未来3小时（22日10时-13时）降雨56mm,超预警指标4mm,发生山洪灾害的可能性较大。
     *
     * @param villages
     */
    public static void handleVillageDes(Villages villages) {
        //序号、村庄名称、预警时间、预报时间、预报降雨量、超预警指标
        try {
            String desc = villages.getDesc();
            String forecastTime = desc.substring(0, desc.indexOf("降雨"));//预报时间
            villages.setForecastTime(forecastTime);
            String forecastRainfall = desc.substring(desc.indexOf("降雨") + 2, desc.indexOf("mm,超预警指标"));//预报降雨量
            villages.setForecastRainfall(forecastRainfall);
            String overWarnIndex = desc.substring(desc.indexOf(",超预警指标") + 6, desc.indexOf("mm,发生山洪"));//超预警指标
            villages.setOverWarnIndex(overWarnIndex);
            villages.setWarnTime(DateUtils.format(new Date(), "yyyy年MM月dd日HH时"));
        } catch (Exception e) {

        }
    }

    /**
     * 山洪组件C位
     *
     * @param dataTime
     * @return
     */
    public JSONObject mountainTorrentCWindow(Date dataTime) {
        JSONObject result = new JSONObject();
        List<MtFcstMonValueDetailResVo> list = computeFcstMonDetail(dataTime);
        List<MtFcstMonValueDetailResVo> predictWarnList = list.stream().filter(o -> o.getPredictWarnValue() != null).collect(Collectors.toList());
        List<MtFcstMonValueDetailResVo> monitorWarnList = list.stream().filter(o -> o.getMonitorWarnValue() != null).collect(Collectors.toList());
        List<MtFcstMonValueDetailResVo> pastMonitorWarnList = list.stream().filter(o -> o.getPastMonitorWarnValue() != null).collect(Collectors.toList());
        int predictWarnNum = predictWarnList.size();
        int monitorWarnNum = monitorWarnList.size();
        int pastMonitorWarnNum = pastMonitorWarnList.size();
        result.put("predictWarnList", predictWarnList);
        result.put("monitorWarnList", monitorWarnList);
        result.put("pastMonitorWarnList", pastMonitorWarnList);
        result.put("predictWarnNum", predictWarnNum);
        result.put("monitorWarnNum", monitorWarnNum);
        result.put("pastMonitorWarnNum", pastMonitorWarnNum);
        return result;
    }


    /**
     * 山洪c位详表
     *
     * @param dataTime
     * @param warnType 1. 过去N分钟山洪，2，实时监测山洪报警，3，预报山洪
     * @return
     */
    public List<MtFcstMonValueDetailResVo> mountainTorrentCWindowTable(Date dataTime, Integer warnType) {
        if (warnType == null) {
            return Collections.emptyList();
        }
        Predicate<MtFcstMonValueDetailResVo> predicate = null;
        switch (warnType) {
            case 1:
                predicate = o -> o.getPastMonitorWarnValue() != null;
                break;
            case 2:
                predicate = o -> o.getMonitorWarnValue() != null;
                break;
            case 3:
                predicate = o -> o.getPredictWarnValue() != null;
                break;
        }
        if (predicate == null) {
            return Collections.emptyList();
        }
        return computeFcstMonDetail(dataTime).stream().filter(predicate).collect(Collectors.toList());
    }

    /**
     * 山洪报警落点
     *
     * @param dataTime
     * @return
     */
    public List<MtFcstMonValueDetailResVo> mountainTorrentWarnMapPoint(Date dataTime) {
        Predicate<MtFcstMonValueDetailResVo> predicate = MtFcstMonValueDetailResVo::warned;
        return computeFcstMonDetail(dataTime).stream().filter(predicate).collect(Collectors.toList());
    }

    /**
     * 计算山洪预报，监测报警值
     *
     * @param dataTime
     * @return
     */
    public List<MtFcstMonValueDetailResVo> computeFcstMonDetail(Date dataTime) {
        // 先取所有的村庄
        // 因为有部分村庄即使没有测站，也有预报值
        List<MountainTorrentDetailResVo> prevTfList = this.attPrevTfBaseMapper.selectByCondition(new PrevTfQueryVo());
//        prevTfList = (List<MountainTorrentDetailResVo>) removeDuplicates(prevTfList);
        if (CollectionUtils.isBlank(prevTfList)) {
            return Collections.emptyList();
        }

        // 先从缓存里取现在的
        // 此时为实时监测值
        List<MountainTorrentDetailResVo> monitorList = (List<MountainTorrentDetailResVo>) getData(Builder.of(UnitDataReqVo::new)
                .with(UnitDataReqVo::setStrategy, "mountainTorrentWarningInfo")
                .with(UnitDataReqVo::putParam, "reqParam", new MountainTorrentDrillReqVo())
                .build());

        // 然后从数据库里取过去24小时最新的报警记录
        List<MountainTorrentMonitorWarnRecord> pastMonitorList = this.mountainFloodMapper.selectPastMinutesNewestWarnRecord(dataTime, 24 * 60);

        // 再从未来的预报值
        JSONObject predictInfo = null;
        if (dataTime == null) {
            // 如果dataTime==null 从缓存里取预报值
            predictInfo = (JSONObject) getData(Builder.of(UnitDataReqVo::new)
                    .with(UnitDataReqVo::setStrategy, "floodWarning4Predict")
                    .build());
        } else {
            // 如果dataTime!=null 从mountain_flood_predict里取tm>=dataTime 且最小的那个
            String predictInfoValue = this.mountainFloodMapper.selectLowestPredictInfo(dataTime, 10);
            try {
                predictInfo = JSONObject.parseObject(predictInfoValue, JSONObject.class);
            } catch (Exception e) {

            }
        }
        // 预报报警
        Map<String, MtFcstMonWarnValueResVo> predictMap = Collections.emptyMap();
        if (predictInfo != null) {
            try {
                List<ResultsWarnVo> resultsWarnList = predictInfo.getJSONObject("data").getJSONArray("town").toJavaList(ResultsWarnVo.class);
                predictMap = new HashMap<>(resultsWarnList.size());
                for (ResultsWarnVo resultsWarnVo : resultsWarnList) {
                    List<Villages> villages = resultsWarnVo.getVillages();
                    if (CollectionUtils.isBlank(villages)) {
                        continue;
                    }
                    for (Villages village : villages) {
                        handleVillageDes(village);
                        predictMap.put(getVillageKey(resultsWarnVo.getCountyName(), resultsWarnVo.getAdnm(), village.getAdnm()), new MtFcstMonWarnValueResVo(village));
                    }
                }
            } catch (Exception e) {

            }
        }

        // 实时监控报警
        Map<String, MountainTorrentDetailResVo> monitorMap = monitorList.stream().collect(Collectors.toMap(this::getUnitKey, Function.identity()));

        // 过去24小时监控报警
        Map<String, MountainTorrentMonitorWarnRecord> pastMonitorMap = pastMonitorList.stream().collect(Collectors.toMap(MountainTorrentManager::getUnitKey, Function.identity()));

        List<MtFcstMonValueDetailResVo> resultList = new ArrayList<>();
        for (MountainTorrentDetailResVo temp : prevTfList) {
            MtFcstMonValueDetailResVo target = new MtFcstMonValueDetailResVo();
            BeanUtils.copyProperties(temp, target);
            resultList.add(target);

            // 预报里面的是自然村
            MtFcstMonWarnValueResVo predictValue = predictMap.get(getVillageKey(temp.getCounty(), temp.getTown(), temp.getNvi()));
            target.setPredictWarnValue(predictValue);

            // 测站为空所以肯定不存在监测值
            if (StringUtils.isBlank(temp.getStationCode())) {
                continue;
            }
            String uniqueKey = getUnitKey(temp);
            MountainTorrentDetailResVo monitor = monitorMap.get(uniqueKey);
            if (monitor != null && CollectionUtils.isNotBlank(monitor.getIndicatorList())) {
                // 取等级最高，超过阈值最大的那个
                MountainTorrentIndicatorResVo indicator = monitor.getIndicatorList().stream().filter(o -> o.getWarnGradeID() != null).max(Comparator.comparing(MountainTorrentIndicatorResVo::getWarnGradeID).thenComparing(MountainTorrentIndicatorResVo::getOverThresholdVal)).orElse(null);
                if (indicator != null) {
                    target.setMonitorWarnValue(new MtFcstMonWarnValueResVo(indicator));
                }
            }

            MountainTorrentMonitorWarnRecord pastMonitor = pastMonitorMap.get(uniqueKey);
            if (pastMonitor != null) {
                List<MountainTorrentMonitorWarnValue> valueList = Collections.emptyList();
                try {
                    valueList = JSONArray.parseArray(pastMonitor.getWarnValue(), MountainTorrentMonitorWarnValue.class);
                } catch (Exception e) {

                }
                if (CollectionUtils.isNotBlank(valueList)) {
                    MountainTorrentMonitorWarnValue warnValue = valueList.stream().peek(MountainTorrentMonitorWarnValue::buildWarnGradeIdAsInt).filter(o -> o.getWarnGradeIdAsInt() != null).max(Comparator.comparing(MountainTorrentMonitorWarnValue::getWarnGradeIdAsInt).thenComparing(MountainTorrentMonitorWarnValue::getOverThresholdVal)).orElse(null);
                    if (warnValue != null) {
                        target.setPastMonitorWarnValue(new MtFcstMonWarnValueResVo(warnValue));
                    }
                }
            }
        }
        return resultList;
    }


    /**
     * 持久化山洪监测报警数据
     *
     * @param allVillageList
     */
    public void persistWarnInfo(List<MountainTorrentDetailResVo> allVillageList) {
        log.info("开始持久化监测报警信息");
        if (CollectionUtils.isBlank(allVillageList)) {
            return;
        }
        Date warnTime = new Date();
        for (MountainTorrentDetailResVo prevIf : allVillageList) {
            try {
                List<MountainTorrentIndicatorResVo> indicatorList = prevIf.getIndicatorList();
                if (CollectionUtils.isBlank(indicatorList)) {
                    continue;
                }
                indicatorList = indicatorList.stream()
                        // todo 测试用设置值，注意会影响到缓存，因为是同一个对象
//                        .peek(o -> o.setWarnGradeID(4))
                        .filter(o -> warnGradeID5Int.equals(o.getWarnGradeID()) || warnGradeID4Int.equals(o.getWarnGradeID()))
                        .collect(Collectors.toList());
                if (CollectionUtils.isBlank(indicatorList)) {
                    continue;
                }
                int hash = getUnitKey(prevIf).hashCode();
                MountainTorrentMonitorWarnRecord record = new MountainTorrentMonitorWarnRecord();
                record.setHashKey(hash);
                record.setAvi(prevIf.getAvi());
                record.setNvi(prevIf.getNvi());
                record.setLat(prevIf.getLat());
                record.setLon(prevIf.getLon());
                record.setWarnTm(warnTime);
                List<MountainTorrentMonitorWarnValue> valueList = new ArrayList<>(indicatorList.size());
                for (MountainTorrentIndicatorResVo indicator : indicatorList) {
                    MountainTorrentMonitorWarnValue tempValue = new MountainTorrentMonitorWarnValue();
                    tempValue.setIndicatorId(indicator.getId());
                    tempValue.setOverThresholdVal(indicator.getOverThresholdVal());
                    tempValue.setTm(indicator.getTm());
                    tempValue.setPeriod(indicator.getPeriod());
                    tempValue.setWarnGradeId(indicator.getWarnGradeID().toString());
                    tempValue.setRealTimeRainfall(indicator.getRealTimeRainfall());
                    tempValue.setWarnTm(warnTime);
                    tempValue.setHashKey(hash);
                    valueList.add(tempValue);
                }
                String warnValue = JSONObject.toJSONString(valueList);
                record.setWarnValue(warnValue);
                mountainFloodMapper.insertMonitorWarnRecord(record);
                for (MountainTorrentMonitorWarnValue value : valueList) {
                    try {
                        mountainFloodMapper.insertMonitorWarnValue(value);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 批量发送报警短信
     * XXXX村庄，截止目前累计XXX小时降雨超过XXX毫米，已经达到准备转移/立即转移的标准。
     *
     * @param warnList
     */
    public CommonResult batchSendWarnSms(List<MtFcstMonValueDetailResVo> warnList, Integer warnType) {
        if (CollectionUtils.isBlank(warnList) || warnType == null) {
            return CommonResult.failed("入参为空");
        }
        Predicate<MtFcstMonValueDetailResVo> predicate = null;
        switch (warnType) {
            case 1:
                predicate = o -> o.getPastMonitorWarnValue() != null;
                break;
            case 2:
                predicate = o -> o.getMonitorWarnValue() != null;
                break;
            case 3:
                predicate = o -> o.getPredictWarnValue() != null;
        }
        if (predicate == null) {
            return CommonResult.failed("报警类型为空");
        }
        warnList = warnList.stream().filter(predicate).filter(o -> StringUtils.isNotBlank(o.getChargerPhone())).collect(Collectors.toList());
        if (CollectionUtils.isBlank(warnList)) {
            return CommonResult.failed("无适合报警的记录");
        }
        List<SysSendMessageLog> messageList = new ArrayList<>(warnList.size());
        warnList.stream()
                .collect(Collectors.groupingBy(AttPrevTfBase::getChargerPhone))
                .forEach((phone, warnTempList) -> {

                    for (MtFcstMonValueDetailResVo detail : warnTempList) {
                        String content = StringUtils.EMPTY;
                        MtFcstMonWarnValueResVo value;
                        switch (warnType) {
                            case 1:
                                value = detail.getPastMonitorWarnValue();
                                content = pastMonitorWarnSmsTemplate(detail.getNvi(), value.getWarnTm(), value.getPeriod(), value.getRainfall(), value.getWarnGradeId());
                                break;
                            case 2:
                                value = detail.getMonitorWarnValue();
                                content = monitorWarnSmsTemplate(detail.getNvi(), value.getPeriod(), value.getRainfall(), value.getWarnGradeId());
                                break;
                            case 3:
                                value = detail.getPredictWarnValue();
                                content = predictWarnSmsTemplate(detail.getNvi(), value.getDesc());
                                break;
                        }
                        SysSendMessageLog message = SysSendMessageLogCreator.createMessage(content, phone,
                                StringUtils.EMPTY, StringUtils.EMPTY,
                                StringUtils.EMPTY, StringUtils.isBlank(detail.getChargerName()) ? "山洪报警接收人：" + phone : detail.getChargerName(),
                                SysSendMessageLog.MessageSendType.SMS.type(),
                                "山洪报警",
                                SysSendMessageLog.SystemType.FLOOD_PROTECT.type(), SysSendMessageLog.messageType.WARNING.type());
                        messageList.add(message);
                    }

                });

        CommonResult resp = warnServiceClient.sendMessage(messageList);
        if (CommonCode.SUCCESS.code() == resp.getCode()) {
            return CommonResult.success();
        } else {
            return CommonResult.failed("一键报警成功");
        }
    }


    /**
     * 批量发送报警短信
     * XXXX村庄，截止目前累计XXX小时降雨超过XXX毫米，已经达到准备转移/立即转移的标准。
     *
     * @param warnDetail
     */
    public CommonResult singleSendWarnSms(MtFcstMonValueDetailResVo warnDetail, Integer warnType) {
        if (warnDetail == null || warnType == null) {
            return CommonResult.failed("入参为空");
        }
        Predicate<MtFcstMonValueDetailResVo> predicate = null;
        switch (warnType) {
            case 1:
                predicate = o -> o.getPastMonitorWarnValue() != null;
                break;
            case 2:
                predicate = o -> o.getMonitorWarnValue() != null;
                break;
            case 3:
                predicate = o -> o.getPredictWarnValue() != null;
        }
        if (predicate == null) {
            return CommonResult.failed("报警类型为空");
        }
        List<MtFcstMonValueDetailResVo> warnList = Collections.singletonList(warnDetail);
        warnList = warnList.stream().filter(predicate).filter(o -> StringUtils.isNotBlank(o.getChargerPhone())).collect(Collectors.toList());
        if (CollectionUtils.isBlank(warnList)) {
            return CommonResult.failed("无适合报警的记录");
        }
        String content = StringUtils.EMPTY;
        MtFcstMonWarnValueResVo value;
        switch (warnType) {
            case 1:
                value = warnDetail.getPastMonitorWarnValue();
                content = pastMonitorWarnSmsTemplate(warnDetail.getNvi(), value.getWarnTm(), value.getPeriod(), value.getRainfall(), value.getWarnGradeId());
                break;
            case 2:
                value = warnDetail.getMonitorWarnValue();
                content = monitorWarnSmsTemplate(warnDetail.getNvi(), value.getPeriod(), value.getRainfall(), value.getWarnGradeId());
                break;
            case 3:
                value = warnDetail.getPredictWarnValue();
                content = predictWarnSmsTemplate(warnDetail.getNvi(), value.getDesc());
                break;
        }
        SysSendMessageLog message = SysSendMessageLogCreator.createMessage(content, warnDetail.getChargerPhone(),
                StringUtils.EMPTY, StringUtils.EMPTY,
                StringUtils.EMPTY, StringUtils.isBlank(warnDetail.getChargerName()) ? "山洪报警接收人：" + warnDetail.getChargerPhone() : warnDetail.getChargerName(),
                SysSendMessageLog.MessageSendType.SMS.type(),
                "山洪报警",
                SysSendMessageLog.SystemType.FLOOD_PROTECT.type(), SysSendMessageLog.messageType.WARNING.type());
        CommonResult resp = warnServiceClient.sendMessage(Collections.singletonList(message));
        if (CommonCode.SUCCESS.code() == resp.getCode()) {
            return CommonResult.success();
        } else {
            return CommonResult.failed("一键报警成功");
        }
    }

    /**
     * XXXX村庄，截止目前累计XXX小时降雨超过XXX毫米，已经达到准备转移/立即转移的标准。
     *
     * @return
     */
    final static String monitorWarnSmsTemplate = "#{nvi}村庄，截止目前累计#{period}小时降雨超过#{rainfall}毫米，已经达到准备转移/立即转移的标准。";

    public static String monitorWarnSmsTemplate(String nvi, String period, String rainfall, String warnGradeID) {
        return monitorWarnSmsTemplate
                .replace("#{nvi}", String.valueOf(nvi))
                .replace("#{period}", String.valueOf(period))
                .replace("#{rainfall}", String.valueOf(rainfall));
    }

    /**
     * XXXX村庄，截止XXXXXX时累计XXX小时降雨超过XXX毫米，已经达到准备转移/立即转移的标准。
     *
     * @return
     */
    final static String pastMonitorWarnSmsTemplate = "#{nvi}村庄，截止#{warnTm}累计#{period}小时降雨超过#{rainfall}毫米，已经达到准备转移/立即转移的标准。";

    /**
     * @param nvi         自然村
     * @param warnTm
     * @param period
     * @param rainfall
     * @param warnGradeID
     * @return
     */
    public static String pastMonitorWarnSmsTemplate(String nvi, String warnTm, String period, String rainfall, String warnGradeID) {
        return pastMonitorWarnSmsTemplate
                .replace("#{nvi}", String.valueOf(nvi))
                .replace("#{warnTm}", String.valueOf(warnTm))
                .replace("#{period}", String.valueOf(period))
                .replace("#{rainfall}", String.valueOf(rainfall));
    }

    /**
     * 例子 XXXX村庄，未来3小时（22日10时-13时）降雨56mm,超预警指标4mm,发生山洪灾害的可能性较大。
     *
     * @return
     */
    final static String predictWarnSmsTemplate = "#{nvi}村庄，#{desc}";

    public static String predictWarnSmsTemplate(String nvi, String desc) {
        return predictWarnSmsTemplate
                .replace("#{nvi}", String.valueOf(nvi))
                .replace("#{desc}", String.valueOf(desc));
    }

}
