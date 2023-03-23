package com.ygkj.common.notify.publishers;

import com.ygkj.common.notify.events.Event;
import com.ygkj.common.notify.events.SmsSendEvent;
import com.ygkj.common.notify.subscribers.Subscriber;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executor;

/**
 * @author Huang.zh
 * @date 2021/4/10 11:43
 * @Description: 通用事件推送者
 */
public class CommonEventPublisher extends Thread implements Publisher {

    private BlockingQueue<Event> queue;

    protected final CopyOnWriteArrayList<Subscriber> subscribers = new CopyOnWriteArrayList<>();

    private Class<? extends Event> eventType;

    public CommonEventPublisher() {
    }

    public CommonEventPublisher(Class<? extends Event> eventType) {
        this.eventType = eventType;
    }

    @Override
    public void init(Class<? extends Event> clazz) {
        eventType = clazz;
        queue = new ArrayBlockingQueue<>(32);
        super.start();
    }

    @Override
    public void run() {
        openEventHandler();
    }

    @Override
    public Class<? extends Event> eventType() {
        return eventType == null ? SmsSendEvent.class : eventType;
    }

    @Override
    public void addSubscriber(Subscriber subscriber) {
        subscribers.add(subscriber);
    }

    @Override
    public void removeSubscriber(Subscriber subscriber) {
        subscribers.remove(subscriber);
    }

    @Override
    public boolean publish(Event event) {
        if (this.queue.contains(event)) {
            //防止重复提交
            return true;
        }
        boolean success = this.queue.offer(event);
        if (!success) {
            //这个策略是必要的，一旦事件推送不成功，需要手动唤醒所有订阅者消费这个事件
            receiveEvent(event);
            return true;
        }
        return true;
    }

    @Override
    public void notifySubscriber(Subscriber subscriber, Event event) {
        final Runnable job = () -> subscriber.onEvent(event);

        final Executor executor = subscriber.executor();

        if (executor != null) {
            //如果订阅者有线程池执行调度任务，那就委托给订阅者
            executor.execute(job);
        } else {
            //否则手动执行
            try {
                job.run();
            } catch (Throwable ex) {
                ex.printStackTrace();
                throw new RuntimeException(ex);
            }
        }
    }

    void receiveEvent(Event event) {
        //接收一个事件，依次唤醒所有的订阅者
        for (Subscriber subscriber : subscribers) {
            notifySubscriber(subscriber, event);
        }
    }

    void openEventHandler() {
        for (; ; ) {
            try {
                //监听当前类型的事件，自旋+阻塞
                final Event event = queue.take();
                receiveEvent(event);
            } catch (Throwable ex) {
                ex.printStackTrace();
            }
        }
//        try {
//
//        } catch (Throwable ex) {
//            //如果发生异常，则再尝试启动自己监听事件
//            super.interrupt();
//            if (super.isInterrupted()){
//                this.queue.clear();
//                super.start();
//            }
//        }
    }
}
