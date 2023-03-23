package com.ygkj.soft.database.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * @author Huang.zh
 * @date 2020/9/21 15:30
 * @Description:
 */
@Repository
public class WaterAssetsCockpitDao {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public String queryData(){
        String sql = "SELECT DATA FROM water_assets_cockpit_data LIMIT 1";
        return jdbcTemplate.queryForObject(sql,String.class);
    }
}
