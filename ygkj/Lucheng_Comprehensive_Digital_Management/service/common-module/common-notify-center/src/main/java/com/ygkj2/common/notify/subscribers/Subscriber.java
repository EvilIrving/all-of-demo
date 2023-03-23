package com.ygkj.common.notify.subscribers;

import com.ygkj.common.notify.events.Event;

import java.util.concurrent.Executor;

/**
 * @author Huang.zh
 * @date 2021/4/10 11:58
 * @Description: 事情订阅者抽象
 */
public abstract class Subscriber<T extends Event> {

    public abstract void onEvent(T event);

    public abstract Class<? extends Event> subscribeType();

    public Executor executor() {
        return null;
    }
}
