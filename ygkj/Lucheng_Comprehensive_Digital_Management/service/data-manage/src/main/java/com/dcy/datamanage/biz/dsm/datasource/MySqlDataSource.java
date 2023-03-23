package com.dcy.datamanage.biz.dsm.datasource;

import com.dcy.datamanage.api.model.BusDataSource;
import com.dcy.datamanage.api.model.DataAssessExtendMsg;
import com.dcy.datamanage.biz.dsm.DefaultDataSource;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MySqlDataSource extends DefaultDataSource {

    private NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);

    private final static String UPDATE_SQL = "SELECT TABLE_NAME,IFNULL(UPDATE_TIME,CREATE_TIME) update_time FROM information_schema.`TABLES` " +
            "WHERE TABLE_SCHEMA = :tableSchema AND TABLE_NAME IN (:tableName)";
    private BeanPropertyRowMapper extendMsgBean = new BeanPropertyRowMapper<>(DataAssessExtendMsg.class);

    public MySqlDataSource(BusDataSource busDataSource) {
        super(busDataSource);
        int start = jdbcUrl.lastIndexOf("/");
        if (start != -1) {
            int end = jdbcUrl.indexOf("?");
            end = end == -1 ? jdbcUrl.length() : end;
            catalog = jdbcUrl.substring(start + 1, end);
        }
        driverClass = "com.mysql.cj.jdbc.Driver";
        addConnectionProperty("useInformationSchema", "true");
    }

    @Override
    protected String getPageSql(String sql, long total, long current, long size) {
        long fromIndex = (current - 1) * size;
        return sql + " LIMIT " + fromIndex + "," + size;
    }

    @Override
    public List<DataAssessExtendMsg> listLastUpdateTime(List<String> tableNameList) {
        Map<String, Object> param = new HashMap<>();
        param.put("tableSchema", catalog);
        param.put("tableName", tableNameList);
        return namedParameterJdbcTemplate.query(UPDATE_SQL, param, extendMsgBean);
    }

}
