package com.ygkj.big.screen.unit;

import com.ygkj.big.screen.unit.notify.event.UnitCacheEvent;
import com.ygkj.big.screen.vo.request.UnitDataBuilderReqVo;
import com.ygkj.common.notify.NotifyCenter;
import com.ygkj.common.notify.events.Event;
import com.ygkj.common.notify.subscribers.Subscriber;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.Objects;
import java.util.concurrent.*;

/**
 * @author xq
 * @Description
 * @Date 2021/7/12
 */
public abstract class AbstractUnitManager implements UnitManager, InitializingBean, DisposableBean, ApplicationContextAware {

    ApplicationContext context = null;

    ExecutorService executorService;

    public AbstractUnitManager(ApplicationContext context) {
        Objects.requireNonNull(context);
        this.context = context;
        executorService = getExecutor();
    }

    public AbstractUnitManager() {
        executorService = getExecutor();
    }

    public abstract void onCreate();

    public abstract void onDestroy();

    public void doBuild(UnitDataBuilderReqVo reqVo) {
        // 这个地方一定要try cache 不然会干掉定时任务
        try {
            Runnable job = () -> {
                buildData(reqVo);
                if (reqVo != null && reqVo.isPublishSelf()) {
                    doPublish();
                }
            };
            ExecutorService executorService = getExecutor();
            if (executorService != null) {
                executorService.execute(job);
            } else {
                job.run();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void destroy() throws Exception {
        if (executorService != null) {
            executorService.shutdown();
        }
        onDestroy();

    }


    @Override
    public void afterPropertiesSet() throws Exception {
        onCreate();
        doSubscribe();
        // 订阅立即缓存事件
        NotifyCenter.registerSubscriber(new Subscriber() {
            @Override
            public void onEvent(Event event) {
                doBuild(new UnitDataBuilderReqVo());
            }

            @Override
            public Class<? extends Event> subscribeType() {
                return UnitCacheEvent.class;
            }

            @Override
            public Executor executor() {
                return getExecutor();
            }
        });
    }

    public ExecutorService getExecutor() {
        return null;
    }

    protected ExecutorService getDefaultConfigExecutor() {
        return new ThreadPoolExecutor(2, 30, 5, TimeUnit.SECONDS
                , new ArrayBlockingQueue<>(12), new ThreadPoolExecutor.DiscardOldestPolicy());
    }

    protected String buildDefaultCacheKey(String methodName, Object... params) {
        if (params != null && params.length > 0) {
            String[] keys = new String[params.length + 1];
            keys[0] = methodName;
            for (int i = 0; i < params.length; i++) {
                keys[i + 1] = String.valueOf(params[i]);
            }
            return String.join(com.ygkj.utils.StringUtils.JOIN_DELIMITER, keys);
        } else {
            return methodName;
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }
}
