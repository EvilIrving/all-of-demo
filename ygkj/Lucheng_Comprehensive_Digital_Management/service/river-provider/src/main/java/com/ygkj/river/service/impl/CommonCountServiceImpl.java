package com.ygkj.river.service.impl;

import com.ygkj.enums.AbstractStationType;
import com.ygkj.river.adapters.AreaCountAdapter;
import com.ygkj.river.adapters.BasinCountAdapter;
import com.ygkj.river.adapters.CountAdapter;
import com.ygkj.river.adapters.RiverGradeCountAdapter;
import com.ygkj.river.enums.CountType;
import com.ygkj.river.service.CommonCountService;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("commonCountService")
public class CommonCountServiceImpl implements CommonCountService, InitializingBean, BeanFactoryAware {

    private Map<String, CountAdapter> rules;

    private BeanFactory beanFactory;

    @Override
    public List<Map<String, Integer>> count(String type, String column) {
        CountAdapter countAdapter = rules.get(column);
        if (null != countAdapter) {
            AbstractStationType countType = null;
            switch (type) {
                case "河流":
                    countType = CountType.RIVER;
                    break;
                case "湖泊":
                    countType = CountType.LAKE;
                    break;
                case "河景":
                    countType = CountType.RIVER_SCENE;
                    break;
                default:
                    break;
            }
            if (null != countType) {
                return countAdapter.countNumberByColumn(countType);
            }
        }
        return new ArrayList<>();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.rules = new HashMap<>();
        rules.put("流域", beanFactory.getBean(BasinCountAdapter.class));
        rules.put("行政区划", beanFactory.getBean(AreaCountAdapter.class));
        rules.put("河流等级", beanFactory.getBean(RiverGradeCountAdapter.class));
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }
}
