package com.ygkj.lcsyn;

import com.ygkj.lcsyn.task.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class MyRunner implements CommandLineRunner {

    @Resource
    private ReportFloodTask reportFloodTask;

//    @Resource
//    private PatrolTask patrolTask;
//
//    @Resource
//    private WaterAndRainTask waterAndRainTask;
//
//    @Resource
//    private BhDutyTask bhDutyTask;

    @Override
    public void run(String... args) throws Exception {
        reportFloodTask.run();
    }
}
