package com.ygkj;

import com.ygkj.task.SynWaterManagerTask;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

//@Component
public class MyRunner implements CommandLineRunner {

    @Resource
    private SynWaterManagerTask synWaterManagerTask;

    @Override
    public void run(String... args) throws Exception {
        synWaterManagerTask.synWaterManagerHandler();
    }
}
