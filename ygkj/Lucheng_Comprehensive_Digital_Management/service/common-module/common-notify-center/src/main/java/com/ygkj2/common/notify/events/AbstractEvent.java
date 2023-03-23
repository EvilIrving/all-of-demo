package com.ygkj.common.notify.events;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author Huang.zh
 * @date 2021/4/9 18:20
 * @Description: 事件抽象类
 */
public abstract class AbstractEvent implements Event {

    private static final AtomicLong SEQUENCE = new AtomicLong(0);

    private final long sequence = SEQUENCE.getAndIncrement();


    public Long sequence() {
        return sequence;
    }
}
