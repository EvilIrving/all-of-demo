package com.ygkj.big.screen.unit;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ygkj.big.screen.feign.MeteorologicVisualizationClient;
import com.ygkj.big.screen.mapper.AttAdcdBaseMapper;
import com.ygkj.big.screen.mapper.BusinessMapper;
import com.ygkj.big.screen.model.AttAdcdBase;
import com.ygkj.big.screen.vo.request.UnitDataBuilderReqVo;
import com.ygkj.big.screen.vo.request.UnitDataReqVo;
import com.ygkj.utils.CollectionUtils;
import com.ygkj.utils.HttpClientUtil;
import com.ygkj.visualization.model.StMeWarnR;
import com.ygkj.water.model.CommonResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author xq
 * @Description
 * @Date 2021/7/20
 */
@Component
@Slf4j
public class WeatherManager extends AbstractUnitManager {

    @Autowired
    private MeteorologicVisualizationClient mvClient;

    @Autowired
    private AttAdcdBaseMapper adcdBaseMapper;

    @Autowired
    private BusinessMapper businessMapper;

    private ConcurrentHashMap<String, Object> cache;

    ExecutorService executor = Executors.newSingleThreadExecutor();

    public WeatherManager(@Autowired ApplicationContext context) {
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
            case "weatherPredict": {
                String areaName = reqVo.getParam("areaName", String.class);
                Object result = null;
                if ((result = cache.get(buildDefaultCacheKey("weatherPredict", areaName))) == null) {
                    result = weatherPredict(areaName);
                }
                return result;
            }
            case "wholeWeather": {
                Object result = null;
                if ((result = cache.get(buildDefaultCacheKey("wholeWeather"))) == null) {
                    result = wholeWeather();
                }
                return result;
            }
            case "weatherWarning": {
                Object result = null;
                if ((result = cache.get(buildDefaultCacheKey("weatherWarning"))) == null) {
                    result = weatherWarning();
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
        log.info("提交了一项天气实况缓存任务");
        List<AttAdcdBase> adcdRecords = this.adcdBaseMapper.queryAllAdOfWz();
        for (AttAdcdBase record : adcdRecords) {
            String areaName = record.getAdnm();
            cache.put(buildDefaultCacheKey("weatherPredict", areaName), weatherPredict(areaName));
        }
        cache.put(buildDefaultCacheKey("weatherWarning"), weatherWarning());
        cache.put(buildDefaultCacheKey("wholeWeather"), wholeWeather());
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

    public CommonResult weatherPredict(String areaName) {
        return mvClient.weatherPredict(areaName);
    }

    public CommonResult wholeWeather() {
        return mvClient.wholeWeather();
    }

    public JSONArray weatherWarning() {
        JSONArray array = JSON.parseArray(HttpClientUtil.httpGet("http://www.wz121.com/map/getWeatherWarn"));
        executor.execute(new persistenceWarningTask(array));
        JSONArray jsonArray = new JSONArray();
        if (!CollectionUtils.isBlank(array)) {
            for (int i = 0; i < array.size(); i++) {
                JSONObject jsonObject = array.getJSONObject(i);
                String adcd = jsonObject.getString("AREA_CODE");
                if (set.contains(adcd)) {
                    jsonArray.add(jsonObject);
                }
            }
        }
        return jsonArray;
    }

    static Set<String> set = new HashSet<String>() {{
        add("3303");
        add("330301");
        add("330302");
        add("330300");
    }};

    private class persistenceWarningTask implements Runnable {

        JSONArray array;

        public persistenceWarningTask(JSONArray array) {
            this.array = array;
        }

        @Override
        public void run() {
            if (array != null && !array.isEmpty()) {
                log.info("------------即将持久化{}项气象预警数据------------");
                List<StMeWarnR> insertList = new ArrayList<>(array.size());
                try {
                    for (Object o : array) {
                        JSONObject jsonObject = (JSONObject) o;
                        StMeWarnR stMeWarnR = new StMeWarnR(jsonObject.getString("WARN_CODE"), jsonObject.getString("AREA_CODE"),
                                jsonObject.getString("LON"), jsonObject.getString("LAT"), jsonObject.getString("PUBLISH_UNIT"),
                                jsonObject.getString("WARN_AREA"), jsonObject.getString("WARN_CONTENT"),
                                jsonObject.getString("WARN_LEVEL"), jsonObject.getString("WARN_MEASURE"),
                                jsonObject.getString("WARN_PERIOD"), jsonObject.getString("WARN_TYPE"),
                                jsonObject.getDate("publishDate"), jsonObject.getString("title"));
                        insertList.add(stMeWarnR);
                    }
                    businessMapper.batchInsertMeteorologicalWarningData(insertList);
                    log.info("------------持久化气象预警数据完毕！------------");
                } catch (Exception e) {
                    e.printStackTrace();
                    log.error("------------持久化气象预警数据发生异常！------------");
                    log.error(e.getMessage());
                }
            }
        }
    }
}
