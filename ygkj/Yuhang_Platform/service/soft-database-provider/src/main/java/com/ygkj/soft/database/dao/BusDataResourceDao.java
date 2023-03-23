package com.ygkj.soft.database.dao;

import com.ygkj.soft.database.api.VO.TableDivideVO;
import com.ygkj.soft.database.api.model.BusDataResource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Huang.zh
 * @date 2020/9/15 9:36
 * @Description: 库表目录划分持久层
 */
@Repository("busDataResourceDao")
public class BusDataResourceDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public int add(BusDataResource busDataResource) {
        return jdbcTemplate.update("insert into bus_data_resource  (id,name,data_source_id ) values (?,?,? )",
                busDataResource.getId(),busDataResource.getName(),busDataResource.getDataSourceId());
    }

    public int update(BusDataResource busDataResource) throws Exception{
        return jdbcTemplate.update("UPDATE  bus_data_resource  SET name=?,data_source_id=?"
                        +" where id=?",
                busDataResource.getName(),busDataResource.getDataSourceId(),
                busDataResource.getId()
        );
    }

    public int delete(String id) {
        return jdbcTemplate.update("DELETE from bus_data_resource where id=?",id);
    }

    public BusDataResource findById(int id) {
        List<BusDataResource> list = jdbcTemplate.query("select * from bus_data_resource where id=?", new Object[]{id}, new BeanPropertyRowMapper<BusDataResource>(BusDataResource.class));
        if(list!=null && list.size()>0){
            BusDataResource busDataResource = list.get(0);
            return busDataResource;
        }else{
            return null;
        }
    }

    public List<BusDataResource> findBusDataSource(BusDataResource busDataResource){
        String sql = "SELECT * FROM bus_data_resource WHERE 1=1 ";
        List<Object> list = new ArrayList<>();
        if (StringUtils.isNotBlank(busDataResource.getDataSourceId())){
            sql += " AND FIND_IN_SET(data_source_id,?)  ";
            list.add(busDataResource.getDataSourceId());
        }
        if (StringUtils.isNotBlank(busDataResource.getId())){
            sql += " AND id = ? ";
            list.add(busDataResource.getId());
        }
        if (StringUtils.isNotBlank(busDataResource.getName())){
            sql += " AND name LIKE ? ";
            list.add("%".concat(busDataResource.getName()).concat("%"));
        }
        return jdbcTemplate.query(sql,new BeanPropertyRowMapper<>(BusDataResource.class),list.toArray());
    }

    /**
     * @Author Huang.zh
     * @Description 为指定的表划分目录
     * @Date 2020/9/15 11:23
     */
    public int divideTableResource(TableDivideVO tableDivideVO){
        String sql = " UPDATE bus_data_table SET resource_id = ? WHERE FIND_IN_SET(id,?) ";
        return jdbcTemplate.update(sql,tableDivideVO.getResourceId(),tableDivideVO.getTableIds());
    }


}
