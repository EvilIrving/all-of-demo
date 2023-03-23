package com.ygkj.entity;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "snowflake")
@Slf4j
public class SnowFlakeConfig implements InitializingBean {

    private long dataCenterId;

    private long machineId;

    @Override
    public void afterPropertiesSet() throws Exception {
        log.info(toString());
    }

    @Override
    public String toString() {
        return "SnowFlakeConfig{" +
                "dataCenterId=" + dataCenterId +
                ", machineId=" + machineId +
                '}';
    }

    public long getDataCenterId() {
        return dataCenterId;
    }

    public void setDataCenterId(long dataCenterId) {
        this.dataCenterId = dataCenterId;
    }

    public long getMachineId() {
        return machineId;
    }

    public void setMachineId(long machineId) {
        this.machineId = machineId;
    }
}
