package com.ygkj.water.project.task;

import com.ygkj.utils.DateUtils;
import com.ygkj.water.project.enums.CameraWarnRuleEnum;
import com.ygkj.water.project.service.CameraOpsService;
import org.springframework.util.Assert;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 * @author xq
 * @Description
 * @Date 2021/9/10
 */
public class CameraWarnNotifyTask implements Delayed, Runnable {

    Date warnSetTime;

    Date next;

    CameraWarnRuleEnum warnRuleEnum;

    String cameraIndexCode;

    CameraOpsService cameraOpsService;

    DelayQueue<CameraWarnNotifyTask> delayQueue;

    public CameraWarnNotifyTask(String cameraIndexCode, Date warnSetTime, CameraWarnRuleEnum warnRuleEnum,
                                CameraOpsService cameraOpsService, DelayQueue<CameraWarnNotifyTask> delayQueue) {
        this.cameraIndexCode = cameraIndexCode;
//        this.callback = callback;
        updateTask(warnSetTime, warnRuleEnum);
        Assert.isTrue(next != null, "构建任务失败");
        this.cameraOpsService = cameraOpsService;
        this.delayQueue = delayQueue;
    }

    @Override
    public long getDelay(TimeUnit unit) {
        return unit.convert(next.getTime() - System.currentTimeMillis(), TimeUnit.MILLISECONDS);
    }


    @Override
    public int compareTo(Delayed o) {
        return (int) (this.getDelay(TimeUnit.MILLISECONDS) - o.getDelay(TimeUnit.MILLISECONDS));
    }

    @Override
    public void run() {
        try {
            cameraOpsService.sendMsgV2(cameraIndexCode, false);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            this.buildNext(LocalDateTime.now());
            //提交到队列中继续执行
            delayQueue.offer(this);
        }

    }

    private void buildNext(LocalDateTime pre) {
        LocalDateTime initLdt = DateUtils.date2LocalDateTime(warnSetTime);
        switch (warnRuleEnum) {
            case HOURLY: {
                LocalDateTime end = pre.withMinute(initLdt.getMinute()).withSecond(initLdt.getSecond()).withNano(initLdt.getNano());
                while (end.isBefore(pre)) {
                    end = end.plusHours(1);
                    // 5分钟测试用
//                    end = end.plusMinutes(5);
                }
                next = DateUtils.localDateTime2Date(end);
            }
            break;
            case DAILY: {
                LocalDateTime end = pre.withHour(initLdt.getHour()).withMinute(initLdt.getMinute()).withSecond(initLdt.getSecond()).withNano(initLdt.getNano());
                while (end.isBefore(pre)) {
                    end = end.plusDays(1);
                }
                next = DateUtils.localDateTime2Date(end);
            }
            break;
            case WEEKLY: {
                DayOfWeek dayOfWeek = initLdt.getDayOfWeek();
                while (!pre.getDayOfWeek().equals(dayOfWeek)) {
                    pre = pre.plusDays(1);
                }
                LocalDateTime end = pre.withHour(initLdt.getHour()).withMinute(initLdt.getMinute()).withSecond(initLdt.getSecond()).withNano(initLdt.getNano());
                while (end.isBefore(pre)) {
                    end = end.plusWeeks(1);
                }
                next = DateUtils.localDateTime2Date(end);
            }
            break;
            case MONTHLY: {
                LocalDateTime end = pre.withDayOfMonth(initLdt.getDayOfMonth()).withHour(initLdt.getHour()).withMinute(initLdt.getMinute()).withSecond(initLdt.getSecond()).withNano(initLdt.getNano());
                while (end.isBefore(pre)) {
                    end = end.plusMonths(1);
                }
                next = DateUtils.localDateTime2Date(end);
            }
            break;
        }
    }

    public void updateTask(Date warnSetTime, CameraWarnRuleEnum warnRuleEnum) {
        this.warnSetTime = warnSetTime;
        this.warnRuleEnum = warnRuleEnum;
        buildNext(LocalDateTime.now());
    }

    public Date getWarnSetTime() {
        return warnSetTime;
    }

    public Date getNext() {
        return next;
    }

    public CameraWarnRuleEnum getWarnRuleEnum() {
        return warnRuleEnum;
    }

    public String getCameraIndexCode() {
        return cameraIndexCode;
    }

    //    public static void main(String[] args) {
//        LocalDate localDate1= LocalDate.of(2021,9,11);
//        LocalDate localDate2= LocalDate.of(2021,9,4);
//        LocalDate localDate3= LocalDate.of(2021,9,1);
//        System.out.println(localDate1.getDayOfWeek().getValue());
//        System.out.println(localDate2.getDayOfWeek().equals(localDate1.getDayOfWeek()));
//        System.out.println(Math.abs(localDate3.getDayOfWeek().getValue()-localDate1.getDayOfWeek().getValue()));
//        System.out.println(localDate1.plus(Math.abs(localDate3.getDayOfWeek().getValue()-localDate1.getDayOfWeek().getValue()), ChronoUnit.DAYS));
//    }
}
