package com.ygkj.soft.database.dao;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ygkj.soft.database.constant.Constants;
import com.ygkj.soft.database.service.impl.DynamicDataSourceJDBCTemplateRegister;
import com.ygkj.jwt.JwtTokenUtil;
import com.ygkj.soft.database.api.VO.*;
import com.ygkj.soft.database.api.model.BusDataSourcePersistence;
import com.ygkj.soft.database.api.model.SoftData;
import com.ygkj.soft.database.util.JsonRowMapper;
import io.seata.spring.annotation.GlobalTransactional;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import springfox.documentation.spring.web.json.Json;
import ygkj.com.assistant.pager.EasyuiPager;
import ygkj.com.assistant.pager.Pager;
import ygkj.com.assistant.utils.MysqlPageUtil;
import ygkj.com.util.DateUtil;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Huang.zh
 * @date 2020/9/8 15:48
 * @Description: 业务数据查询专用持久层
 */
@Repository
public class BusinessDataDao {

    @Autowired
    private SchemaDao schemaDao;

    @Autowired
    private DataDao dataDao;

    @Autowired
    private DynamicDataSourceJDBCTemplateRegister register;

    @Autowired
    private JsonRowMapper jsonRowMapper;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * @Author Huang.zh
     * @Description 业务数据分页查询
     * @Date 2020/9/8 16:55
     */
    public Pager queryBusinessDataByPage(BusinessDataQueryVO queryVO, EasyuiPager easyuiPager){
        JdbcTemplate customJDBCTemplate = register.getCustomJDBCTemplate(queryVO.getDataBaseName());
        String sql = Constants.SELECT+queryVO.getTableName()+
                Constants.WHERE+Constants.DEFAULT_DELETE_COLUMN_KEY+Constants.EQUALS+Constants.NOT_DELETED;
        List<Object> params = new ArrayList<>();
        if (StringUtils.isNotBlank(queryVO.getId())){
            sql += Constants.AND+Constants.DEFAULT_PRIMARY_KEY+Constants.PLACE_HOLDER;
            params.add(queryVO.getId());
        }
        if (StringUtils.isNotBlank(queryVO.getDivideStatus())){
            if (Constants.UNDIVIDED_STATUS.equals(queryVO.getDivideStatus())){
                sql += Constants.AND+Constants.DEFAULT_DEPARTMENT_ID_KEY+Constants.IS_NULL;
            }else if (Constants.DIVIDED_STATUS.equals(queryVO.getDivideStatus())){
                sql += Constants.AND+Constants.DEFAULT_DEPARTMENT_ID_KEY+Constants.IS_NOT_NULL;
            }
        }
        //明确数据权限，不是管理员的用户只能查看自己科室下的数据
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String token = request.getHeader("authorization");
//        if (!JwtTokenUtil.getUserAdmin(token)){
//            String deptId = JwtTokenUtil.getDeptId(token);
//            sql += Constants.AND+Constants.DEFAULT_DEPARTMENT_ID_KEY+Constants.EQUALS+Constants.QUESTION_MARK;
//            params.add(deptId);
//        }
        // 查总数
        int total = customJDBCTemplate.queryForObject(MysqlPageUtil.getCountSql(sql),params.toArray(),Integer.class);
        // 分页，pager为空就不分页
        if(null!=easyuiPager  && !easyuiPager.isFetchAll()){
            int start = MysqlPageUtil.getStart(easyuiPager.getPage(), easyuiPager.getRows());
            sql = MysqlPageUtil.pageSqlForMysql(sql, start, easyuiPager.getRows());
        }
        Pager pager = new Pager();
        List<JSONObject> list = customJDBCTemplate.query(sql, jsonRowMapper, params.toArray());
        pager.setList(list);
        pager.setItemsTotal(total>-1?total:list.size());
        return pager;
    }

    /**
     * @Author Huang.zh
     * @Description
     * @Date 2020/9/9 14:39
     */
    public void insertSoftData(SoftDataAddVO softDataAddVO) throws Exception{
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String token = request.getHeader("authorization");
        String userId = JwtTokenUtil.getUserId(token);
        String targetTableName = softDataAddVO.getDataBaseName().concat("_").concat(softDataAddVO.getTableName().replaceAll("dts_yh_basic_data_db_","")
                .replaceAll("dts_yh_business_data_db_",""));
        SoftData softData = new SoftData();
        BeanUtils.copyProperties(softDataAddVO,softData);
        //设置主键
        softData.setGlobalId(UUID.randomUUID().toString());
        JSONObject object = JSONObject.parseObject(softData.getData());
        //判断数据版本
        if (softDataAddVO.getOperationType().equals(Constants.OPERATION_ADD)){
            //type为1的时候说明是新增操作
            softData.setVersion(0);
            object.put(Constants.DEFAULT_CREATOR_ID_KEY,userId);
        }else if (softDataAddVO.getOperationType().equals(Constants.OPERATION_EDIT)){
            object.put(Constants.DEFAULT_UPDATOR_ID_KEY,userId);
            //type为2的时候说明是修改操作
            Map<String,Object> params = new HashMap<>();
            params.put("id",softData.getId());
            List<JSONObject> objectList = dataDao.queryByCondition(params, targetTableName, " LIMIT 1 ");
            if (objectList.isEmpty()){
                softData.setVersion(1);
            }else{
                softData.setVersion(objectList.get(0).getIntValue("version")+1);
            }
        }
        if (object != null){
            softData.setData(object.toJSONString());
        }
        Map<String, Object> map = (Map) BeanMap.create(softData);
        map = map.entrySet()
                .stream()
                .filter(e -> e.getValue() != null)
                .collect(Collectors.toMap(p -> p.getKey(), p -> p.getValue()));
        String sql = dataDao.concatInsertSql(targetTableName,map,true);
        jdbcTemplate.update(sql, map.values().toArray());
    }

    /**
     * @Author Huang.zh
     * @Description 查询中间库处在审核流程中的数据
     * @Date 2020/9/9 17:28
     */
    public Pager querySoftData(SoftDataQueryVO softDataQueryVO,EasyuiPager easyuiPager) throws Exception{
        String dataBaseName = softDataQueryVO.getDataBaseName();
        String targetTableName = dataBaseName.concat("_").concat(softDataQueryVO.getTableName().replaceAll("dts_yh_basic_data_db_","")
                .replaceAll("dts_yh_business_data_db_",""));
        SoftData softData = new SoftData();
        BeanUtils.copyProperties(softDataQueryVO,softData);
        if (StringUtils.isEmpty(softData.getStatus())){
            softData.setStatus(null);
        }
        Map<String, Object> map = (Map) BeanMap.create(softData);
        //过滤null值和不存在的字段，防止查询参数个数和占位符个数不匹配
        map = map.entrySet()
                .stream()
                .filter(e -> e.getValue() != null && !e.getValue().equals(""))
                .filter(e -> !e.getKey().equals(Constants.DEFAULT_DATA_BASE_NAME_KEY) && !e.getKey().equals(Constants.DEFAULT_TABLE_NAME_KEY))
                .collect(Collectors.toMap(p -> p.getKey(), p -> p.getValue()));
        String querySql = dataDao.concatQuerySql(targetTableName, map);
        RowMapper<SoftData> mapper = new BeanPropertyRowMapper<>(SoftData.class);
        //待审核的数据靠前
        querySql += " ORDER BY status ";
        // 查总数
        int total = jdbcTemplate.queryForObject(MysqlPageUtil.getCountSql(querySql),map.values().toArray(),Integer.class);
        // 分页，pager为空就不分页
        if(null!=easyuiPager  && !easyuiPager.isFetchAll()){
            int start = MysqlPageUtil.getStart(easyuiPager.getPage(), easyuiPager.getRows());
            querySql = MysqlPageUtil.pageSqlForMysql(querySql, start, easyuiPager.getRows());
        }
        Pager pager = new Pager();
        List<SoftData> list = jdbcTemplate.query(querySql,map.values().toArray(),mapper);
        for (SoftData data : list) {
            String operationType = data.getOperationType();
            switch (operationType){
                case Constants.OPERATION_ADD:
                    data.setOperationType("新增");
                    break;
                case Constants.OPERATION_EDIT:
                    data.setOperationType("修改");
                    break;
                case Constants.OPERATION_DELETE:
                    data.setOperationType("删除");
                    break;
            }
            String status = data.getStatus();
            switch (status){
                case "0":
                    data.setStatus("待审核");
                    break;
                case "1":
                    data.setStatus("已通过");
                    break;
                case "2":
                    data.setStatus("已驳回");
                    break;
            }
        }
        pager.setList(list);
        pager.setItemsTotal(total>-1?total:list.size());
        return pager;
    }

    /**
     * @Author Huang.zh
     * @Description 修改中间数据的审核状态
     * @Date 2020/9/10 11:31
     */
    public int updateSoftDataStatus(SoftDataUpdateVO softDataUpdateVO) throws Exception{
        String dataBaseName = softDataUpdateVO.getDataBaseName();

        String targetTableName = dataBaseName.concat("_").concat(softDataUpdateVO.getTableName().replaceAll("dts_yh_basic_data_db_","")
                .replaceAll("dts_yh_business_data_db_",""));
        String sql = " UPDATE ".concat(targetTableName).concat(" SET status = ?,reason=? WHERE global_id = ? ");
        return jdbcTemplate.update(sql,softDataUpdateVO.getStatus(),softDataUpdateVO.getReason(),softDataUpdateVO.getGlobalId());
    }

    /**
     * @Author Huang.zh
     * @Description 通过审核之后操作业务表数据（新增/修改/删除）
     * @Date 2020/9/10 15:56
     */
    public int operateBusinessTableData(SoftData softData) throws Exception{
        int row = 0;
        switch (softData.getOperationType()){
            case "新增":
                //新增
                row = saveBusinessData(softData);
                break;
            case "修改":
                //修改
                row = updateBusinessData(softData);
                break;
            case "删除":
                //删除
                row = deleteBusinessData(softData);
                break;
            default:break;
        }
        return row;
    }

    /**
     * @Author Huang.zh
     * @Description 向指定的库指定的表保存字段
     * @Date 2020/9/10 15:31
     */
    private int saveBusinessData(SoftData softData) throws Exception{
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String token = request.getHeader("authorization");
        String deptId = JwtTokenUtil.getDeptId(token);
        JdbcTemplate customJDBCTemplate = register.getCustomJDBCTemplate(softData.getDataBaseName());
        Map<String, Object> map = JSON.parseObject(softData.getData()).getInnerMap();
        //新增需要生成主键
        map.put(Constants.DEFAULT_PRIMARY_KEY,UUID.randomUUID().toString());
        map.put(Constants.DEFAULT_DEPARTMENT_ID_KEY,deptId);
        String insertSql = dataDao.concatInsertSql(softData.getTableName(), map,false);
        return customJDBCTemplate.update(insertSql,map.values().toArray());
    }

    /**
     * @Author Huang.zh
     * @Description 向指定的库指定的表更新字段
     * @Date 2020/9/10 15:39
     */
    private int updateBusinessData(SoftData softData) throws Exception{
        JdbcTemplate customJDBCTemplate = register.getCustomJDBCTemplate(softData.getDataBaseName());
        JSONObject object = JSONObject.parseObject(softData.getData());
        //设置数据的版本
        object.put(Constants.DEFAULT_DATA_VERSION_KEY,softData.getVersion());
        Map<String, Object> map = object.getInnerMap();
        String id = JSONObject.parseObject(softData.getData()).getString(Constants.DEFAULT_PRIMARY_KEY.replaceAll(" ",""));
        String updateSql = dataDao.concatUpdateSql(softData.getTableName(),"",id, map,false);
        return customJDBCTemplate.update(updateSql,map.values().toArray());
    }

    /**
     * @Author Huang.zh
     * @Description 向指定的库指定的表删除记录（逻辑删除，约定删除字段为deleted）
     * @Date 2020/9/10 15:47
     */
    private int deleteBusinessData(SoftData softData) throws Exception{
        JdbcTemplate customJDBCTemplate = register.getCustomJDBCTemplate(softData.getDataBaseName());
        Map<String, Object> map = new HashMap<>();
        map.put(Constants.DEFAULT_DELETE_COLUMN_KEY,1);
        String updateSql = dataDao.concatUpdateSql(softData.getTableName(),"",softData.getId(), map,false);
        return customJDBCTemplate.update(updateSql,map.values().toArray());
    }

    /**
     * @Author Huang.zh
     * @Description 为指定的数据源建立undo_log表，用于支持分布式事务场景，该方法只允许第一次添加数据源的时候调用
     * @Date 2020/9/14 15:22
     */
    public void createUndoLogTable(JdbcTemplate currentRegisteredJdbcTemplate) throws Exception{
        String createTaleSql = "CREATE TABLE `undo_log` (\n" +
                "  `id` bigint(20) NOT NULL AUTO_INCREMENT,\n" +
                "  `branch_id` bigint(20) NOT NULL,\n" +
                "  `xid` varchar(100) NOT NULL,\n" +
                "  `context` varchar(128) NOT NULL,\n" +
                "  `rollback_info` longblob NOT NULL,\n" +
                "  `log_status` int(11) NOT NULL,\n" +
                "  `log_created` datetime NOT NULL,\n" +
                "  `log_modified` datetime NOT NULL,\n" +
                "  `ext` varchar(100) DEFAULT NULL,\n" +
                "  PRIMARY KEY (`id`),\n" +
                "  UNIQUE KEY `ux_undo_log` (`xid`,`branch_id`)\n" +
                ") ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;";
        currentRegisteredJdbcTemplate.update(createTaleSql);
    }

    /**
     * @Author Huang.zh
     * @Description 移除某个目录下的表
     * @Date 2020/9/15 14:36
     */
    public int clearTablesResource(String reourceId) throws Exception{
        String sql = "UPDATE bus_data_table SET resource_id = NULL WHERE resource_id = ? ";
        return jdbcTemplate.update(sql,reourceId);
    }

    /**
     * @Author Huang.zh
     * @Description 将指定的记录操作权限划分给指定的科室
     * @Date 2020/9/17 9:27
     */
    public void divideBusinessData(DataSourceDivideVO divideVO) throws Exception{
        JdbcTemplate customJDBCTemplate = register.getCustomJDBCTemplate(divideVO.getDataBaseName());
        Map<String,Object> params = new HashMap<>();
        params.put(Constants.DEFAULT_DEPARTMENT_ID_KEY,divideVO.getDeptId());
        String sql = dataDao.concatUpdateSql(divideVO.getTableName(),"",divideVO.getId(),params,false);
        customJDBCTemplate.update(sql,params.values().toArray());
    }

}
