package com.ygkj.water.project.task;

import com.ygkj.project.vo.request.HikCameraQueryReqVo;
import com.ygkj.project.vo.response.HikCameraResVo;
import com.ygkj.utils.Builder;
import com.ygkj.utils.CollectionUtils;
import com.ygkj.water.project.enums.CameraWarnRuleEnum;
import com.ygkj.water.project.mapper.HikCameraMapper;
import com.ygkj.water.project.service.CameraOpsService;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

/**
 * @author xq
 * @Description
 * @Date 2021/9/10
 */
@Component
public class CameraWarnNotifyManager implements ApplicationContextAware, ApplicationListener<ContextRefreshedEvent> {

    Map<String, CameraWarnNotifyTask> taskMap;

    DelayQueue<CameraWarnNotifyTask> queue;

    ExecutorService bossExecutor;

    ExecutorService workerExecutor;

    ApplicationContext applicationContext;

    HikCameraMapper hikCameraMapper;

    CameraOpsService cameraOpsService;

    public CameraWarnNotifyManager() {
        bossExecutor = Executors.newSingleThreadExecutor();
        workerExecutor = new ThreadPoolExecutor(16, 128, 1, TimeUnit.HOURS, new ArrayBlockingQueue<>(1024));
        taskMap = new ConcurrentHashMap<>(4096);
        queue = new DelayQueue<>();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
        this.hikCameraMapper = applicationContext.getBean(HikCameraMapper.class);
        this.cameraOpsService = applicationContext.getBean(CameraOpsService.class);
    }

    public void updateTask(String cameraIndexCode, Date warnSetTime, CameraWarnRuleEnum warnRuleEnum) {
        CameraWarnNotifyTask task = getTask(cameraIndexCode);
        if (task == null) {
            task = new CameraWarnNotifyTask(cameraIndexCode, warnSetTime, warnRuleEnum, cameraOpsService, queue);
            queue.offer(task);
            taskMap.put(cameraIndexCode, task);
        } else {
            task.updateTask(warnSetTime, warnRuleEnum);
        }
    }

    public void deleteTask(String cameraIndexCode) {
        CameraWarnNotifyTask task = getTask(cameraIndexCode);
        if (task != null) {
            queue.remove(task);
            taskMap.remove(cameraIndexCode);
        }
    }

    public CameraWarnNotifyTask getTask(String cameraIndexCode) {
        return taskMap.get(cameraIndexCode);
    }

    public void refresh() {
        queue.clear();
        taskMap.clear();
        List<HikCameraResVo> cameraResVos = hikCameraMapper.selectCameraInfoByCondition(Builder.of(HikCameraQueryReqVo::new)
                .with(HikCameraQueryReqVo::setWithOpsInfo, true)
                .with(HikCameraQueryReqVo::setWithProjInfo, false)
                .build());
        if (CollectionUtils.isNotBlank(cameraResVos)) {
            for (HikCameraResVo cameraResVo : cameraResVos) {
                CameraWarnRuleEnum ruleEnum = CameraWarnRuleEnum.candidateByRuleCode(cameraResVo.getWarnNotifyRule());
                if (ruleEnum == null || cameraResVo.getWarnSetTime() == null) {
                    continue;
                }
//                CameraWarnNotifyTask task = new CameraWarnNotifyTask(cameraResVo.getCameraIndexCode(), cameraResVo.getWarnSetTime(), ruleEnum, cameraOpsService, queue);
//                queue.offer(task);
//                taskMap.put(cameraResVo.getCameraIndexCode(), task);
                updateTask(cameraResVo.getCameraIndexCode(), cameraResVo.getWarnSetTime(), ruleEnum);
            }
        }
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (event.getApplicationContext() == this.applicationContext) {
            refresh();
            Runnable run = () -> {
                while (true) {
                    try {
                        CameraWarnNotifyTask task = queue.take();
                        workerExecutor.execute(task);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            };
            bossExecutor.execute(run);
        }
    }

    @Scheduled(cron = "0 0 9 * * ?")
    public void refreshTask() {
        refresh();
    }

    /**
     * 推断主类
     *
     * @return
     */
    private Class<?> deduceMainApplicationClass() {
        try {
            StackTraceElement[] stackTrace = new RuntimeException().getStackTrace();
            for (StackTraceElement stackTraceElement : stackTrace) {
                if ("main".equals(stackTraceElement.getMethodName())) {
                    return Class.forName(stackTraceElement.getClassName());
                }
            }
        } catch (ClassNotFoundException ex) {
            // Swallow and continue
        }
        return null;
    }
}
