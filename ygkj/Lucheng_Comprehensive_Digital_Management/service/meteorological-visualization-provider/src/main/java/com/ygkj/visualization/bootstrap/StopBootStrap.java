package com.ygkj.visualization.bootstrap;

import com.ygkj.bootstrap.bean.AbstractStopBootstrap;
import com.ygkj.visualization.service.impl.WeatherServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class StopBootStrap extends AbstractStopBootstrap {
    @Override
    public boolean checkBeforeShutDown() {
        //此处根据不同子系统的业务进行最后的检验，比如优雅停止正在处理任务的线程池等......
        WeatherServiceImpl.terminate();
        log.info("----------已完成检查，系统即将停止运行----------");
        return true;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        //此处可以再做一些子系统的定制操作
        log.info("------------停止脚本："+this.getClass().getName()+"完成初始化------------");
    }
}
