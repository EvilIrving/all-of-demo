package com.ygkj.soft.database.dao;

import com.ygkj.soft.database.constant.Constants;
import com.ygkj.soft.database.api.VO.BusApiQueryVo;
import com.ygkj.soft.database.api.model.BusApi;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ygkj.com.assistant.pager.EasyuiPager;
import ygkj.com.assistant.pager.Pager;
import ygkj.com.assistant.utils.MysqlPageUtil;

import java.util.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @description 接口基础表
 * @author wd
 * @date 2020-09-18
 */
@Repository
public class BusApiDao{

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private ApiCategoryDao apiCategoryDao;

    public int add(BusApi busApi) {
        busApi.setCreatedTime(new Date());
        if(null==busApi.getApiType()){
            busApi.setApiType(1);
        }
        return jdbcTemplate.update("insert into bus_api  (id,api_name,out_url,api_url,api_status,api_type,request_type,api_remark,response_type,response_example,belong_dept,category_id,created_by,created_time ) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?)",
                busApi.getId(),busApi.getApiName(),busApi.getOutUrl(),busApi.getApiUrl(),busApi.getApiStatus(),
                busApi.getApiType(),busApi.getRequestType(),busApi.getApiRemark(),busApi.getResponseType(),busApi.getResponseExample(),
                busApi.getBelongDept(),busApi.getCategoryId(),busApi.getCreatedBy(),busApi.getCreatedTime());
    }

    public int update(BusApi busApi) {
        if(null==busApi.getApiType()){
            busApi.setApiType(1);
        }
        return jdbcTemplate.update("UPDATE  bus_api  SET api_name=?,out_url=?,api_url=?,api_status=?,request_type=?,api_remark=?,response_type=?,response_example=?,belong_dept=?,category_id=?,created_by=?"
                        +" where id=?",
                busApi.getApiName(),busApi.getOutUrl(),busApi.getApiUrl(),busApi.getApiStatus(),busApi.getRequestType(),busApi.getApiRemark(),busApi.getResponseType(),busApi.getResponseExample(),busApi.getBelongDept(),busApi.getCategoryId(),busApi.getUpdatedBy(),
                busApi.getId());
    }

    public int delete(String id) {
        return jdbcTemplate.update("update bus_api set deleted=1 where id=?",id);
    }

    public BusApi findById(String id) {
        List<BusApi> list = jdbcTemplate.query("select * from bus_api where id=?", new Object[]{id}, new BeanPropertyRowMapper<BusApi>(BusApi.class));
        if(list!=null && list.size()>0){
            BusApi busApi = list.get(0);
            return busApi;
        }else{
            return null;
        }
    }

    public List<BusApi> findAllList(Map<String,Object> params) {
        List<BusApi> list = jdbcTemplate.query("select * from bus_api", new Object[]{}, new BeanPropertyRowMapper<BusApi>(BusApi.class));
        if(list!=null && list.size()>0){
            return list;
        }else{
            return null;
        }
    }

    public Pager pageList(BusApiQueryVo queryVo, EasyuiPager easyuiPager) throws Exception{
        BusApi param = new BusApi();
        BeanUtils.copyProperties(queryVo,param);
        String sql = "select * from bus_api where deleted=0";

        List<Object> params = new ArrayList<>();

        if(StringUtils.isNotBlank(param.getApiName())){
            sql += " AND api_name like ?";
            params.add("%"+param.getApiName()+"%");
        }

        if(StringUtils.isNotBlank(param.getBelongDept())){
            sql += " AND belong_dept = ?";
            params.add(param.getBelongDept());
        }

        if(StringUtils.isNotBlank(param.getApiStatus())){
            sql += " AND api_status = ?";
            params.add(param.getApiStatus());
        }

		// 分类ID
        if(StringUtils.isNotBlank(param.getCategoryId())){
            sql += " AND category_id = ?";
            params.add(param.getCategoryId());
        }

        // 接口类型
        if(null!=param.getApiType()){
            sql += " AND api_type = ?";
            params.add(param.getApiType());
        }


        //按照上线时间倒序
        sql += " ORDER BY created_time DESC ";
        // 查总数
        int total = jdbcTemplate.queryForObject(MysqlPageUtil.getCountSql(sql),params.toArray(),Integer.class);
        // 分页，pager为空就不分页
        if(null!=easyuiPager  && !easyuiPager.isFetchAll()){
            int start = MysqlPageUtil.getStart(easyuiPager.getPage(), easyuiPager.getRows());
            sql = MysqlPageUtil.pageSqlForMysql(sql, start, easyuiPager.getRows());
        }
        Pager pager = new Pager();

        pager.setItemsTotal(total);

        List<BusApi> list = Collections.emptyList();
        if(total>0){
            list = jdbcTemplate.query(sql, params.toArray() , new BeanPropertyRowMapper<>(BusApi.class));

            // 查询分类信息
            list.stream().forEach(busApi -> {
                try {
                    BusApi apiCategoryByCategotyId = apiCategoryDao.getApiCategoryByCategotyId(busApi.getCategoryId());
                    if(null!=apiCategoryByCategotyId){
                        busApi.setCategoryFst(apiCategoryByCategotyId.getCategoryFst());
//                        busApi.setCategorySec(apiCategoryByCategotyId.getCategorySec());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
        pager.setList(list);
        return pager;
    }

    /**
     * @Author Huang.zh
     * @Description 统计服务总共的调用次数
     * @Date 2020/9/18 16:32
     */
    public int countApiInvokeNumber(String date){
        String sql = Constants.SELECT_COUNT+ "bus_api_invoke_log ";
        List<Object> params = new ArrayList<>();
        if (StringUtils.isNotBlank(date)){
            //传了日期就统计当天调用总数，不传统计所有调用次数
            sql += " WHERE DATE_FORMAT(invoke_time,'%Y-%m-%d') = ? ";
            params.add(date);
        }
        return jdbcTemplate.queryForObject(sql,params.toArray(),Integer.class);
    }

    /**
     * 根据对外地址查询api详细信息
     *
     * @param outUrl
     * @return
     */
    public BusApi findByOutUrl(String outUrl) {
        String sql = "SELECT * FROM bus_api WHERE out_url = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(BusApi.class), outUrl);
        }catch (EmptyResultDataAccessException e){
            return null;
        }
    }
}
