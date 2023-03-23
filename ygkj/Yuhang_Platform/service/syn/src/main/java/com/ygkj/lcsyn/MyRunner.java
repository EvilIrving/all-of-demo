package com.ygkj.lcsyn;

import com.ygkj.lcsyn.task.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class MyRunner implements CommandLineRunner {

    @Resource
    private WaterAndRainTask waterAndRainTask;

    @Resource
    private TableTask tableTask;

    @Override
    public void run(String... args) throws Exception {
        tableTask.addCol();
    }
}
