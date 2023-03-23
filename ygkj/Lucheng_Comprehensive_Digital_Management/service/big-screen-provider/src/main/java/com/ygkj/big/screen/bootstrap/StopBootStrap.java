package com.ygkj.big.screen.bootstrap;

import com.ygkj.big.screen.service.impl.RainfallSurfaceDataManager;
import com.ygkj.bootstrap.bean.AbstractStopBootstrap;
import org.springframework.stereotype.Component;

@Component
public class StopBootStrap extends AbstractStopBootstrap {
    @Override
    public boolean checkBeforeShutDown() {
        return RainfallSurfaceDataManager.terminate();
    }

    @Override
    public void afterPropertiesSet() throws Exception {

    }
}
