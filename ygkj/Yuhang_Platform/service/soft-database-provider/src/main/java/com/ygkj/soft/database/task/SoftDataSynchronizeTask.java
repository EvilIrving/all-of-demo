package com.ygkj.soft.database.task;

import com.alibaba.fastjson.JSONObject;
import com.ygkj.soft.database.dao.BusDataResourceDao;
import com.ygkj.soft.database.service.BusDataSourcePersistenceService;
import com.ygkj.soft.database.service.impl.DynamicDataSourceJDBCTemplateRegister;
import com.ygkj.soft.database.api.VO.BusApiQueryVo;
import com.ygkj.soft.database.api.model.*;
import com.ygkj.soft.database.dao.ApiCategoryDao;
import com.ygkj.soft.database.dao.BusApiDao;
import com.ygkj.soft.database.dao.SchemaDao;
import com.ygkj.soft.database.util.JsonRowMapper;
import io.seata.spring.annotation.GlobalTransactional;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ygkj.com.assistant.pager.EasyuiPager;
import ygkj.com.assistant.pager.Pager;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * @author Huang.zh
 * @date 2020/9/17 18:25
 * @Description: 水利数据资产监控统计任务
 */
@Component
//    @Controller
//    @RequestMapping("/dataSyn")
public class SoftDataSynchronizeTask implements InitializingBean {

    @Autowired
    private BusDataSourcePersistenceService busDataSourcePersistenceService;

    @Value("${schema.prefix}")
    private String prefix;

    @Autowired
    private DynamicDataSourceJDBCTemplateRegister register;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private SchemaDao schemaDao;

    @Autowired
    private BusDataResourceDao busDataResourceDao;

    @Autowired
    private BusApiDao busApiDao;

    @Autowired
    private ApiCategoryDao apiCategoryDao;

    @Autowired
    private JsonRowMapper jsonRowMapper;

    @Autowired
    private SynchronizeThreadProduceFactory synchronizeThreadProduceFactory;

    private ThreadPoolExecutor executor;

//    @RequestMapping("/aaa")
    @Scheduled(cron = "0 0 0/1 * * ?")
    public void execute() throws Exception{
        JSONObject result = new JSONObject();
        // TODO: 2020/9/17 涉及很多数据组装过程，建议每一个接口模块都封装一个方法
        //过去七天入仓数据总量
        List<JSONObject> lastSevenDayPersistenceData = busDataSourcePersistenceService.countPastDaysPersistenceRows(7);
        result.put("lastSevenDayPersistenceData",lastSevenDayPersistenceData);
        //今天入仓数据总量
        JSONObject todayPersistenceData = todayPersistenceData();
        result.put("todayPersistenceData",todayPersistenceData);
        //整个数据仓的数据量
        int allDataRows = allDataRows();
        result.put("allDataRows",allDataRows);
        //各目录下所有表的数据总量
        List<JSONObject> list = countResourceDataRows();
        result.put("tableRowsGroupByResource",list);
        String currentDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        //今日服务调用次数
        int currentDayInvokeNumbers = busApiDao.countApiInvokeNumber(currentDate);
        result.put("currentDayInvokeNumbers",currentDayInvokeNumbers);
        //服务总调用次数
        int totalInvokeNumbers = busApiDao.countApiInvokeNumber("");
        result.put("totalInvokeNumbers",totalInvokeNumbers);
        //分类下的服务
        JSONObject object = queryApisGroupByType();
        result.put("servicesGroupByCategory",object);
        //最新上线的10条服务记录
        Pager pager = queryNewestStartUpService();
        result.put("newestServices",pager);
        //业务分类数据总记录数
        List<JSONObject> byCategory = queryTableDataRowsByCategory();
        result.put("tableRowsByCateGory",byCategory);
        //基础数据分类下的记录数
        try {
            List<JSONObject> basicList = queryBasicDataRows();
            result.put("basicDataTableRows",basicList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        persistenceWaterAssetsData(result);
    }

    /**
     * @Author Huang.zh
     * @Description 持久化整个水利数据资源大屏的统计数据
     * @Date 2020/9/21 15:23
     */
    @GlobalTransactional(rollbackFor = Exception.class)
    public void persistenceWaterAssetsData(JSONObject jsonObject){
        String sql = "TRUNCATE TABLE water_assets_cockpit_data";
        jdbcTemplate.execute(sql);
        sql = "INSERT INTO water_assets_cockpit_data (data) VALUES (?)";
        int row = jdbcTemplate.update(sql, jsonObject.toJSONString());
        if (row != 1){
            throw new RuntimeException();
        }
    }

    /**
     * @Author Huang.zh
     * @Description 查询基础数据条数
     * @Date 2020/9/21 13:39
     */
    private List<JSONObject> queryBasicDataRows() throws Exception{
        String dataBaseName = "ph_basic_data_db";
        JdbcTemplate customJDBCTemplate = register.getCustomJDBCTemplate(dataBaseName);
        List<JSONObject> result = new ArrayList<>();
        Optional.of(customJDBCTemplate).ifPresent(targetJdbcTemplate-> {
            //水利工程
            String[] tableNames = new String[]{"dts_yh_basic_data_db_obj_pro"};
            String sql = concatCountTableDataRowsSql(Arrays.asList(tableNames));
            List<Integer> list = targetJdbcTemplate.queryForList(sql, Integer.class);
            JSONObject project = new JSONObject();
            project.put("number",list.get(0).intValue());
            project.put("type","水利工程");
            result.add(project);
            //监测站点
            tableNames = new String[]{"dts_yh_basic_data_db_st_stbprp_b"};
            sql = concatCountTableDataRowsSql(Arrays.asList(tableNames));
            list = targetJdbcTemplate.queryForList(sql, Integer.class);
            JSONObject station = new JSONObject();
            station.put("number",list.get(0).intValue());
            station.put("type","监测站点");
            result.add(station);
            //江河湖泊
            sql = "SELECT COUNT(1) FROM dts_yh_basic_data_db_obj_wat WHERE WATTP = '0'";
            list = targetJdbcTemplate.queryForList(sql, Integer.class);
            JSONObject river = new JSONObject();
            river.put("number",list.get(0).intValue());
            river.put("type","江河湖泊");
            result.add(river);
            // TODO: 2020/9/21 其他对象的表暂未明确。。。待定
        });
        return result;
    }

    /**
     * @Author Huang.zh
     * @Description 查询各业务分类下表的记录总数
     * @Date 2020/9/21 13:36
     */
    private List<JSONObject> queryTableDataRowsByCategory() {
        List<JSONObject> result = new ArrayList<>();
        Map<String, List<BusDataTable>> tableMap = schemaDao.getBusDataTable(new BusDataTable())
                .stream().filter(table -> StringUtils.isNotBlank(table.getCategory()))
                .collect(Collectors.groupingBy(BusDataTable::getCategory));
        List<BusDataSource> sources = schemaDao.getBusDataSource(new BusDataSource());
        Map<String, List<BusDataSource>> dataSourceMap = sources.stream().collect(Collectors.groupingBy(BusDataSource::getId));
        tableMap.forEach((category,tables)->{
            List<String> dataSourceIds = tables.stream().map(BusDataTable::getDataSourceId)
                    .distinct().collect(Collectors.toList());
            List<String> dataBaseNameList = sources.stream().filter(source -> dataSourceIds.contains(source.getId()))
                    .map(BusDataSource::getDataBaseName).collect(Collectors.toList());
            //将一个分类下的表再根据数据源id进行分组
            AtomicInteger totalRows = new AtomicInteger(0);
            Map<String, List<BusDataTable>> mapGroupByDataSourceId = tables.stream().collect(Collectors.groupingBy(BusDataTable::getDataSourceId));
            mapGroupByDataSourceId.forEach((dataSourceId,sourceTables)->{
                String dataBaseName = dataSourceMap.get(dataSourceId).get(0).getDataBaseName();
                JdbcTemplate customJDBCTemplate = register.getCustomJDBCTemplate(dataBaseName);
                String sql = concatCountTableDataRowsSql(tables.stream().map(BusDataTable::getTableName).collect(Collectors.toList()));
                List<Integer> list = customJDBCTemplate.queryForList(sql, Integer.class);
                int rows = list.stream().mapToInt(Integer::intValue).sum();
                totalRows.addAndGet(rows);
            });
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("type",category);
            jsonObject.put("number",totalRows.get());
            result.add(jsonObject);
        });
        return result;
    }


    /**
     * @Author Huang.zh
     * @Description 查询最新启用的服务，默认十条记录
     * @Date 2020/9/21 10:13
     */
    private Pager queryNewestStartUpService() throws Exception{
        Map<String, List<ApiCategory>> map = apiCategoryDao.findAllList("").stream()
                .collect(Collectors.groupingBy(ApiCategory::getId));
        BusApiQueryVo queryVo = new BusApiQueryVo();
        queryVo.setApiStatus("1");
//        queryVo.setApiType(1);
        Pager pager = null;
        try {
            pager = busApiDao.pageList(queryVo, new EasyuiPager());
            for (BusApi busApi : pager.getList(BusApi.class)) {
                if (StringUtils.isEmpty(busApi.getCategoryId()) || map.get(busApi.getCategoryId()) == null){
                    continue;
                }
                busApi.setCategory(map.get(busApi.getCategoryId()).get(0).getCategoryName());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pager;
    }


    /**
     * @Author Huang.zh
     * @Description 查询分类下的服务
     * @Date 2020/9/18 17:11
     */
    private JSONObject queryApisGroupByType(){
        JSONObject result = new JSONObject();
        List<JSONObject> data = new ArrayList<>();
        try {
            Pager pager = busApiDao.pageList(new BusApiQueryVo(), EasyuiPager.fetchAll());
            List<BusApi> list = pager.getList(BusApi.class).stream().filter(busApi -> StringUtils.isNotBlank(busApi.getCategoryId()))
                    .collect(Collectors.toList());
            Map<String, List<BusApi>> apiGroupByCategory = list.stream().collect(Collectors.groupingBy(BusApi::getCategoryId));
            //根据分类id分组
            Map<String, List<ApiCategory>> map = apiCategoryDao.findAllList("").stream()
                    .collect(Collectors.groupingBy(ApiCategory::getId));
            map.forEach((categoryId,categories)->{
                String categoryName = categories.get(0).getCategoryName();
                JSONObject object = new JSONObject();
                object.put("type",categoryName);
                List<BusApi> apis = apiGroupByCategory.get(categoryId);
                apis = Optional.ofNullable(apis).orElse(new ArrayList<>());
                object.put("number",apis.size());
                data.add(object);
            });
            result.put("total",list.size());
            result.put("list",data);
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }



    /**
     * @Author Huang.zh
     * @Description 获得今日入仓数据量
     * @Date 2020/9/18 9:26
     */
    private JSONObject todayPersistenceData(){
        List<JSONObject> list = busDataSourcePersistenceService.countPastDaysPersistenceRows(1);
        JSONObject todayPersistenceData;
        if (list.isEmpty()){
            todayPersistenceData = new JSONObject();
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            todayPersistenceData.put("persistence_date",format.format(new Date()));
            todayPersistenceData.put("row_number",0);
        }else{
            todayPersistenceData = list.get(0);
        }
        return todayPersistenceData;
    }

    /**
     * @Author Huang.zh
     * @Description 统计各目录下所有表的数据总数
     * @Date 2020/9/18 10:05
     */
    private List<JSONObject> countResourceDataRows(){
        Map<String, List<BusDataTable>> tableMap = schemaDao.getBusDataTable(new BusDataTable())
                .stream().filter(busDataTable -> StringUtils.isNotBlank(busDataTable.getResourceId())).collect(Collectors.groupingBy(BusDataTable::getResourceId));
        Map<String, List<BusDataResource>> resourceMap = busDataResourceDao.findBusDataSource(new BusDataResource())
                .stream().collect(Collectors.groupingBy(BusDataResource::getId));
        Map<String, List<BusDataSource>> dataSourceMap = schemaDao.getBusDataSource(new BusDataSource())
                .stream().collect(Collectors.groupingBy(BusDataSource::getId));
        List<JSONObject> result = new ArrayList<>();
        tableMap.forEach((resourceId,tables)->{
            JSONObject object = new JSONObject();
            List<BusDataResource> busDataResources = resourceMap.get(resourceId);
            if (busDataResources != null){
                String resourceName = busDataResources.get(0).getName();
                List<String> nameList = tables.stream().map(BusDataTable::getTableName).collect(Collectors.toList());
                //所有被归类为当前目录的表都在同一个数据源，直接根据数据源的名称获得对应的jdbcTemplate
                String dataBaseName = dataSourceMap.get(tables.get(0).getDataSourceId()).get(0).getDataBaseName();
                JdbcTemplate customJDBCTemplate = register.getCustomJDBCTemplate(dataBaseName);
                String sql = concatCountTableDataRowsSql(nameList);
                List<Integer> list = customJDBCTemplate.queryForList(sql, Integer.class);
                int rows = list.stream().mapToInt(Integer::intValue).sum();
                object.put("rows",rows);
                object.put("resourceName",resourceName);
                result.add(object);
            }
        });
        return result;
    }

    /**
     * @Author Huang.zh
     * @Description 组装获得指定表数据行数的sql
     * @Date 2020/9/18 15:47
     */
    private static String concatCountTableDataRowsSql(List<String> tableNames){
        StringBuffer buffer = new StringBuffer();
        for (String tableName : tableNames) {
            String sql = com.ygkj.soft.database.constant.Constants.SELECT_COUNT.concat(tableName)
                    .concat(tableNames.indexOf(tableName) == tableNames.size() -1 ?"": com.ygkj.soft.database.constant.Constants.UNION);
            buffer.append(sql);
        }
        return buffer.toString();
    }

    /**
     * @Author Huang.zh
     * @Description 统计整个数据仓的数据量
     * @Date 2020/9/18 9:55
     */
    private int allDataRows()  {
        int total = 0;
        try {
            List<BusDataSource> dataSources = schemaDao.getBusDataSource(new BusDataSource());
            List<Future<Integer>> list = new ArrayList<>();
            for (BusDataSource dataSource : dataSources) {
                SynchronizeTask task = new SynchronizeTask(dataSource);
                Future<Integer> future = executor.submit(task);
                list.add(future);
            }
            for (Future<Integer> future : list) {
                total += future.get().intValue();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return total;
    }


    private class SynchronizeTask implements Callable<Integer>{

        private BusDataSource busDataSource;

        public SynchronizeTask(BusDataSource busDataSource) {
            this.busDataSource = busDataSource;
        }

        @Override
        public Integer call() throws Exception {
            String dataBaseName = busDataSource.getDataBaseName();
            JdbcTemplate customJDBCTemplate = register.getCustomJDBCTemplate(dataBaseName);
            String sql = "select table_schema, table_name,table_rows\n" +
                    "from information_schema.tables\n" +
                    "where table_schema=?";
//            List<JSONObject> list = customJDBCTemplate.query(sql, jsonRowMapper, dataBaseName);
            List<JSONObject> list = customJDBCTemplate.query(sql, jsonRowMapper, StringUtils.isBlank(prefix)?dataBaseName:
                    dataBaseName.replaceFirst("[a-zA-Z]+_",prefix));
            int totalRows = 0;
            for (JSONObject jsonObject : list) {
                totalRows += jsonObject.getIntValue("table_rows");
            }
            return totalRows;
        }
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        executor = new ThreadPoolExecutor(Runtime.getRuntime().availableProcessors()*2,20,60, TimeUnit.SECONDS
                ,new ArrayBlockingQueue<>(10),synchronizeThreadProduceFactory);
    }

    @Bean
    @Lazy
    public SynchronizeThreadProduceFactory synchronizeThreadProduceFactory(){
        return new SynchronizeThreadProduceFactory();
    }

    /**
     * 自定义线程生产工厂
     */
    public class SynchronizeThreadProduceFactory implements ThreadFactory {

        private AtomicInteger index = new AtomicInteger(1);

        @Override
        public Thread newThread(Runnable runnable) {
            Thread thread = new Thread(runnable, SoftDataSynchronizeTask.Constants.SYNCHRONIZED_THREAD_PREFIX+ index.get());
            index = new AtomicInteger(index.addAndGet(1));
            return thread;
        }
    }

    private class Constants{
        static final String SYNCHRONIZED_THREAD_PREFIX = "soft_data_synchronize_thread_";
    }
}
