package com.ygkj.big.screen.drill;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author xq
 * @Description
 * @Date 2021/7/10
 */
public abstract class AbstractDrillManagerStrategy implements DrillManagerStrategy, InitializingBean, DisposableBean {


    ExecutorService executorService;

    @Override
    public void afterPropertiesSet() throws Exception {
        executorService = new ThreadPoolExecutor(Runtime.getRuntime().availableProcessors(), Runtime.getRuntime().availableProcessors() * 4, 60, TimeUnit.MINUTES, new ArrayBlockingQueue<>(10));
        doSubscribe();
    }


    @Override
    public void destroy() throws Exception {
        shutdown();
        executorService.shutdown();
    }

    public ExecutorService getExecutorService() {
        return executorService;
    }
}
