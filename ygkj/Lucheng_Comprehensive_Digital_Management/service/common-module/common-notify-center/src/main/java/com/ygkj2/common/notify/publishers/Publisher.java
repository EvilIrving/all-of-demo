package com.ygkj.common.notify.publishers;

import com.ygkj.common.notify.events.Event;
import com.ygkj.common.notify.subscribers.Subscriber;

/**
 * @author Huang.zh
 * @date 2021/4/10 11:49
 * @Description: 事件推送者抽象
 */
public interface Publisher {

    void init(Class<? extends Event> clazz);

    void addSubscriber(Subscriber subscriber);

    void removeSubscriber(Subscriber subscriber);

    boolean publish(Event event);

    void notifySubscriber(Subscriber subscriber, Event event);

    Class<? extends Event> eventType();
}
