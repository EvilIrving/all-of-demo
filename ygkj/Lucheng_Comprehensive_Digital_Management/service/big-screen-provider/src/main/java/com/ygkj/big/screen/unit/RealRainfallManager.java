package com.ygkj.big.screen.unit;

import com.ygkj.big.screen.feign.GraphClient;
import com.ygkj.big.screen.vo.request.SeawallWarnStatisticReqVo;
import com.ygkj.big.screen.vo.request.SynRainRequestVo;
import com.ygkj.big.screen.vo.request.UnitDataBuilderReqVo;
import com.ygkj.big.screen.vo.request.UnitDataReqVo;
import com.ygkj.water.model.CommonResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;

/**
 * @author xq
 * @Description
 * @Date 2021/7/19
 */
@Component
@Slf4j
public class RealRainfallManager extends AbstractUnitManager {

    @Autowired
    GraphClient graphClient;

    private ConcurrentHashMap<String, Object> cache;

    public RealRainfallManager(@Autowired ApplicationContext context) {
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
            case "grandTotalMaxRainfallStatistic": {
                String endTime = reqVo.getParam("endTime", String.class);
                Integer hour = reqVo.getParam("hour", Integer.class);
                Object result = null;
                if ((result = cache.get(buildDefaultCacheKey("grandTotalMaxRainfallStatistic", endTime, hour))) == null) {
                    result = grandTotalMaxRainfallStatistic(endTime, hour);
                }
                return result;
            }
            case "grandTotalReservoirRainfall": {
                String endTime = reqVo.getParam("endTime", String.class);
                Integer hour = reqVo.getParam("hour", Integer.class);
                Object result = null;
                if ((result = cache.get(buildDefaultCacheKey("grandTotalReservoirRainfall", endTime, hour))) == null) {
                    result = grandTotalReservoirRainfall(endTime, hour);
                }
                return result;
            }
            default:
                return null;
        }
    }

    @Override
    public void buildData(UnitDataBuilderReqVo reqVo) {
        log.info("提交了一项实时降雨缓存任务");
        int[] hours = {0, 1, 3, 6, 24, 72};
        String endTime = null;
        for (int hour : hours) {
            cache.put(buildDefaultCacheKey("grandTotalMaxRainfallStatistic", endTime, hour), grandTotalMaxRainfallStatistic(endTime, hour));
            cache.put(buildDefaultCacheKey("grandTotalReservoirRainfall", endTime, hour), grandTotalReservoirRainfall(endTime, hour));
        }
    }

    @Override
    public ExecutorService getExecutor() {
        return getDefaultConfigExecutor();
    }

    /**
     * 每5分钟提交一次任务
     */
    @Scheduled(cron = "0 0/3 * * * ? ")
    public void updateCacheTask() {
        doBuild(null);
    }


    public CommonResult grandTotalMaxRainfallStatistic(String endTime, Integer hour) {
        return graphClient.grandTotalMaxRainfallStatistic(endTime, hour);
    }

    public CommonResult grandTotalReservoirRainfall(String endTime, Integer hour) {
        return graphClient.grandTotalReservoirRainfall(endTime, hour);
    }

//    public  CommonResult grandTotalRainfallGrid(SynRainRequestVo requestVo, String endTime, String type, String surfaceType, Integer hour) {
//        CommonResult resp= graphClient.grandTotalRainfallGrid(requestVo, endTime, type, surfaceType, hour);
//        Long h1Beyond30=null;
//        Long h3Beyond60=null;
//        JSONObject result=new JSONObject();
//        result.put("list",resp.getData());
//        if (resp.getData()!=null&&(hour==1||hour==3)){
//            List<JSONObject> list= JSONArray.parseArray(JSONObject.toJSONString(resp.getData(), SerializerFeature.WriteMapNullValue),JSONObject.class);
//            if (hour==1) {
//                h1Beyond30= list.stream().filter(o -> o.getDouble("1hour")!=null).mapToDouble(o -> o.getDouble("1hour")).filter(o->o>30).count();
//            }else {
//                h3Beyond60= list.stream().filter(o -> o.getDouble("3hour")!=null).mapToDouble(o -> o.getDouble("3hour")).filter(o->o>60).count();
//            }
//        }
//        result.put("1hourBeyond30", h1Beyond30);
//        result.put("3hourBeyond60", h3Beyond60);
//        return CommonResult.success(result);
//    }
}
