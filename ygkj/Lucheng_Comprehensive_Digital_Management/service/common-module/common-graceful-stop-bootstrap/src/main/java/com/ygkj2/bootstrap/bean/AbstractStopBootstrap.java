package com.ygkj.bootstrap.bean;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;

import javax.annotation.PreDestroy;

public abstract class AbstractStopBootstrap implements ApplicationListener<ApplicationReadyEvent>, InitializingBean {

    protected ConfigurableApplicationContext applicationContext;

    @PreDestroy
    public void shutdown() {
        if (checkBeforeShutDown()) {
            applicationContext.close();
        }
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {
        this.applicationContext = applicationReadyEvent.getApplicationContext();
    }

    public abstract boolean checkBeforeShutDown();

}
