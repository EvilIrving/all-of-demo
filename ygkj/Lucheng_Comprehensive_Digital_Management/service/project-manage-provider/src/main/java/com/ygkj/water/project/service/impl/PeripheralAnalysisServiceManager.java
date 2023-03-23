package com.ygkj.water.project.service.impl;

import com.ygkj.water.project.strategy.Strategy;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Component
public class PeripheralAnalysisServiceManager implements ApplicationContextAware {

    private ConfigurableApplicationContext configurableApplicationContext;

    private static Map<String, Strategy> rules;

    private static final Integer DEFAULT_INITIAL_SIZE = 1 << 4;

    @PostConstruct
    public void init() {
        Map<String, Strategy> map = configurableApplicationContext.getBeansOfType(Strategy.class);
        if (!map.isEmpty()) {
            rules = new HashMap<>(map.size() <= DEFAULT_INITIAL_SIZE ? DEFAULT_INITIAL_SIZE : map.size() << 2);
            map.forEach((beanName, candidateStrategy) -> rules.put(candidateStrategy.type(), candidateStrategy));
        }
        map = null;//help GC.
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.configurableApplicationContext = (ConfigurableApplicationContext) applicationContext;
    }

    public static Collection candidateRecords(String codes, String type) {
        if (rules.containsKey(type)) {
            Strategy strategy = rules.get(type);
            return strategy.records(codes);
        }
        return Collections.EMPTY_LIST;
    }
}
