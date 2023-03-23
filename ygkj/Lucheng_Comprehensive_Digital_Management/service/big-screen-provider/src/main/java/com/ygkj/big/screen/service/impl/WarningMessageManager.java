package com.ygkj.big.screen.service.impl;

import com.ygkj.big.screen.enums.MessageTypes;
import com.ygkj.big.screen.strategy.AbstractHeavyRainStrategy;
import com.ygkj.big.screen.strategy.MountainFloodWarningStrategy;
import com.ygkj.big.screen.strategy.RsvrOverLimitWarningStrategy;
import com.ygkj.big.screen.strategy.WarningStrategy;
import com.ygkj.big.screen.model.MessageRecord;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class WarningMessageManager implements InitializingBean {

    private static Map<MessageTypes, WarningStrategy> RULES;

    public List<MessageRecord> submit(String type, Collection<?> collection) {
        WarningStrategy warningStrategy = RULES.get(MessageTypes.candidateType(type));
        if (warningStrategy != null) {
            return warningStrategy.handle(collection);
        }
        return Collections.emptyList();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        RULES = new HashMap<>();
        RULES.put(MessageTypes.MOUNTAIN_FLOOD, new MountainFloodWarningStrategy());
        RULES.put(MessageTypes.HEAVY_RAIN, new AbstractHeavyRainStrategy());
        RULES.put(MessageTypes.WATER_LEVEL, new RsvrOverLimitWarningStrategy());
    }
}
