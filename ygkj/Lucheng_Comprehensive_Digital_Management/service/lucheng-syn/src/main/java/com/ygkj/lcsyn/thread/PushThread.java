package com.ygkj.lcsyn.thread;

import com.ygkj.lcsyn.task.PushRainAndWaterTask;
import lombok.extern.log4j.Log4j2;

import java.util.Date;

/**
 * @author lxl
 * @create 2022-07-15 16:25
 * @description
 */
@Log4j2
public class PushThread implements Runnable{

    private Date nearlyDate;
    private PushRainAndWaterTask pushRainAndWaterTask;
    private String type;


    public PushThread(Date nearlyDate,PushRainAndWaterTask pushRainAndWaterTask,String type) {
        this.nearlyDate = nearlyDate;
        this.pushRainAndWaterTask = pushRainAndWaterTask;
        this.type = type;
    }

    @Override
    public void run() {
        log.info(" ---- 推送线程执行  时间：{}",nearlyDate);

        if ("PP".equals(type)){
            pushRainAndWaterTask.pushNearlyPpData(nearlyDate);
        }else {
            pushRainAndWaterTask.pushNearlyRsvrData(nearlyDate);
        }

    }
}
