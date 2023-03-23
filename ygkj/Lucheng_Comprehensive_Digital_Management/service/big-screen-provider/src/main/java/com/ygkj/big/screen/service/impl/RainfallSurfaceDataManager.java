package com.ygkj.big.screen.service.impl;

import com.ygkj.big.screen.feign.GraphClient;
import com.ygkj.big.screen.unit.notify.event.UnitCacheEvent;
import com.ygkj.big.screen.vo.request.UnitDataBuilderReqVo;
import com.ygkj.common.notify.NotifyCenter;
import com.ygkj.common.notify.events.Event;
import com.ygkj.common.notify.subscribers.Subscriber;
import com.ygkj.gragh.rpc.GraphRpcService;
import com.ygkj.water.model.CommonResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.Method;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.*;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

@Component
@Slf4j
//@RunWith(SpringRunner.class)
//@SpringBootTest(classes = BigScreenApplication.class)
//@ActiveProfiles("dev")
public class RainfallSurfaceDataManager {

    private static final Set<String> RULES = new HashSet<>();

    static {
        RULES.add("温州市");
//        RULES.add("鹿城区");
//        RULES.add("瓯海区");
//        RULES.add("龙湾区");
//        RULES.add("洞头区");
//        RULES.add("瑞安市");
//        RULES.add("乐清市");
//        RULES.add("龙港市");
//        RULES.add("泰顺县");
//        RULES.add("文成县");
//        RULES.add("永嘉县");
//        RULES.add("平阳县");
//        RULES.add("苍南县");
    }

    @Autowired
    private GraphClient graphClient;

    @Reference(interfaceClass = GraphRpcService.class, retries = 0, methods = {
            @Method(name = "rainfallStatistic", timeout = 300000, retries = 0)
    })
    private GraphRpcService graphRpcService;

    private static ConcurrentHashMap<String, CommonResult> dataMap;

    /**
     * 用于失败补偿
     */
    private static ConcurrentHashMap<String, Integer> compensateMap;

    private static ThreadPoolExecutor executor;

    private ReentrantLock lock;

    public RainfallSurfaceDataManager(@Autowired GraphClient graphClient) {
        this.graphClient = graphClient;
        int size = RULES.size();
        dataMap = new ConcurrentHashMap<>(size);
        compensateMap = new ConcurrentHashMap<>(size);
        lock = new ReentrantLock(Boolean.FALSE);
        executor = new ThreadPoolExecutor(Runtime.getRuntime().availableProcessors() * 2, 30, 60, TimeUnit.SECONDS
                , new ArrayBlockingQueue<>(12), (r, executor) -> {
            if (r instanceof DoGetSurfaceData) {
                DoGetSurfaceData data = (DoGetSurfaceData) r;
                String areaName = data.getAreaName();
                compensateMap.put(areaName, compensateMap.get(areaName) + 1);
                log.info("----------当前线程池繁忙，已进入补偿集合，{}数据对应补偿次数为：{} ----------", areaName, compensateMap.get(areaName));
            }
        });
        // 订阅立即缓存事件
        NotifyCenter.registerSubscriber(new Subscriber() {
            @Override
            public void onEvent(Event event) {
                execute();
            }

            @Override
            public Class<? extends Event> subscribeType() {
                return UnitCacheEvent.class;
            }

            @Override
            public Executor executor() {
                return executor;
            }
        });
    }

    public static CommonResult getRainfallSurfaceData(String areaName) {
        areaName = RULES.contains(areaName) ? areaName : "温州市";
        return dataMap.getOrDefault(StringUtils.isEmpty(areaName) ? "温州市" : areaName, CommonResult.failed("行政区划不正确！"));
    }


    @Scheduled(cron = "0 0/3 * * * ? ")
    public void execute() {
        if (compensateMap.isEmpty()) {
            log.info("------------------当前无失败任务，提交新的任务同步------------------");
            for (String rule : RULES) {
                executor.submit(new DoGetSurfaceData(rule));
            }
        } else {
            log.info("------------------当前存在失败任务，将优先使用补偿机制------------------");
            List<String> list = compensateMap.keySet().stream().collect(Collectors.toList());
            for (String areaName : list) {
                //先补偿
                executor.submit(new DoGetSurfaceData(areaName));
            }
            for (String rule : RULES) {
                //再增量
                if (!list.contains(rule)) {
                    executor.submit(new DoGetSurfaceData(rule));
                }
            }
        }
    }

    private class DoGetSurfaceData implements Runnable {

        private String areaName;

        public DoGetSurfaceData(String areaName) {
            this.areaName = areaName;
        }

        public String getAreaName() {
            return areaName;
        }

        @Override
        public void run() {
//            CommonResult commonResult = graphClient.rainfallStatistic(areaName, StringUtils.EMPTY);
            CommonResult commonResult = CommonResult.success(graphRpcService.rainfallStatistic(areaName, StringUtils.EMPTY));
            long code = commonResult.getCode();
            log.info("---------- {}获取面雨量数据，状态响应码：{} ----------", Thread.currentThread().getName(), code);
            dataMap.put(areaName, commonResult);
            if (code != 0l) {
                try {
                    lock.lock();
                    log.error("---------- {}获取面雨量数据失败 ----------", Thread.currentThread().getName());
                    compensateMap.put(areaName, compensateMap.getOrDefault(areaName, 0) - 1);
                    if (compensateMap.get(areaName) <= 0) {
                        compensateMap.remove(areaName);
                    }
                    log.error("----------{}的数据需要补偿的次数为：{} ----------", areaName, compensateMap.get(areaName));
                } catch (Exception e) {
                    log.error(e.getMessage());
                } finally {
                    lock.unlock();
                }
            }
        }
    }

    public static boolean terminate() {
        executor.shutdown();
        compensateMap.clear();
        log.info("----------已清除需要补偿的热键-----------");
        dataMap.clear();
        log.info("----------已清除面雨量数据缓存-----------");
        return executor.isShutdown() && compensateMap.isEmpty() && dataMap.isEmpty();
    }
//    @Autowired
//    private MetaDbMapper metaDbMapper;

//    @Test
//    public void test(){
//        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:00");
//        try {
//            Date end = format.parse("2019-08-13 12:00:00");
//            Calendar calendar =Calendar.getInstance();
//            calendar.setTime(format.parse("2019-08-04 14:00:00"));
//            while (calendar.getTime().before(end)){
//                for (String areaName : RULES) {
//                    String endTime = format.format(calendar.getTime());
//                    CommonResult commonResult = graphClient.rainfallStatistic(areaName, endTime);
//                    JSONObject object = (JSONObject) JSONObject.toJSON(commonResult);
//                    JSONObject params = new JSONObject();
//                    params.put("areaName",areaName);
//                    params.put("endTime",endTime);
//                    metaDbMapper.insert(object.toJSONString(), params.toJSONString(), "/waterAndRain/rainfallStatistic");
//                }
//                calendar.add(Calendar.HOUR_OF_DAY,1);
//            }
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//    }
}
