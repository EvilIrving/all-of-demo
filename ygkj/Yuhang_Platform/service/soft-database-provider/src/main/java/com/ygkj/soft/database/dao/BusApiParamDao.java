package com.ygkj.soft.database.dao;

import com.ygkj.soft.database.api.model.BusApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ygkj.com.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @description 接口参数
 * @author wd
 * @date 2020-09-20
 */
@Repository
public class BusApiParamDao{

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public int add(BusApiParam busApiParam) {
        return jdbcTemplate.update("insert into bus_api_param  (id,bus_type,api_id,param_name,param_desc,param_type,param_required,default_value,create_id,create_time,update_id,update_time,deleted ) values (?,?,?,?,?,?,?,?,?,?,?,?,? )",
                busApiParam.getId(),busApiParam.getBusType(),busApiParam.getApiId(),busApiParam.getParamName(),busApiParam.getParamDesc(),busApiParam.getParamType(),busApiParam.getParamRequired(),busApiParam.getDefaultValue(),busApiParam.getCreatedBy(),busApiParam.getCreatedTime(),busApiParam.getUpdatedBy(),busApiParam.getUpdatedTime(),busApiParam.getDeleted());
    }

    public int update(BusApiParam busApiParam) {
        return jdbcTemplate.update("UPDATE  bus_api_param  SET bus_type=?,api_id=?,param_name=?,param_desc=?,param_type=?,param_required=?,default_value=?,create_id=?,create_time=?,update_id=?,update_time=?,deleted=?"
                        +" where id=?",
                busApiParam.getBusType(),busApiParam.getApiId(),busApiParam.getParamName(),busApiParam.getParamDesc(),busApiParam.getParamType(),busApiParam.getParamRequired(),busApiParam.getDefaultValue(),busApiParam.getCreatedBy(),busApiParam.getCreatedTime(),busApiParam.getUpdatedBy(),busApiParam.getUpdatedTime(),busApiParam.getDeleted(),
                busApiParam.getId())
                ;
    }

    public int delete(String id) {
        return jdbcTemplate.update("update bus_api_param set deleted=1 where id=?",id);
    }

    public BusApiParam findById(int id) {
        List<BusApiParam> list = jdbcTemplate.query("select * from bus_api_param where id=?", new Object[]{id}, new BeanPropertyRowMapper<BusApiParam>(BusApiParam.class));
        if(list!=null && list.size()>0){
            BusApiParam busApiParam = list.get(0);
            return busApiParam;
        }else{
            return null;
        }
    }

    public List<BusApiParam> findAllList(String apiId, Integer bus_type) throws Exception {

        String sql = "select * from bus_api_param where deleted=0";
        List<Object> params = new ArrayList<>();

        if(StringUtils.isNullOrEmpty(apiId)){
            throw new Exception("apiId不能为空");
        }else{
            sql+= " AND api_id=?";
            params.add(apiId);
        }

        if(null!=bus_type){
            sql+= " AND bus_type=?";
            params.add(bus_type);
        }

        List<BusApiParam> list = jdbcTemplate.query(sql, params.toArray(), new BeanPropertyRowMapper<BusApiParam>(BusApiParam.class));
        if(list!=null && list.size()>0){
            return list;
        }else{
            return null;
        }
    }

    public void batchSave(List<BusApiParam> list) throws Exception{

        List<Object[]> params = new ArrayList<>();
        for (BusApiParam busApiParam : list) {
            params.add(new Object[]{
                    busApiParam.getId(),
                    busApiParam.getBusType(),
                    busApiParam.getApiId(),
                    busApiParam.getParamName(),
                    busApiParam.getParamDesc(),
                    busApiParam.getParamType(),
                    busApiParam.getParamRequired(),
                    busApiParam.getDefaultValue(),
                    busApiParam.getCreatedBy()
            });
        }
        jdbcTemplate.batchUpdate("insert into bus_api_param  (id,bus_type,api_id,param_name,param_desc,param_type,param_required,default_value,create_id) values (?,?,?,?,?,?,?,?,?)", params);
    }

    public void batchUpdate(List<BusApiParam> list) throws Exception{

        List<Object[]> params = new ArrayList<>();
        for (BusApiParam busApiParam : list) {
            params.add(new Object[]{
                    busApiParam.getBusType(),
                    busApiParam.getApiId(),
                    busApiParam.getParamName(),
                    busApiParam.getParamDesc(),
                    busApiParam.getParamType(),
                    busApiParam.getParamRequired(),
                    busApiParam.getDefaultValue(),
                    busApiParam.getUpdatedBy(),
                    busApiParam.getId(),
            });
        }
        jdbcTemplate.batchUpdate("UPDATE  bus_api_param  SET bus_type=?,api_id=?,param_name=?,param_desc=?,param_type=?,param_required=?,default_value=?,update_id=? where id=?", params);
    }
}
