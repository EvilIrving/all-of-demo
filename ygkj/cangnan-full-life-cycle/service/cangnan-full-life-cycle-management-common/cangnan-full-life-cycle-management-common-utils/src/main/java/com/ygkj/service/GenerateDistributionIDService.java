package com.ygkj.service;

import com.ygkj.entity.SnowFlakeConfig;
import com.ygkj.utils.SnowFlake;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

@Slf4j
public class GenerateDistributionIDService implements InitializingBean {

    private SnowFlake snowFlake;

    public GenerateDistributionIDService(@Autowired SnowFlakeConfig snowFlakeConfig) {
        Assert.notNull(snowFlakeConfig,"请检查分布式ID生成配置类，不允许为空!");
        this.snowFlake = new SnowFlake(snowFlakeConfig.getDataCenterId(),snowFlakeConfig.getMachineId());
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        log.info("---------完成分布式ID生成工具类初始化---------");
    }

    public String nextID(){
        return String.valueOf(this.snowFlake.nextId());
    }
}
