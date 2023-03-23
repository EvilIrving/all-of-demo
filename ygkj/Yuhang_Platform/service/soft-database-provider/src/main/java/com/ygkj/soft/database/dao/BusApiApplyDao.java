package com.ygkj.soft.database.dao;

import com.ygkj.soft.database.api.VO.ApiApplyQueryVo;
import com.ygkj.soft.database.api.model.ApiApplyStatusEnum;
import com.ygkj.soft.database.api.model.BusApi;
import com.ygkj.soft.database.api.model.BusApiApply;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ygkj.com.assistant.pager.EasyuiPager;
import ygkj.com.assistant.pager.Pager;
import ygkj.com.assistant.utils.MysqlPageUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * @description 接口申请表
 * @author wangd
 * @date 2020-09-24
 */
@Repository
public class BusApiApplyDao{

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private ApiCategoryDao apiCategoryDao;

    public int add(BusApiApply busApiApply) {
        return jdbcTemplate.update("insert into bus_api_apply  (id,api_id,contact_man,contact_phone,contact_mail,apply_reason,apply_system,apply_man,apply_time,create_id,create_time ) values (?,?,?,?,?,?,?,?,?,?,? )",
                busApiApply.getId(),busApiApply.getApiId(),busApiApply.getContactMan(),busApiApply.getContactPhone(),busApiApply.getContactMail(),busApiApply.getApplyReason(),busApiApply.getApplySystem(),busApiApply.getApplyMan(),busApiApply.getApplyTime(),busApiApply.getCreateId(),busApiApply.getCreateTime());
    }

    public int update(BusApiApply busApiApply) {
        return jdbcTemplate.update("UPDATE  bus_api_apply  SET api_id=?,contact_man=?,contact_phone=?,contact_mail=?,apply_reason=?,apply_system=?,apply_status=?,apply_man=?,apply_time=?,update_id=?,update_time=?"
                        +" where id=?",
                busApiApply.getApiId(),busApiApply.getContactMan(),busApiApply.getContactPhone(),busApiApply.getContactMail(),busApiApply.getApplyReason(),busApiApply.getApplySystem(),busApiApply.getApplyStatus(),busApiApply.getApplyMan(),busApiApply.getApplyTime(),busApiApply.getUpdateId(),busApiApply.getUpdateTime(),
                busApiApply.getId())
                ;
    }

    public int delete(String id) {
        return jdbcTemplate.update("update bus_api_apply set deleted=1 where id=?",id);
    }

    public BusApiApply findById(String id) {
        List<BusApiApply> list = jdbcTemplate.query("select * from bus_api_apply where id=?", new Object[]{id}, new BeanPropertyRowMapper<BusApiApply>(BusApiApply.class));
        if(list!=null && list.size()>0){
            BusApiApply busApiApply = list.get(0);
            return busApiApply;
        }else{
            return null;
        }
    }

    public int updateState(String applyId, String remark, ApiApplyStatusEnum status) {
        String sql = "update bus_api_apply set apply_status=?,apply_man=?,apply_time=?,apply_remark=? where id=?";
        List<Object> params = new ArrayList<>();
        params.add(status.getStatus());
        params.add(0);
        params.add(new Date());
        params.add(remark);
        params.add(applyId);
        return jdbcTemplate.update(sql,params.toArray());
    }

 /*   public List<BusApiApply> findAllList(Map<String,Object> params) {
        List<BusApiApply> list = jdbcTemplate.query("select * from bus_api_apply", new Object[]{}, new BeanPropertyRowMapper<BusApiApply>(BusApiApply.class));
        if(list!=null && list.size()>0){
            return list;
        }else{
            return null;
        }
    }*/

    public Pager queryAuditRecordsFromMe(ApiApplyQueryVo queryVo, EasyuiPager easyuiPager) throws Exception {

        BusApiApply param = new BusApiApply();
        BeanUtils.copyProperties(queryVo,param);
        String sql = "select a.*,b.api_name,b.api_type,b.category_id from bus_api_apply a LEFT JOIN bus_api b on a.api_id=b.id where a.deleted=0";

        List<Object> params = new ArrayList<>();

        // 接口类型
        if(null!=param.getApiType()){
            sql += " AND b.api_type = ?";
            params.add(param.getApiType());
        }
        if(StringUtils.isNotBlank(param.getApiName())){
            sql += " AND b.api_name LIKE ?";
            params.add("%" + param.getApiName() + "%");
        }

        //按照上线时间倒序
        sql += " ORDER BY a.create_time DESC ";
        // 查总数
        int total = jdbcTemplate.queryForObject(MysqlPageUtil.getCountSql(sql),params.toArray(),Integer.class);
        // 分页，pager为空就不分页
        if(null!=easyuiPager  && !easyuiPager.isFetchAll()){
            int start = MysqlPageUtil.getStart(easyuiPager.getPage(), easyuiPager.getRows());
            sql = MysqlPageUtil.pageSqlForMysql(sql, start, easyuiPager.getRows());
        }
        Pager pager = new Pager();

        pager.setItemsTotal(total);

        List<BusApiApply> list = Collections.emptyList();
        if(total>0){
            list = jdbcTemplate.query(sql, params.toArray() , new BeanPropertyRowMapper<>(BusApiApply.class));

            // 查询分类信息
            list.stream().forEach(busApi -> {
                try {
                    if(StringUtils.isNotBlank(busApi.getCategoryId())){
                        BusApi apiCategoryByCategotyId = apiCategoryDao.getApiCategoryByCategotyId(busApi.getCategoryId());
                        if(null!=apiCategoryByCategotyId){
                            busApi.setCategoryFst(apiCategoryByCategotyId.getCategoryFst());
                            busApi.setCategorySec(apiCategoryByCategotyId.getCategorySec());
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
        pager.setList(list);
        return pager;
    }

    public Pager queryMyAuditTodo(ApiApplyQueryVo queryVo, EasyuiPager easyuiPager) throws Exception {

        BusApiApply param = new BusApiApply();
        BeanUtils.copyProperties(queryVo,param);
        String sql = "select a.*,b.api_name,b.api_type,b.category_id from bus_api_apply a LEFT JOIN bus_api b on a.api_id=b.id where a.deleted=0";

        List<Object> params = new ArrayList<>();
        // 接口类型
        if(null!=param.getApiType()){
            sql += " AND b.api_type = ?";
            params.add(param.getApiType());
        }
        if(StringUtils.isNotBlank(param.getApiName())){
            sql += " AND b.api_name LIKE ?";
            params.add("%" + param.getApiName() + "%");
        }

        //按照上线时间倒序
        sql += " ORDER BY a.create_time DESC ";
        // 查总数
        int total = jdbcTemplate.queryForObject(MysqlPageUtil.getCountSql(sql),params.toArray(),Integer.class);
        // 分页，pager为空就不分页
        if(null!=easyuiPager  && !easyuiPager.isFetchAll()){
            int start = MysqlPageUtil.getStart(easyuiPager.getPage(), easyuiPager.getRows());
            sql = MysqlPageUtil.pageSqlForMysql(sql, start, easyuiPager.getRows());
        }
        Pager pager = new Pager();

        pager.setItemsTotal(total);

        List<BusApiApply> list = Collections.emptyList();
        if(total>0){
            list = jdbcTemplate.query(sql, params.toArray() , new BeanPropertyRowMapper<>(BusApiApply.class));

            // 查询分类信息
            list.stream().forEach(busApi -> {
                try {
                    if(StringUtils.isNotBlank(busApi.getCategoryId())){
                        BusApi apiCategoryByCategotyId = apiCategoryDao.getApiCategoryByCategotyId(busApi.getCategoryId());
                        if(null!=apiCategoryByCategotyId){
                            busApi.setCategoryFst(apiCategoryByCategotyId.getCategoryFst());
                            busApi.setCategorySec(apiCategoryByCategotyId.getCategorySec());
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

        }
        pager.setList(list);
        return pager;
    }
}
