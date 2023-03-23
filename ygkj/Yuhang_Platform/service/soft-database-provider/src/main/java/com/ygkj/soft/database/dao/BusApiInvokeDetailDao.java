package com.ygkj.soft.database.dao;

import com.ygkj.soft.database.api.model.BusApiInvokeDetail;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

@Repository
public class BusApiInvokeDetailDao {

    @Resource
    private JdbcTemplate jdbcTemplate;


    public void save(BusApiInvokeDetail item) {
        String sql = "INSERT INTO `bus_api_invoke_detail`" +
                "(`id`, `log_id`, `invoke_param`, `invoke_result`) " +
                "VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(sql,item.getId(),item.getLogId(),item.getInvokeParam(),item.getInvokeResult());
    }
}
