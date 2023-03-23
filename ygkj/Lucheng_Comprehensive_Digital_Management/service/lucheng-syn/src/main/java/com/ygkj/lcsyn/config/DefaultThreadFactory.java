package com.ygkj.lcsyn.config;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author xq
 * @description
 * @date 2022/4/7
 */
public class DefaultThreadFactory implements ThreadFactory {

    private static final AtomicInteger group = new AtomicInteger(1);

    private final AtomicInteger index = new AtomicInteger(1);

    private final String prefix;

    public DefaultThreadFactory(){
        this("default-"+group.getAndIncrement()+"-pool-");
    }

    public DefaultThreadFactory(String prefix){
        this.prefix=prefix;
    }

    @Override
    public Thread newThread(Runnable r) {
        return new Thread(r, prefix+"-" + index.getAndIncrement());
    }
}
