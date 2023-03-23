package com.ygkj.big.screen.unit;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ygkj.big.screen.feign.ProjectManageClient;
import com.ygkj.big.screen.vo.request.*;
import com.ygkj.big.screen.vo.response.RsvrStationWaterResVo;
import com.ygkj.enums.EngScalEnum;
import com.ygkj.utils.BeanComparator;
import com.ygkj.utils.Builder;
import com.ygkj.utils.CollectionUtils;
import com.ygkj.vo.PageVO;
import com.ygkj.water.model.CommonCode;
import com.ygkj.water.model.CommonResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;

/**
 * @author xq
 * @Description
 * @Date 2021/7/13
 */
@Component
@Slf4j
public class RsvrManager extends AbstractUnitManager {

    @Autowired
    ProjectManageClient projectManageClient;

    private ConcurrentHashMap<String, Object> cache;

    public RsvrManager(@Autowired ApplicationContext context) {
        super(context);
        this.cache = new ConcurrentHashMap<>();
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
            case "rsvrProjStats1": {
                Date dataTime = reqVo.getParam("dataTime", Date.class);
                Object result = null;
                if ((result = cache.get(buildDefaultCacheKey("rsvrProjStats1", dataTime))) == null) {
                    result = rsvrProjStats1(dataTime);
                }
                return result;
            }
            case "rsvrProjStats2": {
                String scaleType = reqVo.getParam("scaleType", String.class);
                Date dataTime = reqVo.getParam("dataTime", Date.class);
                Object result = null;
                if ((result = cache.get(buildDefaultCacheKey("rsvrProjStats2", scaleType, dataTime))) == null) {
                    result = rsvrProjStats2(scaleType, dataTime);
                }
                return result;
            }
            case "rsvrProjDrainageStats": {
                Date dataTime = reqVo.getParam("dataTime", Date.class);
                Object result = null;
                if ((result = cache.get(buildDefaultCacheKey("rsvrProjDrainageStats", dataTime))) == null) {
                    result = rsvrProjDrainageStats(dataTime);
                }
                return result;
            }
            case "allRsvrRealtimeLevel": {
                Object result = null;
                if ((result = cache.get(buildDefaultCacheKey("allRsvrRealtimeLevel"))) == null) {
                    result = allRsvrRealtimeLevel();
                }
                return result;
            }
            case "rsvrProjList": {
                RsvrStationWaterReqVo reqParam = reqVo.getParam("reqVo", RsvrStationWaterReqVo.class);
                return rsvrProjList(reqParam);
            }
            default:
                return null;
        }
    }

    @Override
    public void buildData(UnitDataBuilderReqVo reqVo) {
        log.info("提交了一项水库缓存任务");
        Date dataTime = null;
        String scaleType = "大中型";
        cache.put(buildDefaultCacheKey("rsvrProjStats1", dataTime), rsvrProjStats1(dataTime));
        cache.put(buildDefaultCacheKey("rsvrProjStats2", scaleType, dataTime), rsvrProjStats2(scaleType, dataTime));
        cache.put(buildDefaultCacheKey("rsvrProjDrainageStats", dataTime), rsvrProjDrainageStats(dataTime));
        cache.put(buildDefaultCacheKey("allRsvrRealtimeLevel"), allRsvrRealtimeLevel());
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

    public JSONObject rsvrProjStats1(Date dataTime) {
        JSONObject result = new JSONObject();
        long allNormal = 0, allOverLimit = 0, allTotal = 0;
        long lmNormal = 0, lmOverLimit = 0, lmTotal = 0;
        long s1Normal = 0, s1OverLimit = 0, s1Total = 0;
        long s2Normal = 0, s2OverLimit = 0, s2Total = 0;
        CommonResult<PageVO<LinkedHashMap>> resp = projectManageClient.rsvrWaterLevel(Builder.of(RsvrStationWaterReqVo::new)
                .with(RsvrStationWaterReqVo::setDataTime, dataTime)
                .build());
        if (resp != null && resp.getCode() == CommonCode.SUCCESS.code() && CollectionUtils.isNotBlank(resp.getData().getList())) {
            List<RsvrStationWaterResVo> data = JSONArray.parseArray(JSONArray.toJSONString(resp.getData().getList()), RsvrStationWaterResVo.class);

            Map<String, Long> allMap = data.stream().collect(Collectors.groupingBy(o -> (o.getOverLimitVal() != null && o.getOverLimitVal() > 0) ? "overLimit" : "normal",
                    Collectors.counting()));
            Map<String, Long> lmMap = data.stream().filter(o -> (EngScalEnum.L1.getDesc().equals(o.getEngScal())
                    || EngScalEnum.L2.getDesc().equals(o.getEngScal())
                    || EngScalEnum.MID.getDesc().equals(o.getEngScal()))).collect(Collectors.groupingBy(o -> (o.getOverLimitVal() != null && o.getOverLimitVal() > 0) ? "overLimit" : "normal",
                    Collectors.counting()));
            Map<String, Long> s1Map = data.stream().filter(o -> (EngScalEnum.S1.getDesc().equals(o.getEngScal()))).collect(Collectors.groupingBy(o -> (o.getOverLimitVal() != null && o.getOverLimitVal() > 0) ? "overLimit" : "normal",
                    Collectors.counting()));
            Map<String, Long> s2Map = data.stream().filter(o -> (EngScalEnum.S2.getDesc().equals(o.getEngScal()))).collect(Collectors.groupingBy(o -> (o.getOverLimitVal() != null && o.getOverLimitVal() > 0) ? "overLimit" : "normal",
                    Collectors.counting()));
            Long zeroL = 0L;
            allNormal = allMap.getOrDefault("normal", zeroL);
            allOverLimit = allMap.getOrDefault("overLimit", zeroL);
            allTotal = data.size();
            lmNormal = lmMap.getOrDefault("normal", zeroL);
            lmOverLimit = lmMap.getOrDefault("overLimit", zeroL);
            lmTotal = lmNormal + lmOverLimit;
            s1Normal = s1Map.getOrDefault("normal", zeroL);
            s1OverLimit = s1Map.getOrDefault("overLimit", zeroL);
            s1Total = s1Normal + s1OverLimit;
            s2Normal = s2Map.getOrDefault("normal", zeroL);
            s2OverLimit = s2Map.getOrDefault("overLimit", zeroL);
            s2Total = s2Normal + s2OverLimit;
        }
        result.put("allNormal", allNormal);
        result.put("allOverLimit", allOverLimit);
        result.put("allTotal", allTotal);
        result.put("lmNormal", lmNormal);
        result.put("lmOverLimit", lmOverLimit);
        result.put("lmTotal", lmTotal);
        result.put("s1Normal", s1Normal);
        result.put("s1OverLimit", s1OverLimit);
        result.put("s1Total", s1Total);
        result.put("s2Normal", s2Normal);
        result.put("s2OverLimit", s2OverLimit);
        result.put("s2Total", s2Total);
        return result;
    }

    public JSONObject rsvrProjStats2(String scaleType, Date dataTime) {
        JSONObject result = new JSONObject();
        double avgStorageRatio = 0D;
        double totalStorage = 0D;
        double totalCurrentStorage = 0D;
        String engScales = null;
        List<RsvrStationWaterResVo> storageList = Collections.emptyList();
        if ("大中型".equals(scaleType)) {
            engScales = String.join(com.ygkj.utils.StringUtils.JOIN_DELIMITER, EngScalEnum.L1.getDesc(), EngScalEnum.L2.getDesc(), EngScalEnum.MID.getDesc());
        } else if (EngScalEnum.S1.getDesc().equals(scaleType)) {
            engScales = EngScalEnum.S1.getDesc();
        } else if (EngScalEnum.S2.getDesc().equals(scaleType)) {
            engScales = EngScalEnum.S2.getDesc();
        } else {
            return result;
        }
        CommonResult<PageVO<LinkedHashMap>> resp = projectManageClient.rsvrWaterLevel(Builder.of(RsvrStationWaterReqVo::new)
                .with(RsvrStationWaterReqVo::setDataTime, dataTime)
                .with(RsvrStationWaterReqVo::setEngScales, engScales)
                .with(RsvrStationWaterReqVo::setFloodStorageAbility, true)
                .with(RsvrStationWaterReqVo::setLimitStorageAbility, true)
                .with(RsvrStationWaterReqVo::setStorageRatio, true)
                .build());
        if (resp != null && resp.getCode() == CommonCode.SUCCESS.code() && CollectionUtils.isNotBlank(resp.getData().getList())) {
            List<RsvrStationWaterResVo> data = JSONArray.parseArray(JSONArray.toJSONString(resp.getData().getList()), RsvrStationWaterResVo.class);
//            OptionalDouble avgStorageRatioOpt = data.stream().filter(o -> o.getStorageRatio() != null).mapToDouble(RsvrStationWaterResVo::getStorageRatio).average();
//            if (avgStorageRatioOpt.isPresent()) {
//                avgStorageRatio = avgStorageRatioOpt.getAsDouble();
//            }
            totalStorage = data.stream().filter(o -> o.getLimitLevelCapacity() != null).mapToDouble(RsvrStationWaterResVo::getLimitLevelCapacity).sum();
            totalCurrentStorage = data.stream().filter(o -> o.getWaterLevelCapacity() != null).mapToDouble(RsvrStationWaterResVo::getWaterLevelCapacity).sum();
            if (totalStorage > 0) {
                avgStorageRatio = totalCurrentStorage * 100 / totalStorage;
            }
            storageList = data.stream()
//                    .filter(o -> o.getStorageAbility() != null)
//                    .sorted(Comparator.comparing(RsvrStationWaterResVo::getStorageAbility))
                    .sorted((o1, o2) -> {
                        if (o1.getFloodStorageAbility() != null && o2.getFloodStorageAbility() != null) {
                            return o1.getFloodStorageAbility().compareTo(o2.getFloodStorageAbility());
                        } else if (o1.getFloodStorageAbility() == null && o2.getFloodStorageAbility() != null) {
                            return 1;
                        } else if (o1.getFloodStorageAbility() != null && o2.getFloodStorageAbility() == null) {
                            return -1;
                        } else {
                            return 0;
                        }
                    })
                    .collect(Collectors.toList());
        }
        result.put("avgStorageRatio", Double.parseDouble(String.format("%.1f", avgStorageRatio)));
        result.put("totalStorage", Double.parseDouble(String.format("%.1f", totalStorage)));
        result.put("totalCurrentStorage", Double.parseDouble(String.format("%.1f", totalCurrentStorage)));
        result.put("storageList", storageList);
        return result;
    }

    public JSONObject rsvrProjDrainageStats(Date dataTime) {
        JSONObject result = new JSONObject();
        long drainageRsvrNum = 0;
        double drainageTotal = 0D;
        List<RsvrStationWaterResVo> list = Collections.emptyList();
        String engScales = String.join(com.ygkj.utils.StringUtils.JOIN_DELIMITER, EngScalEnum.L1.getDesc(), EngScalEnum.L2.getDesc(), EngScalEnum.MID.getDesc());
        CommonResult<PageVO<LinkedHashMap>> resp = projectManageClient.rsvrWaterLevel(Builder.of(RsvrStationWaterReqVo::new)
                .with(RsvrStationWaterReqVo::setDataTime, dataTime)
                .with(RsvrStationWaterReqVo::setEngScales, engScales)
                .with(RsvrStationWaterReqVo::setDrainage24H, true)
                .build());
        if (resp != null && resp.getCode() == CommonCode.SUCCESS.code() && CollectionUtils.isNotBlank(resp.getData().getList())) {
            List<RsvrStationWaterResVo> data = JSONArray.parseArray(JSONArray.toJSONString(resp.getData().getList()), RsvrStationWaterResVo.class);
            List<RsvrStationWaterResVo> drainageList = data.stream().filter(o -> o.getDrainage24H() != null && o.getDrainage24H() > 0).collect(Collectors.toList());
            drainageRsvrNum = drainageList.size();
            drainageTotal = drainageList.stream().mapToDouble(RsvrStationWaterResVo::getDrainage24H).sum();
            drainageList.stream().forEach(o -> {
                if (o.getDrainage24H() != null) {
                    o.setDrainage24H(Double.parseDouble(String.format("%.1f", o.getDrainage24H())));
                }
            });
            list = data.stream().sorted(new BeanComparator<>("drainage24H").reversed()).collect(Collectors.toList());
        }
        result.put("drainageTotal", Double.parseDouble(String.format("%.1f", drainageTotal)));
        result.put("drainageRsvrNum", drainageRsvrNum);
        result.put("list", list);
        return result;
    }

    public JSONObject rsvrProjList(RsvrStationWaterReqVo reqVo) {
        if (reqVo == null) {
            reqVo = new RsvrStationWaterReqVo();
        }
        reqVo.setFloodStorageAbility(true);
        reqVo.setLimitStorageAbility(true);
        reqVo.setStorageRatio(true);
        JSONObject result = new JSONObject();
        // 列表
        result.put("list", Collections.emptyList());
        // 平均蓄水率
        result.put("avgStorageRatio", 0D);
        // 平均至汛限纳蓄水能力
        result.put("avgLimitStorageAbility", 0D);
        // 超汛限水库数量
        result.put("overLimitNum", 0);
        // 水库总数
        result.put("total", 0);
        // 大中型、小型
        if (StringUtils.isNotBlank(reqVo.getEngScales())) {
            if ("大中型".equals(reqVo.getEngScales())) {
                reqVo.setEngScales(String.join(com.ygkj.utils.StringUtils.JOIN_DELIMITER, EngScalEnum.L1.getDesc(), EngScalEnum.L2.getDesc(), EngScalEnum.MID.getDesc()));
            } else if ("小型".equals(reqVo.getEngScales())) {
                reqVo.setEngScales(String.join(com.ygkj.utils.StringUtils.JOIN_DELIMITER, EngScalEnum.S1.getDesc(), EngScalEnum.S2.getDesc()));
            }
        }
        CommonResult<PageVO<LinkedHashMap>> resp = projectManageClient.rsvrWaterLevel(reqVo);
        if (resp != null && resp.getCode() == CommonCode.SUCCESS.code() && CollectionUtils.isNotBlank(resp.getData().getList())) {
            List<RsvrStationWaterResVo> data = JSONArray.parseArray(JSONArray.toJSONString(resp.getData().getList()), RsvrStationWaterResVo.class);
//            OptionalDouble avgStorageRatio = data.stream().filter(o -> o.getStorageRatio() != null).mapToDouble(RsvrStationWaterResVo::getStorageRatio).average();
            OptionalDouble avgLimitStorageAbility = data.stream().filter(o -> o.getLimitStorageAbility() != null).mapToDouble(RsvrStationWaterResVo::getLimitStorageAbility).average();
            long overLimitNum = data.stream().filter(o -> (o.getOverLimitVal() != null && o.getOverLimitVal() > 0)).count();
//            if (avgStorageRatio.isPresent()) {
//                result.put("avgStorageRatio", Double.parseDouble(String.format("%.1f", avgStorageRatio.getAsDouble())));
//            }
            // 平均蓄水率
            double totalStorage = data.stream().filter(o -> o.getLimitLevelCapacity() != null).mapToDouble(RsvrStationWaterResVo::getLimitLevelCapacity).sum();
            double totalCurrentStorage = data.stream().filter(o -> o.getWaterLevelCapacity() != null).mapToDouble(RsvrStationWaterResVo::getWaterLevelCapacity).sum();
            if (totalStorage > 0) {
                result.put("avgStorageRatio", Double.parseDouble(String.format("%.1f", totalCurrentStorage * 100 / totalStorage)));
            }
            if (avgLimitStorageAbility.isPresent()) {
                result.put("avgLimitStorageAbility", Double.parseDouble(String.format("%.1f", avgLimitStorageAbility.getAsDouble())));
            }
            RsvrStationWaterReqVo finalReqVo = reqVo;
            if (finalReqVo.getDrainage24H() == null) {
                finalReqVo.setDrainage24H(false);
            }
            if (finalReqVo.getDrainage48H() == null) {
                finalReqVo.setDrainage48H(false);
            }
            if (finalReqVo.getDrainage72H() == null) {
                finalReqVo.setDrainage72H(false);
            }
            if (finalReqVo.getDrainage24H() || finalReqVo.getDrainage48H() || finalReqVo.getDrainage72H()) {
                data.forEach(o -> {
                    if (finalReqVo.getDrainage24H() && o.getDrainage24H() != null) {
                        o.setDrainage24H(Double.parseDouble(String.format("%.1f", o.getDrainage24H())));
                    }
                    if (finalReqVo.getDrainage48H() && o.getDrainage48H() != null) {
                        o.setDrainage48H(Double.parseDouble(String.format("%.1f", o.getDrainage48H())));
                    }
                    if (finalReqVo.getDrainage72H() && o.getDrainage72H() != null) {
                        o.setDrainage72H(Double.parseDouble(String.format("%.1f", o.getDrainage72H())));
                    }
                });
            }
            result.put("overLimitNum", overLimitNum);
            List<RsvrStationWaterResVo> tempList = data.stream().filter(Objects::nonNull).peek(o -> Optional.ofNullable(EngScalEnum.getByLevel(o.getEngScal()))
                    .ifPresent(o1 -> o.setEngScal(o1.getDesc())))
                    .collect(Collectors.toList());

            // 默认情况下按超汛限值降序
            if (StringUtils.isBlank(reqVo.getOrderColumn())) {
                tempList = tempList.stream().sorted(((o1, o2) -> {
                    if (o1.getOverLimitVal() != null && o2.getOverLimitVal() != null) {
                        return o2.getOverLimitVal().compareTo(o1.getOverLimitVal());
                    } else if (o1.getOverLimitVal() != null && o2.getOverLimitVal() == null) {
                        return -1;
                    } else if (o1.getOverLimitVal() == null && o2.getOverLimitVal() != null) {
                        return 1;
                    } else {
                        return 0;
                    }
                })).collect(Collectors.toList());
            }
            result.put("list", tempList);
            result.put("total", tempList.size());
        }
        return result;
    }

    public Object allRsvrRealtimeLevel() {
        return projectManageClient.rsvrWaterLevel(new RsvrStationWaterReqVo()).getData().getList();
    }

}
