package com.ygkj.soft.database.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ygkj.jwt.JwtTokenUtil;
import com.ygkj.jwt.constants.Constants;
import com.ygkj.soft.database.api.VO.BusDataBaseAddVO;
import com.ygkj.soft.database.api.VO.BusDataTableAddVo;
import com.ygkj.soft.database.api.VO.BusDataTablePageQueryVo;
import com.ygkj.soft.database.api.VO.BusDataTableStatusChangeVo;
import com.ygkj.soft.database.api.model.BusDataColumn;
import com.ygkj.soft.database.api.model.BusDataResource;
import com.ygkj.soft.database.api.model.BusDataSource;
import com.ygkj.soft.database.api.model.BusDataTable;
import com.ygkj.soft.database.dao.BusDataResourceDao;
import com.ygkj.soft.database.dao.BusinessDataDao;
import com.ygkj.soft.database.dao.SchemaDao;
import com.ygkj.soft.database.service.SchemaService;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import ygkj.com.assistant.pager.Pager;

import javax.servlet.http.HttpServletRequest;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * @author Huang.zh
 * @date 2020/9/7 17:52
 * @Description: 数据源元数据服务层具体实现
 */
@Service("schemaService")
@Slf4j
public class SchemaServiceImpl implements SchemaService, InitializingBean {

    @Autowired
    private SchemaDao schemaDao;
    @Autowired(required = false)
    private DynamicDataSourceJDBCTemplateRegister register;
    @Autowired
    private BusinessDataDao businessDataDao;
    @Autowired
    private BusDataResourceDao busDataResourceDao;

    @Override
    public List<JSONObject> getBusDataSource(BusDataSource dataSource) {
        List<JSONObject> result = new ArrayList<>();
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        if (request!=null){
            dataSource.setDeptId(JwtTokenUtil.getDeptId(request.getHeader(Constants.ACCESS_TOKEN)));
        }
        List<BusDataSource> dataSources = schemaDao.getBusDataSource(dataSource);
        String dataSourceIds = dataSources.stream().map(BusDataSource::getId).collect(Collectors.joining(","));
        //查询每个数据源对应的表划分目录
        BusDataResource resource = new BusDataResource();
        resource.setDataSourceId(dataSourceIds);
        List<BusDataResource> resources = busDataResourceDao.findBusDataSource(resource);
        //根据数据源id分组
        Map<String, List<BusDataResource>> dataResourceMap = resources.stream()
                .collect(Collectors.groupingBy(BusDataResource::getDataSourceId));
        BusDataTable table = new BusDataTable();

        table.setDataSourceId(dataSourceIds);
        //过滤没有中文注释的表
        List<BusDataTable> tables = schemaDao.getBusDataTable(table).stream().filter(tb -> StringUtils.isNotBlank(tb.getRemarks()))
                .collect(Collectors.toList());
        Map<String, List<BusDataTable>> map = tables.stream().collect(Collectors.groupingBy(BusDataTable::getDataSourceId));
        Map<String, List<BusDataTable>> resourceTableMap = tables.stream().filter(busDataTable -> StringUtils.isNotBlank(busDataTable.getResourceId()))
                .collect(Collectors.groupingBy(BusDataTable::getResourceId));
        for (BusDataSource source : dataSources) {
            JSONObject object = (JSONObject) JSON.toJSON(source);
            object.put("type","db");
            List<BusDataResource> list = dataResourceMap.get(source.getId());
            List<BusDataTable> tableList = map.get(source.getId());
            JSONArray array = new JSONArray();
            //查询子级划分
            if (list != null){
                for (BusDataResource busDataResource : list) {
                    JSONObject objectResource = (JSONObject) JSON.toJSON(busDataResource);
                    objectResource.put("remarks",objectResource.getString("name"));
                    objectResource.put("type","resource");
                    //已经被分类的表
                    List<BusDataTable> resourceTables = resourceTableMap.get(busDataResource.getId());
                    objectResource.put("children",resourceTables);
                    if (resourceTables != null){
                        tableList.removeAll(resourceTables);
                    }
                    array.add(objectResource);
                }
            }
            if(!CollectionUtils.isEmpty(tableList)){
//            if (!tableList.isEmpty()){
                //处理暂未划分的表到未划分目录下
                JSONObject undivided = new JSONObject();
                undivided.put("type","resource");
                undivided.put("remarks","未划分");
                undivided.put("children",tableList);
                undivided.put("dataSourceId",source.getId());
                array.add(undivided);
            }
            object.put("children",array);
            result.add(object);
        }
        return result;
    }

    @Override
    public List<BusDataTable> getBusDataTable(BusDataTable busDataTable) {
        return schemaDao.getBusDataTable(busDataTable);
    }

    @Override
    public List<BusDataColumn> getBusDataColumn(BusDataColumn busDataColumn) {
        List<BusDataColumn> column = schemaDao.getBusDataColumn(busDataColumn);
        return column;
    }

    /**
     * @Author Huang.zh
     * @Description 添加数据源
     * @Date 2020/9/7 21:30
     */
    @Transactional
    @Override
    public void addDataSource(BusDataBaseAddVO dataBaseAddVO) throws Exception{
        BusDataSource dataSource = new BusDataSource();
        BeanUtils.copyProperties(dataBaseAddVO,dataSource);
        if (StringUtils.isBlank(dataSource.getId())){
            dataSource.setId(UUID.randomUUID().toString());
        }
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        if (request!=null){
            dataSource.setDeptId(JwtTokenUtil.getDeptId(request.getHeader(Constants.ACCESS_TOKEN)));
        }
        //先注册数据源并为其指定相应的JDBCTemplate
        JdbcTemplate currentRegisteredJdbcTemplate = register.JDBCTemplateRegistry(dataSource);
        businessDataDao.createUndoLogTable(currentRegisteredJdbcTemplate);
        //将配置的数据源信息持久化
        int rows = schemaDao.addDataSource(dataSource);
        if (rows != 1){
            throw new RuntimeException("配置异常！请联系管理员！");
        }
    }

    @Override
    public boolean testDataSourceConnection(BusDataBaseAddVO dataBaseAddVO) throws SQLException {
        BusDataSource dataSource = new BusDataSource();
        BeanUtils.copyProperties(dataBaseAddVO,dataSource);
        return register.testDataSourceConnection(dataSource);
    }

    @Override
    public void addBusDataTable(List<BusDataTable> list) throws Exception{
        List<Object[]> params = new ArrayList<>();
        for (BusDataTable busDataTable : list) {
            params.add(new Object[]{
                    busDataTable.getId(),busDataTable.getDataSourceId(),
                    busDataTable.getTableName()
                    ,busDataTable.getRemarks(),busDataTable.getSort(),
                    busDataTable.getUpdateAt(),busDataTable.getRowsNum()
            });
        }
        schemaDao.addBusDataTable(params);
    }

    @Override
    public void addBusDataColumn(List<BusDataColumn> list) throws Exception {
        List<Object[]> params = new ArrayList<>();
        for (BusDataColumn busDataColumn : list) {
            params.add(new Object[]{
                    busDataColumn.getId(),busDataColumn.getTableId(),busDataColumn.getName(),busDataColumn.isNullable()
                    ,busDataColumn.getColumnType(),busDataColumn.getColumnDefault(),busDataColumn.getRemarks()
                    ,busDataColumn.getColumnLength(),busDataColumn.getEditable(),busDataColumn.getFormComponent(),
                    busDataColumn.getOrderColumn()
            });
        }
        schemaDao.addBusDataColumn(params);
    }

    /**
     * @Author Huang.zh
     * @Description 初始化数据源
     * @Date 2020/9/8 17:45
     */
//    @GlobalTransactional(rollbackFor = Exception.class)
    @Override
    public boolean initDataSource(String dataSourceID) throws Exception {
        log.info("-----开始初始化数据源-----");
        long millis = System.currentTimeMillis();
        boolean flag;
        BusDataSource dataSource = new BusDataSource();
        dataSource.setId(dataSourceID);
        List<BusDataSource> sourceList = schemaDao.getBusDataSource(dataSource);
        log.error("获取数据源：{}",sourceList);
        if (sourceList.isEmpty()){
            throw new RuntimeException("数据源配置异常，请检查！");
        }
        dataSource = sourceList.get(0);
        if (!dataSource.isInit()){
            //之前已经被入库的表
            List<BusDataTable> busDataTables = schemaDao.getBusDataTable(new BusDataTable(dataSource.getId()));
            /*****增量持久化数据源中的表*****/
            //之前已经被入库的表名
            List<String> sourceTables = busDataTables.stream().map(BusDataTable::getTableName).collect(Collectors.toList());
            //当前数据源中实际存在的表
            List<BusDataTable> tables = schemaDao.getSchemaTables(dataSource);
            List<BusDataTable> targetTables= tables.stream().filter(table-> !sourceTables.contains(table.getTableName()))
                    .filter(table -> !table.getTableName().equals("undo_log")).collect(Collectors.toList());
            //先持久化表信息
            addBusDataTable(targetTables);
            List<BusDataColumn> columnList = new ArrayList<>();
            /*****增量创建中间表*****/
            createSoftTable(tables,dataSource);
            /*****最后增量持久化每张表的字段信息*****/
            //再查一遍，保证字段的增量同步依赖于表的增量同步
            busDataTables = schemaDao.getBusDataTable(new BusDataTable(dataSource.getId()));
            //找到当前数据源已经被入库的表的id
            String tableIds = busDataTables.stream().map(BusDataTable::getId).collect(Collectors.joining(","));
            //找到当前已经被入库的表里面的所有字段，根据表id分组
            Map<String, List<BusDataColumn>> columnMap = schemaDao.getBusDataColumn(new BusDataColumn(tableIds)).stream()
                    .collect(Collectors.groupingBy(BusDataColumn::getTableId));
            for (BusDataTable table : busDataTables) {
                if (table.getTableName().equals("undo_log")){
                    continue;
                }
                List<BusDataColumn> list = columnMap.get(table.getId());
                List<BusDataColumn> columns = schemaDao.getSchemaTableColumns(dataSource, table);
                for (BusDataColumn column : columns) {
                    if (column.getName().equals(com.ygkj.soft.database.constant.Constants.DEFAULT_PRIMARY_KEY)){
                        column.setNullable(true);
                    }
                }
                AtomicInteger index;
                if (list != null && !list.isEmpty()){
                    List<String> existColumns = list.stream().map(BusDataColumn::getName).collect(Collectors.toList());
                    list.sort(Comparator.comparingInt(BusDataColumn::getOrderColumn));
                    Integer currentMaxOrder = list.get(list.size() - 1).getOrderColumn();
                    index = new AtomicInteger(currentMaxOrder.intValue());
                    //过滤已经被持久化的字段
                    columns = columns.stream().filter(column ->!existColumns.contains(column.getName())).collect(Collectors.toList());
                }else{
                    index = new AtomicInteger(1);
                    //指定排序号，用于维护原表的字段顺序
                }
                columns.stream().forEach(column -> column.setOrderColumn(index.getAndAdd(1)));
                columnList.addAll(columns);
            }
            if (!columnList.isEmpty()){
                addBusDataColumn(columnList);
            }
            dataSource.setInit(true);
            schemaDao.setDataSourceInitStatus(dataSource);
        }
        flag = true;
        log.info("-----初始化数据源完毕！耗时：{}毫秒-----",System.currentTimeMillis() - millis);
        return flag;
    }


    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public void createSoftTable(List<BusDataTable> list,BusDataSource dataSource) throws Exception {
        List<String> existTables = schemaDao.getSchemaExistTables();
        //过滤已经创建的中间表，增量创建
        list = list.stream().filter(busDataTable ->
                !existTables.contains(dataSource.getDataBaseName().concat("_").concat(busDataTable.getTableName())))
                .collect(Collectors.toList());
        List<String> tableNames = list.stream().map(table -> dataSource.getDataBaseName().concat("_").concat(table.getTableName()))
                .collect(Collectors.toList());
        schemaDao.createSoftTable(tableNames);
    }

    /**
     * @Author Huang.zh
     * @Description 分页查询指定条件下的表
     * @Date 2020/11/9 13:40
     */
    @Override
    public Pager queryBusDataTableByPage(BusDataTablePageQueryVo vo) {
        return schemaDao.getBusDataTableByPaginate(vo);
    }

    @Override
    public void addBusDataTable(BusDataTableAddVo vo) throws Exception{
        BusDataTable table = new BusDataTable();
        BeanUtils.copyProperties(vo,table);
        table.setId(UUID.randomUUID().toString());
        schemaDao.addBuaDataTable(table);
    }

    @Override
    public void changeTableStatus(BusDataTableStatusChangeVo vo) throws Exception {
        schemaDao.changeTableStatus(vo);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        //初始化之前配置过的所有数据源
        List<BusDataSource> sourceList = schemaDao.getBusDataSource(new BusDataSource());
        if (!sourceList.isEmpty()){
            for (BusDataSource dataSource : sourceList) {
                register.JDBCTemplateRegistry(dataSource);
            }
        }
        log.info("-----恢复已配置的数据源完毕！-----");
    }


}
