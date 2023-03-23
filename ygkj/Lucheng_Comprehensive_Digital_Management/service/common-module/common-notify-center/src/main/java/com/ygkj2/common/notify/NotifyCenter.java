package com.ygkj.common.notify;

import com.ygkj.common.notify.events.Event;
import com.ygkj.common.notify.functions.BuildFunction;
import com.ygkj.common.notify.publishers.CommonEventPublisher;
import com.ygkj.common.notify.publishers.Publisher;
import com.ygkj.common.notify.subscribers.Subscriber;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author Huang.zh
 * @date 2021/4/9 18:04
 * @Description: 事件通知中心
 */
public class NotifyCenter {

    private static ConcurrentHashMap<Class<? extends Event>, Publisher> publishers;

    private static CopyOnWriteArrayList<BuildFunction<Class<? extends Event>, Publisher>> eventPublisherFactories;

    private static ConcurrentHashMap<Class<? extends Event>, BuildFunction<Class<? extends Event>, Publisher>> eventPublisherFactoryMap;

    private static NotifyCenter INSTANCE;

    static {
        //懒汉模式
        if (null == INSTANCE) {
            synchronized (NotifyCenter.class) {
                //双重检查，避免线程安全问题
                if (null == INSTANCE) {
                    INSTANCE = new NotifyCenter();
                }
            }
        }
    }

    public static NotifyCenter instance() {
        return INSTANCE;
    }

    /**
     * @Author Huang.zh
     * @Description 寻找合适的推送者，如果继承重写了publisher，就可以覆盖当前默认的CommonEventPublisher
     * @Date 2021/4/10 12:43
     */
    private static Collection<Class<? extends Publisher>> findCandidatePublisher() {
        List<Class<? extends Publisher>> classList = new ArrayList<>();
        Class<? extends Publisher> clazz;
        final ServiceLoader<Publisher> loader = ServiceLoader.load(Publisher.class);
        Iterator<Publisher> iterator = loader.iterator();
        classList.add(CommonEventPublisher.class);
        while (iterator.hasNext()) {
            clazz = iterator.next().getClass();
            classList.add(clazz);
        }
//        if (iterator.hasNext()) {
//            clazz = iterator.next().getClass();
//            classList.add(clazz);
//        } else {
//            clazz = CommonEventPublisher.class;
//        }
        return classList;
    }

    private NotifyCenter() {
        publishers = new ConcurrentHashMap<>(1024);
        eventPublisherFactories = new CopyOnWriteArrayList<>();
        eventPublisherFactoryMap = new ConcurrentHashMap<>(256);
        Collection<Class<? extends Publisher>> collection = findCandidatePublisher();
        collection.forEach(publisherClass -> {
            try {
                Publisher publisher = publisherClass.newInstance();
                BuildFunction<Class<? extends Event>, Publisher> function = new BuildFunction<Class<? extends Event>, Publisher>() {
                    @Override
                    public Publisher apply(Class<? extends Event> aClass) {
                        try {
                            Class<? extends Event> eventType = publisher.eventType();
                            publisher.init(eventType);
                            return publisher;
                        } catch (Throwable ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                };
                eventPublisherFactories.add(function);
                eventPublisherFactoryMap.put(publisher.eventType(), function);
            } catch (Throwable ex) {
                throw new RuntimeException(ex);
            }
        });
//        eventPublisherFactory = clazz -> {
//            try {
//                Publisher publisher = collection.newInstance();
//                publisher.init(clazz);
//                return publisher;
//            } catch (Throwable ex) {
//                throw new RuntimeException(ex);
//            }
//        };
    }

    public static void registerSubscriber(final Subscriber subscriber) {
        //订阅者根据事件类型进行订阅
        //如果存在就取出推送者，将订阅者委托给推送者维护
        //如果不存在就直接为当前订阅者创建推送者，提供消费动机
        Class<? extends Event> type = subscriber.subscribeType();
        if (INSTANCE.publishers.containsKey(type)) {
            Publisher publisher = INSTANCE.publishers.get(type);
            publisher.addSubscriber(subscriber);
        } else {
            BuildFunction<Class<? extends Event>, Publisher> eventPublisherFactory = eventPublisherFactoryMap.get(type);
            if (eventPublisherFactory == null) {
                eventPublisherFactory = new BuildFunction<Class<? extends Event>, Publisher>() {
                    @Override
                    public Publisher apply(Class<? extends Event> aClass) {
                        CommonEventPublisher publisher = new CommonEventPublisher(type);
                        publisher.init(type);
                        return publisher;
                    }
                };
                eventPublisherFactoryMap.put(type, eventPublisherFactory);
            }
            Publisher publisher = eventPublisherFactory.apply(type);
            publisher.addSubscriber(subscriber);
            INSTANCE.publishers.putIfAbsent(type, publisher);
        }
    }

    public static boolean deregisterSubscriber(final Subscriber subscriber) {
        Class<? extends Event> type = subscriber.subscribeType();
        return deregisterSubscriber(type);
    }

    public static boolean deregisterSubscriber(final Class<? extends Event> type) {
        if (!INSTANCE.publishers.containsKey(type)) {
            return true;
        }
        return INSTANCE.publishers.remove(type) != null;
    }

    public static boolean publishEvent(final Event event) {
        try {
            return publishEvent(event.getClass(), event);
        } catch (Throwable ex) {
            throw new RuntimeException(ex);
        }
    }

    private static boolean publishEvent(final Class<? extends Event> eventType, final Event event) {
        Publisher publisher;
        if (!INSTANCE.publishers.containsKey(eventType)) {
            BuildFunction<Class<? extends Event>, Publisher> eventPublisherFactory = eventPublisherFactoryMap.get(eventType);
            publisher = eventPublisherFactory.apply(eventType);
        } else {
            publisher = publishers.get(eventType);
        }
        if (publisher != null) {
            return publisher.publish(event);
        }
        return false;
    }


}
