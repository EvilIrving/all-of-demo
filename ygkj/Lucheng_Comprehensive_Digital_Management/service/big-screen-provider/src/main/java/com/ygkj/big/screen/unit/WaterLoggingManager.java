package com.ygkj.big.screen.unit;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.ygkj.big.screen.feign.FloodProtectClient;
import com.ygkj.big.screen.vo.request.FloodPointReqVo;
import com.ygkj.big.screen.vo.response.ChartResVo;
import com.ygkj.big.screen.vo.request.UnitDataBuilderReqVo;
import com.ygkj.big.screen.vo.request.UnitDataReqVo;
import com.ygkj.utils.Builder;
import com.ygkj.utils.CollectionUtils;
import com.ygkj.water.model.CommonResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;

/**
 * @author xq
 * @Description
 * @Date 2021/7/12
 */
@Component
@Slf4j
public class WaterLoggingManager extends AbstractUnitManager {

    @Autowired
    private FloodProtectClient floodProtectClient;

    private ConcurrentHashMap<String, Object> cache;

    public WaterLoggingManager(@Autowired ApplicationContext context) {
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
            case "waterLoggingList": {
                return waterLoggingList2(reqVo.getParam("reqVo", FloodPointReqVo.class));
            }
            case "waterLoggingStats": {
                Object result = null;
                Integer statsType = reqVo.getParam("statsType", Integer.class);
                if ((result = cache.get(buildDefaultCacheKey("waterLoggingStats", statsType))) == null) {
                    result = waterLoggingStats(statsType);
                }
                return result;
            }
            case "floodPointCWindows": {
                Object result = null;
                Date dataTime = reqVo.getParam("dataTime", Date.class);
                if ((result = cache.get(buildDefaultCacheKey("floodPointCWindows", dataTime))) == null) {
                    result = floodPointCWindows(dataTime);
                }
                return result;
            }
            default:
                return null;
        }
    }

    @Override
    public void buildData(UnitDataBuilderReqVo reqVo) {
        log.info("提交了一项积水缓存任务");
        Integer statsType = 1;
        cache.put(buildDefaultCacheKey("waterLoggingStats", statsType), waterLoggingStats(statsType));
        statsType = 2;
        cache.put(buildDefaultCacheKey("waterLoggingStats", statsType), waterLoggingStats(statsType));
        statsType = 3;
        cache.put(buildDefaultCacheKey("waterLoggingStats", statsType), waterLoggingStats(statsType));
        Date dataTime = null;
        cache.put(buildDefaultCacheKey("floodPointCWindows", dataTime), floodPointCWindows(dataTime));
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

    public CommonResult floodPointCWindows(Date dataTime) {
        return floodProtectClient.floodPointCWindows(dataTime);
    }

    public CommonResult waterLoggingStats(Integer statsType) {
        switch (statsType) {
            case 1:
                return floodProtectClient.floodPointStatistic(1);
            case 2: {
                int total = 0;
                int hasWater = 0;
                int noWater = 0;
                CommonResult resp = floodProtectClient.floodPointStatistic(3);
                if (resp.getData() != null) {
                    List<ChartResVo> data = JSONArray.parseArray(JSONObject.toJSONString(resp.getData(), SerializerFeature.WriteMapNullValue), ChartResVo.class);
                    for (ChartResVo temp : data) {
                        if ("有积水".equals(temp.getKey())) {
                            hasWater = Integer.parseInt(temp.getValue().toString());
                            total += hasWater;
                        } else if ("无积水".equals(temp.getKey())) {
                            noWater = Integer.parseInt(temp.getValue().toString());
                            total += noWater;
                        }
                    }
                }
                JSONObject result = new JSONObject(true);
                result.put("total", total);
                result.put("hasWater", hasWater);
                result.put("noWater", noWater);
                return CommonResult.success(result);
            }
            case 3: {
                int num = 0;
                CommonResult resp = floodProtectClient.floodPointStatistic(3);
                if (resp.getData() != null) {
                    List<ChartResVo> data = JSONArray.parseArray(JSONObject.toJSONString(resp.getData(), SerializerFeature.WriteMapNullValue), ChartResVo.class);
                    for (ChartResVo temp : data) {
                        if ("有积水".equals(temp.getKey())) {
                            num = Integer.parseInt(temp.getValue().toString());
                            break;
                        }
                    }
                }
                return CommonResult.success(num);
            }
            default:
                return CommonResult.success();
        }
    }

    public CommonResult waterLoggingList(FloodPointReqVo reqVo) {
        return floodProtectClient.listFloodPoint(reqVo);
    }

    public CommonResult waterLoggingList2(FloodPointReqVo reqVo) {
        if (reqVo != null) {
            reqVo.setRecentDays(3);
        }
        CommonResult<List<LinkedHashMap>> resp = floodProtectClient.listFloodPoint(reqVo);
        List<LinkedHashMap> list = Collections.emptyList();
        int hasWater = 0;
        int noWater = 0;
        if (CollectionUtils.isNotBlank(resp.getData())) {
            list = resp.getData();
            for (LinkedHashMap data : list) {
                Object resultPondingObj = data.get("ponding");
                if (resultPondingObj != null) {
                    Boolean resultPonding = (Boolean) resultPondingObj;
                    if (resultPonding) {
                        hasWater++;
                    } else {
                        noWater++;
                    }
                }
            }
        }
        JSONObject data = new JSONObject();
        data.put("list", list);
        data.put("total", list.size());
        data.put("hasWater", hasWater);
        data.put("noWater", noWater);
        return CommonResult.success(data);
    }
}
