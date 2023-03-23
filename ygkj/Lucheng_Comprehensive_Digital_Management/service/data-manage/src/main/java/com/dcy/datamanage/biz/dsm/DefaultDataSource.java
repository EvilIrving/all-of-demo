package com.dcy.datamanage.biz.dsm;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidPooledConnection;
import com.dcy.datamanage.api.model.BusDataSource;
import com.dcy.datamanage.api.model.DataAssessExtendMsg;
import com.dcy.datamanage.api.model.common.PageModel;
import com.dcy.datamanage.api.model.common.PageResult;
import com.dcy.datamanage.biz.dto.input.*;
import com.dcy.datamanage.biz.dto.output.BusDataColumnOutputDTO;
import com.dcy.datamanage.biz.dto.output.BusDataTableOutputDTO;
import com.dcy.datamanage.biz.util.DateUtil;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.util.CollectionUtils;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;


public class DefaultDataSource extends DruidDataSource {

    protected JdbcTemplate jdbcTemplate;

    protected DataSourceTransactionManager dataSourceTransactionManager;

    protected String dataSourceId;

    protected String catalog;

    protected String schema;

    public DefaultDataSource(BusDataSource busDataSource) {
        this.dataSourceId = busDataSource.getId();
        this.jdbcUrl = busDataSource.getDataBaseAddress();
        this.username = busDataSource.getAccountNumber();
        this.password = busDataSource.getAccountPwd();
        this.jdbcTemplate = new JdbcTemplate(this);
        this.dataSourceTransactionManager = new DataSourceTransactionManager(this);
        this.maxWait = 3000;
        this.setKeepAlive(true);
        addConnectionProperty("remarks", "true");
    }

    /**
     * 子类务必重写！！！！！(或者搞一套通用的)
     *
     * @param sql
     * @param total
     * @param current
     * @param size
     * @return
     */
    protected String getPageSql(String sql, long total, long current, long size) {
        return sql;
    }

    /**
     * 子类务必重写！！！！！(或者搞一套通用的)
     *
     * @return
     */
    public List<DataAssessExtendMsg> listLastUpdateTime(List<String> tableNameList) {
        return null;
    }

    /**
     * 数据处理 (完善或者子类重写)
     *
     * @param col
     * @param val
     * @param paramList
     * @param valBuilder
     */
    protected void dealWithVal(BusDataColumnOutputDTO col, Object val, List<Object> paramList, StringBuilder valBuilder) {
        valBuilder.append("?");
        if (val != null) {
            Integer dataType = col.getDataType();
            //时间类型单独处理
            switch (dataType) {
                case Types.DATE:
                    val = DateUtil.strToDate((String) val, DateUtil.SDF_DATE);
                    break;
                case Types.TIME:
                    val = DateUtil.strToDate((String) val, DateUtil.SDF_TIME);
                    break;
                case Types.TIMESTAMP:
                    val = DateUtil.strToDate((String) val, DateUtil.SDF_DATETIME);
                    break;
                default:
            }
        }
        paramList.add(val);
    }

    /**
     * 格式化返回值(完善或者子类重写)
     *
     * @param list
     */
    protected void formatVal(List<Map<String, Object>> list) {

        if (!CollectionUtils.isEmpty(list)){
            list.forEach(map ->
                    map.forEach((k, v) -> {
                        if (v != null) {
                            Class clazz = v.getClass();
                            if (java.sql.Date.class == clazz) {
                                map.put(k, DateUtil.dateToStr((java.sql.Date) v, DateUtil.SDF_DATE));
                            } else if (LocalDateTime.class == clazz) {
                                map.put(k, DateUtil.dateToStr((LocalDateTime) v, DateUtil.DTF_DATETIME));
                            } else if (Timestamp.class == clazz) {
                                map.put(k, DateUtil.dateToStr((Timestamp) v, DateUtil.SDF_DATETIME));
                            } else if (java.sql.Time.class == clazz) {
                                map.put(k, DateUtil.dateToStr((java.sql.Time) v, DateUtil.SDF_TIME));
                            }
                        }
                    })
            );
        }
    }

    /**
     * 查询原始表列表
     *
     * @return
     */
    public PageResult<BusDataTableOutputDTO> listTableSource(PageModel pageModel) {
        PageResult pageResult = new PageResult();
        try(DruidPooledConnection connection = getConnection()) {
            DatabaseMetaData metaData = connection.getMetaData();
            ResultSet tables = metaData.getTables(catalog, schema, null, new String[]{"TABLE"});
            List<BusDataTableOutputDTO> list = new ArrayList<>();
            while (tables.next()) {
                BusDataTableOutputDTO outputDTO = new BusDataTableOutputDTO();
                outputDTO.setTableName(tables.getString("TABLE_NAME"));
                outputDTO.setRemarks(tables.getString("REMARKS"));
                outputDTO.setDataSourceId(dataSourceId);
                list.add(outputDTO);
            }
            pageResult.setRecords(list);
            pageResult.setTotal(list.size());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return pageResult;
    }

    /**
     * 查询原始列信息
     */
    public List<BusDataColumnOutputDTO> listColumnSource(BusDataColumnSearchInputDTO dto) {
        List<BusDataColumnOutputDTO> list = new ArrayList<>();
        try (DruidPooledConnection connection = getConnection()){
            DatabaseMetaData metaData = connection.getMetaData();
            String tableName = dto.getTableName();
            ResultSet columns = metaData.getColumns(catalog, schema, tableName, null);
            ResultSet primaryKeys = metaData.getPrimaryKeys(catalog, schema, tableName);
            Set<String> primaryKeySet = new HashSet<>();
            while (primaryKeys.next()) {
                primaryKeySet.add(primaryKeys.getString("COLUMN_NAME"));
            }
            while (columns.next()) {
                BusDataColumnOutputDTO outputDTO = new BusDataColumnOutputDTO();
                String columnName = columns.getString("COLUMN_NAME");
                outputDTO.setPrimary(primaryKeySet.contains(columnName) ? 1 : 0);
                outputDTO.setColumnName(columnName);
                outputDTO.setDataSourceId(dataSourceId);
                outputDTO.setTableName(tableName);
                outputDTO.setTypeName(columns.getString("TYPE_NAME"));
                outputDTO.setColumnSize(columns.getLong("COLUMN_SIZE"));
                outputDTO.setDecimalDigits(columns.getLong("DECIMAL_DIGITS"));
                outputDTO.setNullable(columns.getInt("NULLABLE"));
                outputDTO.setRemarks(columns.getString("REMARKS"));
                outputDTO.setOrdinalPosition(columns.getInt("ORDINAL_POSITION"));
                outputDTO.setDataType(columns.getInt("DATA_TYPE"));
                list.add(outputDTO);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    public DataSourceTransactionManager getDataSourceTransactionManager() {
        return dataSourceTransactionManager;
    }

    /**
     * 查询数据
     *
     * @param dto
     * @param pageModel
     * @return
     */
    public PageResult<Map<String, Object>> page(DataSearchInputDTO dto, PageModel pageModel) {
        PageResult<Map<String, Object>> pageResult = new PageResult();
        String sql = "SELECT * FROM " + dto.getTableName();
        List<Map<String, Object>> list = null;
        if (pageModel.isPage()) {
            long size = pageModel.getSize();
            long current = pageModel.getCurrent();
            //计算 起始数字和展示条数
            pageResult.setCurrent(current);
            pageResult.setSize(size);
            //分页的话首先查询总数
            String countSql = getCountSql(sql);
            Long total = jdbcTemplate.queryForObject(countSql, Long.class);
            pageResult.setTotal(total);
            //数量大于零的话执行查询
            if (total > 0) {
                long pages = total / size;
                if (total % size != 0L) {
                    ++pages;
                }
                if (getClass() == DefaultDataSource.class) {
                    long fromIndex = (current - 1) * size;
                    //页码太大直接返回
                    if (fromIndex >= total) {
                        return pageResult;
                    }
                    long toIndex = current * size;
                    toIndex = toIndex > total ? total : toIndex;
                    //未实现分页方式的话采用逻辑分页
                    list = jdbcTemplate.queryForList(sql);
                    list = list.subList((int) fromIndex, (int) toIndex);
                } else {
                    //实现分页方法的话实行物理分页
                    pageResult.setPages(pages);
                    sql = getPageSql(sql, total, current, size);
                    list = jdbcTemplate.queryForList(sql);
                }
            }
        } else {
            //不分页就直接用sql查询,未实现分页的
            list = jdbcTemplate.queryForList(sql);
            pageResult.setTotal(list == null ? 0 : list.size());
        }
        //处理返回值
        formatVal(list);
        pageResult.setRecords(list);

        return pageResult;
    }

    protected String getCountSql(String sql) {
        return "SELECT COUNT(1) FROM (" + sql + ") a";
    }

    public String save(DataSaveInputDTO dto) {
        Map<String, Object> data = dto.getData();
        List<Object> paramList = new ArrayList<>();
        StringBuilder sb = new StringBuilder("INSERT INTO ");
        sb.append(dto.getTableName());
        sb.append(" (");
        List<BusDataColumnOutputDTO> colList = getColList(dto);
        StringBuilder valBuilder = new StringBuilder();
        for (BusDataColumnOutputDTO col : colList) {
            String colName = col.getColumnName();
            sb.append(colName).append(",");
            dealWithVal(col, data.get(colName), paramList, valBuilder);
            valBuilder.append(",");
        }
        sb.deleteCharAt(sb.length() - 1);
        valBuilder.deleteCharAt(valBuilder.length() - 1);
        sb.append(") VALUES (");
        sb.append(valBuilder);
        sb.append(")");
        try {
            jdbcTemplate.update(sb.toString(), paramList.toArray());
            return null;
        } catch (Exception e) {
            return e.getCause().getMessage();
        }
    }

    public void del(DataDelInputDTO dto) {
        //删除数据
        List<BusDataColumnOutputDTO> colList = getColList(dto);
        StringBuilder sb = new StringBuilder("DELETE FROM ").append(dto.getTableName());
        Map<String, Object> data = dto.getData();
        List<Object> paramList = new ArrayList<>();
        appendConditionByKeys(sb, data, colList, paramList);
        jdbcTemplate.update(sb.toString(), paramList.toArray());
    }

    public List<BusDataColumnOutputDTO> getColList(DataEditInputDTO dto) {
        List<BusDataColumnOutputDTO> colList = dto.getColList();
        if (CollectionUtils.isEmpty(colList)) {
            BusDataColumnSearchInputDTO searchInputDTO = new BusDataColumnSearchInputDTO();
            searchInputDTO.setTableName(dto.getTableName());
            colList = listColumnSource(searchInputDTO);
        }
        return colList;
    }

    protected void appendConditionByKeys(StringBuilder sb, Map<String, Object> data, List<BusDataColumnOutputDTO> colList, List<Object> paramList) {
        List<BusDataColumnOutputDTO> primaryList = colList.stream().filter(bus -> bus.getPrimary() == 1).collect(Collectors.toList());
        //有主键的话按主键来
        if (!CollectionUtils.isEmpty(primaryList)) {
            colList = primaryList;
        }
        sb.append(" WHERE ");
        for (int i = 0; i < colList.size(); i++) {
            BusDataColumnOutputDTO dto = colList.get(i);
            String colName = dto.getColumnName();
            Object val = data.get(colName);
            if (i > 0) {
                sb.append(" AND ");
            }
            sb.append(colName);
            if (val == null) {
                sb.append(" IS NULL ");
            } else {
                sb.append("=");
                dealWithVal(dto, val, paramList, sb);
            }
        }
    }

    public void update(DataUpdateInputDTO dto) {
        StringBuilder sb = new StringBuilder("UPDATE ");
        sb.append(dto.getTableName()).append(" SET ");
        List<BusDataColumnOutputDTO> colList = getColList(dto);
        Map<String, Object> data = dto.getData();
        List<Object> paramList = new ArrayList<>();
        for (BusDataColumnOutputDTO busDataColumnOutputDTO : colList) {
            String colName = busDataColumnOutputDTO.getColumnName();
            sb.append(colName).append("=");
            dealWithVal(busDataColumnOutputDTO, data.get(colName), paramList, sb);
            sb.append(",");
        }
        sb.deleteCharAt(sb.length() - 1);
        appendConditionByKeys(sb, dto.getOldData(), colList, paramList);
        jdbcTemplate.update(sb.toString(), paramList.toArray());
    }
}
