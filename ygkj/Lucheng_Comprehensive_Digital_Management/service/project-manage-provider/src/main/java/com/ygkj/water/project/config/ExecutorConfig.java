package com.ygkj.water.project.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

@Configuration
public class ExecutorConfig {

    @Bean("myExecutor")
    public AsyncTaskExecutor asyncExecutor() {
        ThreadPoolTaskExecutor asyncTaskExecutor = new ThreadPoolTaskExecutor();
        asyncTaskExecutor.setMaxPoolSize(32);
        asyncTaskExecutor.setCorePoolSize(8);
        asyncTaskExecutor.setThreadNamePrefix("task-executor-name");
        asyncTaskExecutor.setQueueCapacity(100);
        asyncTaskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        asyncTaskExecutor.initialize();
        return asyncTaskExecutor;
    }
}
