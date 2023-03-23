package com.ygkj.soft.database.dao;

import com.ygkj.soft.database.api.model.BusApiInvokeLog;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

@Repository
public class BusApiInvokeLogDao {

    @Resource
    private JdbcTemplate jdbcTemplate;

    public void save(BusApiInvokeLog item) {
        String sql = "INSERT INTO `bus_api_invoke_log`" +
                "(`id`, `api_id`, `invoke_ip`, `invoke_ip_remark`, `invoke_satus`, " +
                "`invoke_time`, `invoke_spend`, `data_size`, `invoke_user_id`, `access_key`) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql,item.getId(),item.getApiId(),item.getInvokeIp(),item.getInvokeIpRemark(),item.getInvokeSatus(),
                item.getInvokeTime(),item.getInvokeSpend(),item.getDataSize(),item.getInvokeUserId(),item.getAccessKey());
    }
}
