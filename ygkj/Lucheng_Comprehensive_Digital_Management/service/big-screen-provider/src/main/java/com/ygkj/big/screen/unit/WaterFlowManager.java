package com.ygkj.big.screen.unit;

import com.alibaba.fastjson.JSONObject;
import com.ygkj.big.screen.feign.HydrologyDigitizationClient;
import com.ygkj.big.screen.vo.request.UnitDataBuilderReqVo;
import com.ygkj.big.screen.vo.request.UnitDataReqVo;
import com.ygkj.digitization.vo.request.MrwlReqVo;
import com.ygkj.utils.CollectionUtils;
import com.ygkj.water.model.CommonResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;

/**
 * @author xq
 * @Description
 * @Date 2021/7/12
 */
@Component
@Slf4j
public class WaterFlowManager extends AbstractUnitManager {

    @Autowired
    HydrologyDigitizationClient hydrologyDigitizationClient;

    private ConcurrentHashMap<String, Object> cache;

    public WaterFlowManager(@Autowired ApplicationContext context) {
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
            case "waterFlowList": {
                Object result = cache.get("waterFlowList");
                if (result != null) {
                    return result;
                }
                return waterFlowList();

            }
            case "waterFlowStats": {
                Object result = cache.get("waterFlowStats");
                if (result != null) {
                    return result;
                }
                return computeWaterFlowStats();
            }
            default:
                return null;
        }

    }

    @Override
    public void buildData(UnitDataBuilderReqVo reqVo) {
        log.info("提交了一项流量组件缓存任务");
        cache.put("waterFlowStats", computeWaterFlowStats());
        cache.put("waterFlowList", waterFlowList());
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

    String flowKey = "flow";

    public CommonResult waterFlowList() {
        CommonResult<List<LinkedHashMap>> resp = hydrologyDigitizationClient.mountainRiverWaterLevel(new MrwlReqVo());
        if (resp != null && CollectionUtils.isNotBlank(resp.getData())) {
            List<LinkedHashMap> data = resp.getData();
            // 从小到大排序
            data.sort((o1, o2) -> {
                try {
                    if (o1.get(flowKey) != null && o2.get(flowKey) != null) {
                        return new BigDecimal(String.valueOf(o2.get(flowKey))).compareTo(new BigDecimal(String.valueOf(o1.get(flowKey))));
                    } else if (o1.get(flowKey) == null & o2.get(flowKey) != null) {
                        return 1;
                    } else if (o1.get(flowKey) != null & o2.get(flowKey) == null) {
                        return -1;
                    } else {
                        return 0;
                    }
                } catch (Exception e) {
                    return 0;
                }
            });
        }
        return resp;
    }

    public CommonResult computeWaterFlowStats() {
        LinkedHashMap max = null;
        int overWarnNum = 0;
        CommonResult<List<LinkedHashMap>> resp = hydrologyDigitizationClient.mountainRiverWaterLevel(new MrwlReqVo());
        if (CollectionUtils.isNotBlank(resp.getData())) {
            List<LinkedHashMap> data = resp.getData();
            Optional<LinkedHashMap> maxOpt = data.stream().filter(o -> o.get("flow") != null).max(Comparator.comparing(o -> {
                Object flow = o.get("flow");
                return Double.parseDouble(flow.toString());
            }));
            if (maxOpt.isPresent()) {
                max = maxOpt.get();
            }
        }
        JSONObject result = new JSONObject();
        result.put("max", max);
        result.put("overWarnNum", overWarnNum);
        result.put("overWarnStation", Collections.emptyList());
        return CommonResult.success(result);
    }
}
