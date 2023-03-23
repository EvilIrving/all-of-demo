package com.ygkj.soft.database.dao;

import com.ygkj.soft.database.constant.Constants;
import com.ygkj.soft.database.constant.FormComponents;
import com.ygkj.soft.database.constant.SchemaTypes;
import com.ygkj.soft.database.service.impl.DynamicDataSourceJDBCTemplateRegister;
import com.ygkj.soft.database.api.VO.BusDataTablePageQueryVo;
import com.ygkj.soft.database.api.VO.BusDataTableStatusChangeVo;
import com.ygkj.soft.database.api.model.BusDataColumn;
import com.ygkj.soft.database.api.model.BusDataSource;
import com.ygkj.soft.database.api.model.BusDataTable;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;
import ygkj.com.assistant.pager.Pager;
import ygkj.com.assistant.utils.MysqlPageUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author Huang.zh
 * @date 2020/9/7 17:31
 * @Description: 元数据持久层
 */
@Repository("/schemaDao")
public class SchemaDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Value("${schema.prefix}")
    private String prefix;

    @Autowired
    private DynamicDataSourceJDBCTemplateRegister register;

    /**
     * 数据源查询
     * @param dataSource
     * @return
     */
    public List<BusDataSource> getBusDataSource(BusDataSource dataSource){
        String sql = "SELECT * FROM bus_data_source WHERE deleted = 1";
        List<Object> list = new ArrayList<>();
        if (StringUtils.isNotBlank(dataSource.getId())){
            sql+=" AND id = ? ";
            list.add(dataSource.getId());
        }
        if (StringUtils.isNotBlank(dataSource.getDataBaseName())){
            sql+=" AND data_base_name = ? ";
            list.add(dataSource.getDataBaseName());
        }
        if (StringUtils.isNotBlank(dataSource.getDeptId())){
            sql+=" AND dept_id = ? ";
            list.add(dataSource.getDeptId());
        }
        RowMapper<BusDataSource> mapper = new BeanPropertyRowMapper<>(BusDataSource.class);
        return jdbcTemplate.query(sql,list.toArray(),mapper);
    }

    /**
     * @Author Huang.zh
     * @Description 数据源所含表查询
     * @Date 2020/9/8 16:16
     */
    public List<BusDataTable> getBusDataTable(BusDataTable dataTable){
        String sql = "SELECT * FROM bus_data_table WHERE 1 = 1";
        List<Object> list = new ArrayList<>();
        if (StringUtils.isNotBlank(dataTable.getId())){
            sql+=" AND id = ? ";
            list.add(dataTable.getId());
        }
        if (StringUtils.isNotBlank(dataTable.getDataSourceId())){
            sql+=" AND FIND_IN_SET(data_source_id,?) ";
            list.add(dataTable.getDataSourceId());
        }
        if (StringUtils.isNotBlank(dataTable.getTableName())){
            sql+=" AND table_name = ? ";
            list.add(dataTable.getTableName());
        }
        if (StringUtils.isNotBlank(dataTable.getCategory())){
            sql+=" AND category = ? ";
            list.add(dataTable.getCategory());
        }
        RowMapper<BusDataTable> mapper = new BeanPropertyRowMapper<>(BusDataTable.class);
        return jdbcTemplate.query(sql,list.toArray(),mapper);
    }

    /**
     * @Author Huang.zh
     * @Description 表中所含字段查询
     * @Date 2020/9/8 16:17
     */
    public List<BusDataColumn> getBusDataColumn(BusDataColumn busDataColumn){
        String sql = "SELECT * FROM bus_data_column WHERE 1 = 1";
        List<Object> list = new ArrayList<>();
        if (StringUtils.isNotBlank(busDataColumn.getTableId())){
            sql+=" AND FIND_IN_SET(table_id,?) ";
            list.add(busDataColumn.getTableId());
        }
        sql += " ORDER BY order_column ";
        RowMapper<BusDataColumn> mapper = new BeanPropertyRowMapper<>(BusDataColumn.class);
        return jdbcTemplate.query(sql,list.toArray(),mapper);
    }


    public List<BusDataColumn> getSoftTableColumn(String tableName){
        String sql ="select column_name as name,IF(is_nullable = 'YES',1,0) AS nullable,column_type,column_default,column_comment AS remarks\n" +
                " from information_schema.columns where table_schema='soft-database' and table_name=?";
        RowMapper<BusDataColumn> mapper = new BeanPropertyRowMapper<>(BusDataColumn.class);
        return jdbcTemplate.query(sql,mapper,tableName);
    }

    /**
     * @Author Huang.zh
     * @Description 数据源配置信息持久化
     * @Date 2020/9/7 21:44
     */
    public int addDataSource(BusDataSource busDataSource){
        String sql = "insert into bus_data_source  " +
                "(id,data_base_name,data_base_address,server_name,port,db_name,data_base_type,account_number,account_pwd," +
                "remarks,checked,init,belong_system,data_nature,data_from,dept_id) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,? )";
        return jdbcTemplate.update(sql,
                busDataSource.getId(),busDataSource.getDataBaseName(),busDataSource.getDataBaseAddress(),
                busDataSource.getServerName(),busDataSource.getPort(),busDataSource.getDbName(),busDataSource.getDataBaseType()
                ,busDataSource.getAccountNumber(),busDataSource.getAccountPwd(),busDataSource.getRemarks()
                ,busDataSource.getChecked(),busDataSource.isInit(),busDataSource.getBelongSystem()
                ,busDataSource.getDataNature(),busDataSource.getDataFrom(),busDataSource.getDeptId());
    }

    public int setDataSourceInitStatus(BusDataSource dataSource){
        String sql = "UPDATE bus_data_source SET init = ? WHERE id = ? ";
        return jdbcTemplate.update(sql,dataSource.isInit(),dataSource.getId());
    }

    public void addBusDataTable(List<Object[]> params) throws Exception{
        jdbcTemplate.batchUpdate("insert into bus_data_table  (id,data_source_id,table_name,remarks,sort,update_at,rows_num ) " +
                        "values (?,?,?,?,?,?,? )", params);
    }

    public void addBusDataColumn(List<Object[]> params) throws Exception{
        jdbcTemplate.batchUpdate("insert into bus_data_column  (id,table_id,name,nullable,column_type,column_default,remarks" +
                ",column_length,editable,form_component,order_column ) " +
                "values (?,?,?,?,?,?,?,?,?,?,? )",params);
    }

    /**
     * @Author Huang.zh
     * @Description 从配置的数据源获取数据源中的表名和备注，该方法原则上只能在初始化数据源的时候被调用
     * @Date 2020/9/8 9:58
     */
    public List<BusDataTable> getSchemaTables(BusDataSource dataSource){
        Assert.notNull(dataSource.getDataBaseName(),"请指定一个数据源！");
        JdbcTemplate targetTemplate = register.getCustomJDBCTemplate(dataSource.getDataBaseName());
        if (targetTemplate == null){
            throw new RuntimeException("切换数据源失败！请联系管理员");
        }
        RowMapper<BusDataTable> mapper = new BeanPropertyRowMapper<>(BusDataTable.class);
        String sql = "select table_name,table_comment AS remarks from information_schema.tables where table_schema=?";
//        List<BusDataTable> list = targetTemplate.query(sql, mapper,dataSource.getDataBaseName());
        List<BusDataTable> list = targetTemplate.query(sql, mapper,StringUtils.isBlank(prefix)?dataSource.getDataBaseName():
                dataSource.getDataBaseName().replaceFirst("[a-zA-Z]+_",prefix));
        for (BusDataTable table : list) {
            table.setId(UUID.randomUUID().toString());
            table.setDataSourceId(dataSource.getId());
        }
        return list;
    }

    /**
     * @Author Huang.zh
     * @Description 获取表中的字段，该方法原则上只能在初始化过程中被调用
     * @Date 2020/9/8 10:12
     */
    public List<BusDataColumn> getSchemaTableColumns(BusDataSource dataSource,BusDataTable table){
        JdbcTemplate targetTemplate = register.getCustomJDBCTemplate(dataSource.getDataBaseName());
        String sql ="select column_name as name,IF(is_nullable = 'YES',1,0) AS nullable,data_type AS column_type,column_default,character_maximum_length AS column_length," +
                "column_comment AS remarks\n" +
                " from information_schema.columns where table_schema=? and table_name=?";
        RowMapper<BusDataColumn> mapper = new BeanPropertyRowMapper<>(BusDataColumn.class);
        List<BusDataColumn> columnList = new ArrayList<>();
//        List<BusDataColumn> columnList = targetTemplate.query(sql, mapper, dataSource.getDataBaseName(), table.getTableName());
        try {
            columnList = targetTemplate.query(sql, mapper, StringUtils.isBlank(prefix)?dataSource.getDataBaseName():
                    dataSource.getDataBaseName().replaceFirst("[a-zA-Z]+_",prefix), table.getTableName());
            for (BusDataColumn column : columnList) {
                column = judgeColumns(column);
                column.setId(UUID.randomUUID().toString());
                column.setTableId(table.getId());
            }

        }catch (Exception e){
            System.out.println("-------------------"+table.getTableName()+"-------------------");
            e.printStackTrace();
        }
        return columnList;
    }

    /**
     * @Author Huang.zh
     * @Description 判断字段是否允许修改、对应表单控件等属性
     * @Date 2020/9/12 13:51
     */
    private BusDataColumn judgeColumns(BusDataColumn column){
        //主键和关联项字段不允许修改
        if (column.getName().equals(Constants.DEFAULT_PRIMARY_KEY) || column.getName().endsWith(Constants.ASSOCIATED_ITEM_SUFFIX)){
            column.setEditable(false);
        }else{
            column.setEditable(true);
        }
        //根据数据库字段类型确定对应表单控件
        String formComponent;
        String type = column.getColumnType();
        switch (type){
            case SchemaTypes.VARCHAR:
                formComponent = FormComponents.INPUT;
                break;
            case SchemaTypes.BIT:
            case SchemaTypes.TINYINT:
                formComponent = FormComponents.SELECT;
                break;
            case SchemaTypes.DATE:
                formComponent = FormComponents.DATE;
                break;
            case SchemaTypes.DATETIME:
                formComponent = FormComponents.DATETIME;
                break;
            case SchemaTypes.ENUM:
                formComponent = FormComponents.RADIO;
                break;
            default:
                formComponent = FormComponents.INPUT;
                break;
        }
        column.setFormComponent(formComponent);
        return column;
    }

    /**
     * @Author Huang.zh
     * @Description 中间库创建表，该方法原则上只能在初始化过程中调用
     * @Date 2020/9/8 13:37
     */
    public void createSoftTable(List<String> tableNames) throws Exception{
        for (String tableName : tableNames) {
            tableName = tableName.replaceAll("dts_yh_basic_data_db_","")
                    .replaceAll("dts_yh_business_data_db_","");
            String sql = "CREATE TABLE `"+tableName+"` (\n" +
                    "  `global_id` varchar(64) NOT NULL COMMENT '全局id，代码生成',\n" +
                    "  `id` varchar(64) DEFAULT NULL COMMENT '对应业务库的id',\n" +
                    "  `data` text COMMENT '数据json串',\n" +
                    "  `version` int(11) DEFAULT 0 COMMENT '数据版本号',\n" +
                    "  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '数据操作时间',\n" +
                    "  `operation_type` int(1) DEFAULT NULL COMMENT '操作类型，1新增 2修改 3删除',\n " +
                    " `status` int(11) DEFAULT 0 COMMENT '数据状态 1审核通过并推送 2拒绝推送 0未审核',\n" +
                    " `data_base_name` varchar(255) NOT NULL COMMENT '数据库名称',\n" +
                    " `table_name` varchar(255) NOT NULL COMMENT '表名',\n" +
                    "  `reason` varchar(2000) DEFAULT NULL COMMENT '审核理由',\n" +
                    "  PRIMARY KEY (`global_id`),\n" +
                    "  UNIQUE KEY `id_version_unique_index` (`id`,`version`) USING BTREE,\n" +
                    "  KEY `global_id_index` (`global_id`) USING BTREE\n" +
                    ") ENGINE=InnoDB DEFAULT CHARSET=utf8;";
            jdbcTemplate.execute(sql);
        }
    }

    /**
     * @Author Huang.zh
     * @Description 查询中间库中已经存在的表
     * @Date 2020/9/10 13:37
     */
    public List<String> getSchemaExistTables(){
        String sql = "select table_name from information_schema.tables where table_schema='soft-database'";
        return jdbcTemplate.queryForList(sql,String.class);
    }

    public void addBuaDataTable(BusDataTable table) throws Exception{
        String sql = "INSERT INTO bus_data_table (id,table_name,data_source_id,remarks,resource_id) VALUES (?,?,?,?,?)";
        jdbcTemplate.update(sql,table.getId(),table.getTableName(),table.getDataSourceId(),table.getRemarks(),table.getResourceId());
    }

    public void updateBuaDataTable(BusDataTable table) throws Exception{
        String sql = "UPDATE bus_data_table SET table_name = ?,remarks = ? WHERE id = ?";
        jdbcTemplate.update(sql,table.getTableName(),table.getRemarks(),table.getId());
    }

    public void changeTableStatus(BusDataTableStatusChangeVo vo){
        String sql = "UPDATE bus_data_table SET status = "+vo.getStatus()+" WHERE id = ? ";
        jdbcTemplate.update(sql,vo.getId());
    }

    public Pager getBusDataTableByPaginate(BusDataTablePageQueryVo vo){
        String sql = "SELECT * FROM bus_data_table WHERE 1=1 ";
        List<Object> params = new ArrayList<>();

        if (StringUtils.isNotBlank(vo.getId())){
            sql += " AND id = ? ";
            params.add(vo.getId());
        }
        if (StringUtils.isNotBlank(vo.getRemarks())){
            sql += " AND remarks LIKE ? ";
            params.add("%"+vo.getRemarks()+"%");
        }
        if(StringUtils.isNotBlank(vo.getDataSourceId())){
            sql += " AND (data_source_id = ? AND resource_id IS NULL )";
            params.add(vo.getDataSourceId());
        }
        if (StringUtils.isNotBlank(vo.getResourceId())){
            sql += " AND FIND_IN_SET(?,resource_id)";
            params.add(vo.getResourceId());
        }

        // 查总数
        int total = jdbcTemplate.queryForObject(MysqlPageUtil.getCountSql(sql),params.toArray(),Integer.class);
        // 分页，pager为空就不分页
        if(null!=vo  && !vo.isFetchAll()){
            int start = MysqlPageUtil.getStart(vo.getPage(), vo.getRows());
            sql = MysqlPageUtil.pageSqlForMysql(sql, start, vo.getRows());
        }
        Pager pager = new Pager();
        List<BusDataTable> list = jdbcTemplate.query(sql, params.toArray(), new BeanPropertyRowMapper<>(BusDataTable.class));
        pager.setItemsTotal(total);
        pager.setList(list);
        return pager;
    }

}
