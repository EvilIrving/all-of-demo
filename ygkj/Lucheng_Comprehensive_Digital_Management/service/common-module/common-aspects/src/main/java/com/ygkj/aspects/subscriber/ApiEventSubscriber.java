package com.ygkj.aspects.subscriber;

import com.alibaba.fastjson.JSON;
import com.ygkj.aspects.event.SysLogApiPersistenceEvent;
import com.ygkj.aspects.mapper.ApiLogMapper;
import com.ygkj.common.notify.NotifyCenter;
import com.ygkj.common.notify.events.Event;
import com.ygkj.common.notify.subscribers.Subscriber;
import com.ygkj.platform.management.model.SysApiLog;
import com.ygkj.platform.management.model.SysApiParamLog;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class ApiEventSubscriber extends Subscriber<SysLogApiPersistenceEvent> implements InitializingBean, ApplicationContextAware {

    private ApiLogMapper apiLogMapper;

    private ConfigurableApplicationContext applicationContext;

    private Set<String> ignoreParams = new HashSet<>();

    @Override
    public void onEvent(SysLogApiPersistenceEvent event) {
        synchronized (apiLogMapper) {
            SysApiLog sysApiLog = (SysApiLog) event.data();
            apiLogMapper.insertApiLog(sysApiLog);
            long logId = sysApiLog.getId();
            System.out.println(logId);
            String parameters = sysApiLog.getParameters();
            if (!StringUtils.isBlank(parameters)) {
                //保存入参
                Set<Map.Entry<String, Object>> entries = JSON.parseObject(parameters).entrySet();
                if (!entries.isEmpty()) {
                    entries.forEach(entry -> {
                        String parameterValue = entry.getValue().toString();
                        if (parameterValue.startsWith("{") && entries.size() == 1) {
                            //内部还是一个json对象
                            Set<Map.Entry<String, Object>> set = JSON.parseObject(parameterValue).entrySet();
                            for (Map.Entry<String, Object> objectEntry : set) {
                                String key = objectEntry.getKey();
                                if (ignoreParams.contains(key)) {
                                    //过滤分页字段
                                    continue;
                                }
                                SysApiParamLog sysApiParamLog = new SysApiParamLog(logId, key, objectEntry.getValue().toString(), sysApiLog.getUserId());
                                apiLogMapper.insertApiParameterLog(sysApiParamLog);
                            }
                        } else {
                            SysApiParamLog sysApiParamLog = new SysApiParamLog(logId, entry.getKey(), parameterValue, sysApiLog.getUserId());
                            apiLogMapper.insertApiParameterLog(sysApiParamLog);
                        }
                    });
                }
            }
        }
    }

    @Override
    public Class<? extends Event> subscribeType() {
        return SysLogApiPersistenceEvent.class;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        SqlSessionTemplate template = applicationContext.getBean(SqlSessionTemplate.class);
        SqlSessionFactory factory = template.getSqlSessionFactory();
        Configuration configuration = factory.getConfiguration();
        configuration.addMapper(ApiLogMapper.class);
        apiLogMapper = factory.openSession().getMapper(ApiLogMapper.class);
        ignoreParams.addAll(Arrays.asList("pageNum", "pageSize", "paged"));
        NotifyCenter.registerSubscriber(this);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = (ConfigurableApplicationContext) applicationContext;
    }
}
