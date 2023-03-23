package com.ygkj.big.screen.unit;

import com.ygkj.big.screen.feign.MeteorologicVisualizationClient;
import com.ygkj.big.screen.vo.request.UnitDataBuilderReqVo;
import com.ygkj.big.screen.vo.request.UnitDataReqVo;
import com.ygkj.water.model.CommonResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;

/**
 * @author xq
 * @Description
 * @Date 2021/7/20
 */
@Component
@Slf4j
public class ShortForecastManager extends AbstractUnitManager {

    @Autowired
    private MeteorologicVisualizationClient mvClient;

    private ConcurrentHashMap<String, Object> cache;

    public ShortForecastManager(@Autowired ApplicationContext context) {
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
            case "predictRainfallByAreaName": {
                String areaName = reqVo.getParam("areaName", String.class);
                Object result = null;
                if ((result = cache.get(buildDefaultCacheKey("predictRainfallByAreaName", areaName))) == null) {
                    result = predictRainfallByAreaName(areaName);
                }
                return result;
            }
            case "rainfallMaxValuePredict": {
                Integer hour = reqVo.getParam("hour", Integer.class);
                Object result = null;
                if ((result = cache.get(buildDefaultCacheKey("rainfallMaxValuePredict", hour))) == null) {
                    result = rainfallMaxValuePredict(hour);
                }
                return result;
            }
            case "rainfallPredictGrid": {
                String type = reqVo.getParam("type", String.class);
                Object result = null;
                if ((result = cache.get(buildDefaultCacheKey("rainfallPredictGrid", type))) == null) {
                    result = rainfallPredictGrid(type);
                }
                return result;
            }
            default:
                return null;
        }
    }


    @Override
    public void buildData(UnitDataBuilderReqVo reqVo) {
        log.info("提交了一项短临预报缓存任务");
        String areaName = StringUtils.EMPTY;
        cache.put(buildDefaultCacheKey("predictRainfallByAreaName"), predictRainfallByAreaName(areaName));
        Integer[] hours = {1, 3, 6};
        for (Integer hour : hours) {
            cache.put(buildDefaultCacheKey("rainfallMaxValuePredict", hour), rainfallMaxValuePredict(hour));
        }
        String[] types = {"行政区划", "流域"};
        for (String type : types) {
            cache.put(buildDefaultCacheKey("rainfallPredictGrid", type), rainfallPredictGrid(type));
        }

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

    public CommonResult predictRainfallByAreaName(String areaName) {
        return mvClient.predictRainfallByAreaName(StringUtils.isEmpty(areaName) ? "" : areaName);
    }

    public CommonResult rainfallMaxValuePredict(Integer hour) {
        return mvClient.rainfallMaxValuePredict(hour);
    }

    public CommonResult rainfallPredictGrid(String type) {
        return mvClient.rainfallPredictGrid(type);
    }
}
