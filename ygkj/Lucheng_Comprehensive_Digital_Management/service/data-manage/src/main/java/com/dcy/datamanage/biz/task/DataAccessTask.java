//package com.dcy.datamanage.biz.task;
//
//import com.dcy.datamanage.api.model.DataAssessFrequency;
//import com.dcy.datamanage.biz.service.DataAssessFrequencyService;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.scheduling.TaskScheduler;
//import org.springframework.scheduling.Trigger;
//import org.springframework.scheduling.TriggerContext;
//import org.springframework.scheduling.annotation.SchedulingConfigurer;
//import org.springframework.scheduling.config.ScheduledTaskRegistrar;
//import org.springframework.scheduling.support.CronTrigger;
//import org.springframework.stereotype.Component;
//import org.springframework.util.CollectionUtils;
//
//import java.util.Date;
//import java.util.List;
//import java.util.Map;
//import java.util.concurrent.ConcurrentHashMap;
//import java.util.concurrent.Executors;
//import java.util.concurrent.ScheduledExecutorService;
//import java.util.concurrent.ScheduledFuture;
//
//@Component
//public class DataAccessTask implements SchedulingConfigurer {
//
//    private TaskScheduler taskScheduler;
//    private Map<String, ScheduledFuture<?>> scheduledFutureMap = new ConcurrentHashMap<>();
//
//    @Autowired
//    private DataAssessFrequencyService dataAssessFrequencyService;
//
//    @Override
//    public void configureTasks(ScheduledTaskRegistrar scheduledTaskRegistrar) {
//        //创建一个线程池调度器
//        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(8);
//
////        this.taskScheduler = scheduler;
//        //初始化已有的任务
//        List<DataAssessFrequency> list = dataAssessFrequencyService.list();
//        if (!CollectionUtils.isEmpty(list)) {
//            list.forEach(dataAssessFrequency -> add(dataAssessFrequency));
//        }
//    }
//
//    /**
//     * 新增调度任务
//     *
//     * @param dataAssessFrequency
//     */
//    public void add(DataAssessFrequency dataAssessFrequency) {
//        Task task = new Task(dataAssessFrequency.getDataSourceId(), dataAssessFrequency.getTableName());
//        Trig trig = new Trig("dataAssessFrequency.getCron()");
//        ScheduledFuture<?> schedule = taskScheduler.schedule(task, trig);
//        scheduledFutureMap.put(dataAssessFrequency.getId(), schedule);
//    }
//
//    /**
//     * 清除调度任务
//     *
//     * @param frequencyId
//     */
//    public void remove(String frequencyId) {
//        ScheduledFuture<?> scheduledFuture = scheduledFutureMap.get(frequencyId);
//        if (scheduledFuture != null) {
//            scheduledFuture.cancel(true);
//        }
//        scheduledFutureMap.remove(frequencyId);
//    }
//
//}
//
//
///**
// * 业务类
// */
//@Slf4j
//class Task implements Runnable {
//    private String dataSourceId;
//    private String tableName;
//
//    public Task(String dataSourceId, String tableName) {
//        this.dataSourceId = dataSourceId;
//        this.tableName = tableName;
//    }
//
//    //具体业务
//    @Override
//    public void run() {
//        log.info("[数据考核分数计算][dataSourceId]{}[tableName]{}", dataSourceId, tableName);
//    }
//}
//
///**
// * 调度类
// */
//class Trig implements Trigger {
//
//    private String cron;
//
//    public Trig(String cron) {
//        this.cron = cron;
//    }
//
//    @Override
//    public Date nextExecutionTime(TriggerContext triggerContext) {
//        CronTrigger cronTrigger = new CronTrigger(cron);
//        return cronTrigger.nextExecutionTime(triggerContext);
//    }
//}