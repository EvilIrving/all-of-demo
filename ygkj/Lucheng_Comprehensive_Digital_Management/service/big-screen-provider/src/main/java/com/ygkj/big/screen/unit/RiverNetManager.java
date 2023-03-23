package com.ygkj.big.screen.unit;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.ygkj.big.screen.feign.GraphClient;
import com.ygkj.big.screen.mapper.AttAdcdBaseMapper;
import com.ygkj.big.screen.model.AttAdcdBase;
import com.ygkj.big.screen.model.AttRnBase;
import com.ygkj.big.screen.vo.request.*;
import com.ygkj.big.screen.vo.response.RivernetWaterLevelResVo;
import com.ygkj.big.screen.vo.response.RsvrStationWaterResVo;
import com.ygkj.utils.Builder;
import com.ygkj.utils.CollectionUtils;
import com.ygkj.utils.MapComparator;
import com.ygkj.water.model.CommonCode;
import com.ygkj.water.model.CommonResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author xq
 * @Description 河网组件
 * @Date 2021/7/13
 */
@Component
@Slf4j
public class RiverNetManager extends AbstractUnitManager {

    @Autowired
    private GraphClient graphClient;

    @Resource
    private AttAdcdBaseMapper adcdBaseMapper;

    @Autowired
    ShortForecastManager shortForecastManager;

    @Autowired
    RsvrManager rsvrManager;

    private ConcurrentHashMap<String, Object> cache;

    public RiverNetManager(@Autowired ApplicationContext context) {
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
            case "riverNetworkList": {
                RivNetListQueryReqVo param = reqVo.getParam("reqVo", RivNetListQueryReqVo.class);
                return riverNetworkList(param);
            }
            case "riverNetStats": {
                Date dataTime = reqVo.getParam("dataTime", Date.class);
                Integer type = reqVo.getParam("type", Integer.class);
                Object result = null;
                if ((result = cache.get(buildDefaultCacheKey("riverNetStats", type, dataTime))) == null) {
                    result = riverNetStats(dataTime, type);
                }
                return result;
            }
            case "allRivernetRealtimeLevel": {
                Object result = null;
                if ((result = cache.get(buildDefaultCacheKey("allRivernetRealtimeLevel"))) == null) {
                    result = allRivernetRealtimeLevel();
                }
                return result;
            }
            case "rivernetReplayStats": {
                RivernetReplayReqVo param = reqVo.getParam("reqVo", RivernetReplayReqVo.class);
                return rivernetReplayStats(param);
            }
            default:
                break;
        }
        return null;
    }

    @Override
    public void buildData(UnitDataBuilderReqVo reqVo) {
        log.info("提交了一项河网缓存任务");
        Date dataTime = null;
        Integer type = null;
        cache.put(buildDefaultCacheKey("riverNetStats", type, dataTime), riverNetStats(dataTime, type));
        type = 2;
        cache.put(buildDefaultCacheKey("riverNetStats", type, dataTime), riverNetStats(dataTime, type));
        cache.put(buildDefaultCacheKey("allRivernetRealtimeLevel"), allRivernetRealtimeLevel());
    }

    @Override
    public ExecutorService getExecutor() {
        return getDefaultConfigExecutor();
    }

    /**
     * 每十分钟提交一次任务
     */
    @Scheduled(cron = "0 1/5 * * * ? ")
    public void updateCacheTask() {
        doBuild(null);
    }

    /**
     * 河网测站统计，统计正常、超警戒、超保证数目
     *
     * @param dataTime 复盘时刻
     * @return
     */
    public JSONObject riverNetStats(Date dataTime, Integer type) {
        if (type == null) {
            type = 1;
        }
        if (type == 1) {
            return rivernetWaterLevelStats(dataTime);
        } else if (type == 2) {
            return rivernetStorageStats(dataTime);
        } else {
            return null;
        }
    }

    private JSONObject rivernetWaterLevelStats(Date dataTime) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        CommonResult<List<LinkedHashMap>> resp = graphClient.listRivernet(Builder.of(RivernetWaterLevelReqVo::new)
                .with(RivernetWaterLevelReqVo::setDataTime, dataTime == null ? null : sdf.format(dataTime))
                .build());
        String stCodes = StringUtils.EMPTY;
        List<JSONObject> list = Collections.emptyList();
        if (resp.getCode() == CommonCode.SUCCESS.code() && CollectionUtils.isNotBlank(resp.getData())) {
            List<RivernetWaterLevelResVo> data = JSONArray.parseArray(JSONArray.toJSONString(resp.getData()), RivernetWaterLevelResVo.class);
            stCodes = data.stream().map(RivernetWaterLevelResVo::getMainStCode).collect(Collectors.joining(com.ygkj.utils.StringUtils.JOIN_DELIMITER));
            list = new ArrayList<>(data.size());
            for (RivernetWaterLevelResVo temp : data) {
                Double waterLevel = temp.getWaterLevel();
                BigDecimal warningLevel = temp.getWarningLevel();
                BigDecimal guaranteeLevel = temp.getGuranteeLevel();
                int status = 0;
                if (warningLevel != null && waterLevel != null && warningLevel.doubleValue() <= waterLevel) {
                    status = 1;
                }
                if (guaranteeLevel != null && waterLevel != null && guaranteeLevel.doubleValue() <= waterLevel) {
                    status = 2;
                }
                JSONObject tempRes = new JSONObject();
                tempRes.put("waterLevel", waterLevel);
                tempRes.put("warningLevel", warningLevel);
                tempRes.put("guaranteeLevel", guaranteeLevel);
                tempRes.put("status", status);
                tempRes.put("rnName", temp.getRnName());
                list.add(tempRes);
            }
        }
        JSONObject result = new JSONObject();
        result.put("list", list);
        result.put("stCodes", stCodes);
        return result;
    }

    private JSONObject rivernetStorageStats(Date dataTime) {
        List<RivernetWaterLevelResVo> list = Collections.emptyList();
        double avgWarnStorageAbility = 0D;
        double totalWarnStorageSub = 0D;
        long overWarnNum = 0;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        CommonResult<List<LinkedHashMap>> resp = graphClient.listRivernet(Builder.of(RivernetWaterLevelReqVo::new)
                .with(RivernetWaterLevelReqVo::setWaterCapacity, true)
                .with(RivernetWaterLevelReqVo::setWarnStorage, true)
                .with(RivernetWaterLevelReqVo::setDataTime, dataTime == null ? null : sdf.format(dataTime))
                .build());
        String stCodes = StringUtils.EMPTY;
        if (resp.getCode() == CommonCode.SUCCESS.code() && CollectionUtils.isNotBlank(resp.getData())) {
            list = JSONArray.parseArray(JSONArray.toJSONString(resp.getData()), RivernetWaterLevelResVo.class);
            stCodes = list.stream().map(RivernetWaterLevelResVo::getMainStCode).collect(Collectors.joining(com.ygkj.utils.StringUtils.JOIN_DELIMITER));
            avgWarnStorageAbility = list.stream().filter(o -> o.getWarnStorageAbility() != null).mapToDouble(RivernetWaterLevelResVo::getWarnStorageAbility).average().getAsDouble();
            totalWarnStorageSub = list.stream().filter(o -> o.getWarnStorageSub() != null && o.getWarnStorageSub() > 0).mapToDouble(RivernetWaterLevelResVo::getWarnStorageSub).sum();
            overWarnNum = list.stream().filter(o -> o.getWaterLevel() != null && o.getWarningLevel() != null && o.getWaterLevel() > o.getWarningLevel().doubleValue()).count();
        }
        JSONObject result = new JSONObject();
        result.put("list", list);
        result.put("avgWarnStorageAbility", String.format("%.1f", avgWarnStorageAbility));
        result.put("totalWarnStorageSub", String.format("%.1f", totalWarnStorageSub));
        result.put("overWarnNum", overWarnNum);
        // 未来六小时降雨量
        result.put("forecastNext6hRainfall", forecastNext6hRainfall());
        // 距离最近的大中型水库
        result.put("nearestLargeMiddleRsvr", nearestLargeMiddleRsvr(totalWarnStorageSub, dataTime));
        result.put("stCodes", stCodes);
        return result;
    }

    public Object forecastNext6hRainfall() {
        CommonResult result = (CommonResult) shortForecastManager.getData(Builder.of(UnitDataReqVo::new)
                .with(UnitDataReqVo::setStrategy, "predictRainfallByAreaName")
                .with(UnitDataReqVo::putParam, "areaName", null)
                .build());
        if (result.getData() != null) {
            return ((LinkedHashMap) result.getData()).get("rainfall_6");
        }
        return null;
    }

    public Object nearestLargeMiddleRsvr(double totalWarnStorageSub, Date dataTime) {
        JSONObject temp = (JSONObject) rsvrManager.getData(Builder.of(UnitDataReqVo::new)
                .with(UnitDataReqVo::setStrategy, "rsvrProjStats2")
                .with(UnitDataReqVo::putParam, "scaleType", "大中型")
                .with(UnitDataReqVo::putParam, "dataTime", dataTime)
                .build());

        List<RsvrStationWaterResVo> storageList = temp.getJSONArray("storageList").toJavaList(RsvrStationWaterResVo.class);
        Optional<Map.Entry<Double, RsvrStationWaterResVo>> optional = storageList.stream()
                .filter(o -> o.getWaterLevelCapacity() != null)
                .collect(Collectors.toMap(o -> Math.abs(totalWarnStorageSub - o.getWaterLevelCapacity()), Function.identity()))
                .entrySet().stream()
                .max(Map.Entry.comparingByKey());
        return optional.<Object>map(Map.Entry::getValue).orElse(null);

    }

    /**
     * 河网列表
     *
     * @return
     */
    public JSONObject riverNetworkList(RivNetListQueryReqVo reqVo) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        CommonResult<List<LinkedHashMap>> resp = graphClient.listRivernet(Builder.of(RivernetWaterLevelReqVo::new)
                .with(RivernetWaterLevelReqVo::setDataTime, reqVo.getDataTime() == null ? null : sdf.format(reqVo.getDataTime()))
                .with(RivernetWaterLevelReqVo::setBas, reqVo.getBas())
                .with(RivernetWaterLevelReqVo::setAdcd, reqVo.getAdcd())
                .build());
        List<RivernetWaterLevelResVo> data = Collections.emptyList();
        if (resp.getCode() == CommonCode.SUCCESS.code() && CollectionUtils.isNotBlank(resp.getData())) {
            data = JSONArray.parseArray(JSONArray.toJSONString(resp.getData()), RivernetWaterLevelResVo.class);
        }
        List<JSONObject> list = new ArrayList<>(data.size());
        int warn = 0;
        Map<String, String> adcdMap = selectAdnmByAdcd(data.stream().filter(o -> StringUtils.isNotBlank(o.getAreaCode())).map(AttRnBase::getAreaCode).collect(Collectors.toList()));
        for (RivernetWaterLevelResVo temp : data) {
            String areaName = adcdMap.get(temp.getAreaCode());
            Double waterLevel = temp.getWaterLevel();
            BigDecimal warningLevel = temp.getWarningLevel();
            BigDecimal guaranteeLevel = temp.getGuranteeLevel();
            String overWarn = null;
            String overGuarantee = null;
            if (warningLevel != null && waterLevel != null && warningLevel.doubleValue() < waterLevel) {
                overWarn = String.format("%.2f", waterLevel - warningLevel.doubleValue());
                warn++;
            }
            if (guaranteeLevel != null && waterLevel != null && guaranteeLevel.doubleValue() < waterLevel) {
                overGuarantee = String.format("%.2f", waterLevel - guaranteeLevel.doubleValue());
            }
            JSONObject tempRes = JSONObject.parseObject(JSONObject.toJSONString(temp, SerializerFeature.WriteMapNullValue));
            tempRes.put("waterLevel", waterLevel == null ? null : String.format("%.2f", waterLevel));
            tempRes.put("overWarn", overWarn);
            tempRes.put("overGuarantee", overGuarantee);
            tempRes.put("areaName", areaName);
            tempRes.put("longitude", temp.getStLong());
            tempRes.put("latitude", temp.getStLat());
            list.add(tempRes);
        }
        if (StringUtils.isNotBlank(reqVo.getOrderColumn())) {
            Comparator<JSONObject> comparator = new MapComparator<>(reqVo.getOrderColumn());
            if (reqVo.getAscOrDesc() != null && reqVo.getAscOrDesc()) {
                comparator = comparator.reversed();
            }
            list = list.stream().sorted(comparator).collect(Collectors.toList());
        }
        JSONObject result = new JSONObject();
        result.put("list", list);
        result.put("total", data.size());
        result.put("warn", warn);
        return result;
    }

    public CommonResult rivernetReplayStats(RivernetReplayReqVo reqVo) {
        return graphClient.rivernetReplayStats(reqVo);
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

    private Object allRivernetRealtimeLevel() {
        return graphClient.listRivernet(Builder.of(RivernetWaterLevelReqVo::new)
                .build()).getData();
    }

}
