package com.ygkj.soft.database.dao;

import com.alibaba.fastjson.JSONObject;
import com.ygkj.soft.database.api.model.BusDataSourcePersistence;
import com.ygkj.soft.database.util.JsonRowMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ygkj.com.util.DateUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @author Huang.zh
 * @date 2020/9/17 17:04
 * @Description:
 */
@Repository("busDataSourcePersistenceDao")
public class BusDataSourcePersistenceDao {

    @Autowired
    private JsonRowMapper jsonRowMapper;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<BusDataSourcePersistence> query(BusDataSourcePersistence busDataSourcePersistence){
        String sql = "SELECT * FROM bus_data_source_persistence WHERE 1 = 1 ";
        List<Object> params = new ArrayList<>();
        if (busDataSourcePersistence.getPersistenceDate() != null){
            sql += " AND persistence_date = ? ";
            params.add(busDataSourcePersistence.getPersistenceDate());
        }
        if (StringUtils.isNotBlank(busDataSourcePersistence.getDataBaseName())){
            sql += " AND data_base_name = ? ";
            params.add(busDataSourcePersistence.getDataBaseName());
        }
        return jdbcTemplate.query(sql,new BeanPropertyRowMapper<>(BusDataSourcePersistence.class),params.toArray());
    }

    public int add(BusDataSourcePersistence busDataSourcePersistence) throws Exception{
        return jdbcTemplate.update("insert into bus_data_source_persistence  (id,data_base_name,persistence_date,row_number) values (?,?,?,?)",
                busDataSourcePersistence.getId(),busDataSourcePersistence.getDataBaseName(),
                busDataSourcePersistence.getPersistenceDate(),busDataSourcePersistence.getRowNumber());
    }

    public int update(BusDataSourcePersistence busDataSourcePersistence) throws Exception{
        return jdbcTemplate.update("UPDATE  bus_data_source_persistence  SET row_number=? where id=?",
                busDataSourcePersistence.getRowNumber(), busDataSourcePersistence.getId());
    }

    /**
     * @Author Huang.zh
     * @Description 更新今天的入库数据总条数
     * @Date 2020/9/17 17:25
     */
    public void updateCurrentDayPersistence(BusDataSourcePersistence busDataSourcePersistence) throws Exception{
        List<BusDataSourcePersistence> list = query(busDataSourcePersistence);
        if (!list.isEmpty()){
            Integer currentPersistenceRows = busDataSourcePersistence.getRowNumber();
            busDataSourcePersistence = list.get(0);
            busDataSourcePersistence.setRowNumber(busDataSourcePersistence.getRowNumber()+currentPersistenceRows);
            update(busDataSourcePersistence);
        }else{
            add(busDataSourcePersistence);
        }
    }

    /**
     * @Author Huang.zh
     * @Description 过去N天里每天存入数据仓的数据总条数
     * @Date 2020/9/17 17:52
     */
    public List<JSONObject> countPastDaysPersistenceRows(int pastDay){
        String date = getPastDate(pastDay - 1);
        String sql = "SELECT SUM(row_number) AS rows,persistence_date FROM bus_data_source_persistence " +
                "WHERE persistence_date >= ? GROUP BY persistence_date";
        return jdbcTemplate.query(sql,jsonRowMapper,date);
    }

    /**
     * @Author Huang.zh
     * @Description 获取past天前的日期
     * @Date 2020/9/17 17:58
     */
    private static String getPastDate(int past) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) - past);
        Date today = calendar.getTime();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String result = format.format(today);
        return result;
    }

}
