package com.ygkj.soft.database.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Huang.zh
 * @date 2020/9/1 15:24
 * @Description: 线程工厂，负责执行任务
 */
//@Component
@Slf4j
public class DataReceiveThreadFactory implements InitializingBean {


    private ThreadPoolExecutor executor;

    @Autowired(required = false)
    private DataReceiveThreadProduceFactory dataReceiveThreadProduceFactory;

    @Override
    public void afterPropertiesSet() throws Exception {
        initializeExecutor();
        log.info("-----数据接收线程工厂初始化完毕-----");
    }

    private void initializeExecutor(){
        Assert.notNull(dataReceiveThreadProduceFactory,"dataReceiveThreadProduceFactory不能为空！！！");
        executor = new ThreadPoolExecutor(Runtime.getRuntime().availableProcessors()*2,20,60, TimeUnit.SECONDS
                ,new ArrayBlockingQueue<>(10),dataReceiveThreadProduceFactory);
    }


    /**
     * 自定义线程生产工厂
     */
    private class DataReceiveThreadProduceFactory implements ThreadFactory {

        private AtomicInteger index = new AtomicInteger(1);

        @Override
        public Thread newThread(Runnable r) {
            Thread thread = new Thread(r, Constants.DATA_RECEIVE_THREAD_PREFIX + index.get());
            index = new AtomicInteger(index.addAndGet(1));
            return thread;
        }
    }

    @Bean
    @Lazy
    public DataReceiveThreadProduceFactory dataReceiveThreadProduceFactory(){
        return new DataReceiveThreadProduceFactory();
    }

    private static class Constants{
        private static final String DATA_RECEIVE_THREAD_PREFIX = "data_receive_thread_";
    }

    /**
     * @Author Huang.zh
     * @Description 将任务提交给线程池并执行，并指定返回类型
     * @Date 2020/9/2 9:24
     */
    public Future<Integer>  submit(Callable task){
        Future<Integer> submitResult = executor.submit(task);
        return submitResult;
    }

}
