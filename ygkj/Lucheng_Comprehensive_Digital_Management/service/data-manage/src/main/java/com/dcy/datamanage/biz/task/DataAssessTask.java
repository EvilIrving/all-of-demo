package com.dcy.datamanage.biz.task;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.dcy.datamanage.api.model.DataAssessExtendMsg;
import com.dcy.datamanage.api.model.DataAssessFrequency;
import com.dcy.datamanage.api.model.DataAssessResult;
import com.dcy.datamanage.biz.service.DataAssessExtendMsgService;
import com.dcy.datamanage.biz.service.DataAssessFrequencyService;
import com.dcy.datamanage.biz.service.DataAssessResultService;
import com.dcy.datamanage.biz.util.DateUtil;
import com.dcy.datamanage.biz.util.SnowFlake;
import org.springframework.aop.interceptor.AsyncExecutionAspectSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class DataAssessTask {

    @Autowired
    private DataAssessFrequencyService dataAssessFrequencyService;

    @Autowired
    private DataAssessExtendMsgService dataAssessExtendMsgService;

    @Autowired
    private DataAssessResultService dataAssessResultService;

    /**
     * 留一个手动的入口
     *
     * @param frequencyUnit
     */
    public void calculateScore(int frequencyUnit) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime endTime = now.withHour(0).withMinute(0).withSecond(0).withNano(0);
        LocalDateTime startTime;
        if (frequencyUnit == 1) {
            startTime = endTime.minusDays(1);
        } else if (frequencyUnit == 2) {
            endTime = endTime.withDayOfMonth(1);
            startTime = endTime.minusMonths(1);
        } else if (frequencyUnit == 3) {
            int monthValue = now.getMonthValue();
            int endMonth = (monthValue - 1) / 3 * 3 + 1;
            endTime = endTime.withMonth(endMonth).withDayOfMonth(1);
            startTime = endTime.minusMonths(3);
        } else {
            endTime = endTime.withDayOfYear(1);
            startTime = endTime.minusYears(1);
        }
        calculateScore(startTime, endTime, frequencyUnit);
    }

    @Scheduled(cron = "0 0 1 1/1 * ?")
    @Async(AsyncExecutionAspectSupport.DEFAULT_TASK_EXECUTOR_BEAN_NAME)
    public void day() {
        calculateScore(1, ChronoUnit.DAYS, 1);
    }

    @Scheduled(cron = "0 0 1 1 1/1 ?")
    @Async(AsyncExecutionAspectSupport.DEFAULT_TASK_EXECUTOR_BEAN_NAME)
    public void month() {
        calculateScore(1, ChronoUnit.MONTHS, 2);
    }

    @Scheduled(cron = "0 0 1 1 1/3 ?")
    @Async(AsyncExecutionAspectSupport.DEFAULT_TASK_EXECUTOR_BEAN_NAME)
    public void quarter() {
        calculateScore(3, ChronoUnit.MONTHS, 3);
    }

    @Scheduled(cron = "0 0 1 1 1 ?")
    @Async(AsyncExecutionAspectSupport.DEFAULT_TASK_EXECUTOR_BEAN_NAME)
    public void year() {
        calculateScore(1, ChronoUnit.YEARS, 4);
    }

    private void calculateScore(long amountToSubtract, TemporalUnit unit, int frequencyUnit) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime endTime = now.withHour(0).withMinute(0).withSecond(0).withNano(0);
        LocalDateTime startTime = endTime.minus(amountToSubtract, unit);
        calculateScore(startTime, endTime, frequencyUnit);
    }

    private void calculateScore(LocalDateTime startTime, LocalDateTime endTime, int frequencyUnit) {
        LambdaQueryWrapper<DataAssessFrequency> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DataAssessFrequency::getFrequencyUnit, frequencyUnit);
        List<DataAssessFrequency> list = dataAssessFrequencyService.list(queryWrapper);
        calculateScore(startTime, endTime, list);
    }

    private void calculateScore(LocalDateTime startLocalTime, LocalDateTime endLocalTime, List<DataAssessFrequency> list) {
        if (!CollectionUtils.isEmpty(list)) {
            Date startDate = DateUtil.timeToDate(startLocalTime);
            Date endDate = DateUtil.timeToDate(endLocalTime);
            //按数据源分组
            Map<String, List<String>> tableNameListMap = list.stream().collect(Collectors.groupingBy(DataAssessFrequency::getDataSourceId,
                    Collectors.mapping(DataAssessFrequency::getTableName, Collectors.toList())));
            List<DataAssessResult> resultList = new ArrayList<>();
            tableNameListMap.forEach((dataSourceId, tableNameList) -> {
                //每张表最新一次的更新时间
                Map<String, DataAssessExtendMsg> timeMap = dataAssessExtendMsgService.listLastUpdateTime(dataSourceId, tableNameList);
                tableNameList.forEach(tableName -> {
                    Date date = timeMap.containsKey(tableName) ? timeMap.get(tableName).getUpdateTime() : null;
                    //没更新,更新时间早于上个周期开始时间，更新时间晚于上个周期结束时间
                    boolean timeOut = date == null || date.before(startDate) || date.after(endDate) || date.equals(endDate);
                    if (timeOut) {
                        resultList.add(initResult(dataSourceId, tableName, 0));
                    } else {
                        resultList.add(initResult(dataSourceId, tableName, 100));
                    }
                });
            });

            //保存分数
            dataAssessResultService.replaceAll(resultList);
        }
    }

    private DataAssessResult initResult(String dataSourceId, String tableName, int score) {
        DataAssessResult dataAssessResult = new DataAssessResult();
        dataAssessResult.setScore(score);
        dataAssessResult.setId(SnowFlake.zero.nextId());
        dataAssessResult.setTableName(tableName);
        dataAssessResult.setDataSourceId(dataSourceId);
        return dataAssessResult;
    }
}
