package com.ygkj.soft.database.dao;

import com.ygkj.soft.database.api.model.ApiCategory;
import com.ygkj.soft.database.api.model.BusApi;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 * @description 接口分类表
 * @author wd
 * @date 2020-09-18
 */
@Repository
public class ApiCategoryDao{

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public int add(ApiCategory apiCategory) {
        return jdbcTemplate.update("insert into bus_api_category  (id,category_name,category_remark,parent_id,create_id,create_time,update_id,update_time,deleted ) values (?,?,?,?,?,?,?,?,? )",
                apiCategory.getId(),apiCategory.getCategoryName(),apiCategory.getCategoryRemark(),apiCategory.getParentId(),apiCategory.getCreateId(),apiCategory.getCreateTime(),apiCategory.getUpdateId(),apiCategory.getUpdateTime(),apiCategory.getDeleted());
    }

    public int update(ApiCategory apiCategory) {
        return jdbcTemplate.update("UPDATE  bus_api_category  SET category_name=?,category_remark=?,parent_id=?,create_id=?,create_time=?,update_id=?,update_time=?,deleted=?"
                        +" where id=?",
                apiCategory.getCategoryName(),apiCategory.getCategoryRemark(),apiCategory.getParentId(),apiCategory.getCreateId(),apiCategory.getCreateTime(),apiCategory.getUpdateId(),apiCategory.getUpdateTime(),apiCategory.getDeleted(),
                apiCategory.getId());
    }

    public int delete(String id) {
        return jdbcTemplate.update("update bus_api_category set deleted=1 where id=?",id);
    }

    public ApiCategory findById(String id) {
        List<ApiCategory> list = jdbcTemplate.query("select * from bus_api_category where deleted=0 and id=?", new Object[]{id}, new BeanPropertyRowMapper<ApiCategory>(ApiCategory.class));
        if(list!=null && list.size()>0){
            ApiCategory apiCategory = list.get(0);
            return apiCategory;
        }else{
            return null;
        }
    }

/**
     * 根据父级目录ID查询所有目录
     * @param parentId
     * @return
     * @throws Exception
     */
    public List<ApiCategory> findAllList(String parentId) throws Exception {

        String sql = "select * from bus_api_category where deleted=0";
        List<Object> params = new ArrayList<>();

        if(StringUtils.isEmpty(parentId)){
            sql+= " AND parent_id is null";
        }else{
            sql+= " AND parent_id=?";
            params.add(parentId);
        }

        List<ApiCategory> list = jdbcTemplate.query(sql, params.toArray(), new BeanPropertyRowMapper<ApiCategory>(ApiCategory.class));
        if(list!=null && list.size()>0){
            return list;
        }else{
            return null;
        }
    }

    /**
     * 根据二级目录的ID，查询一级名称和二级目录名称
     * @param categoryId
     * @return
     * @throws Exception
     */
    public BusApi getApiCategoryByCategotyId(String categoryId) throws Exception {

        ApiCategory second = this.findById(categoryId);

        String sql = "select lv2.category_name category_sec,lv1.category_name category_fst from bus_api_category lv2 \n" +
                "LEFT JOIN bus_api_category lv1 on lv2.parent_id = lv1.id and lv1.deleted=0\n" +
                "where lv2.deleted=0 AND lv2.id=?";
        List<Object> params = new ArrayList<>();
        params.add(categoryId);

        List<BusApi> list = jdbcTemplate.query(sql, params.toArray(), new BeanPropertyRowMapper<BusApi>(BusApi.class));
        if(list!=null && list.size()>0){
            BusApi busApi = list.get(0);
            if(StringUtils.isBlank(busApi.getCategoryFst())){
                busApi.setCategoryFst(busApi.getCategorySec());
                busApi.setCategorySec(null);
            }
            return busApi;
        }else{
            return null;
        }
    }

}
