package com.dcy.datamanage.biz.dsm.datasource;

import com.dcy.datamanage.api.model.BusDataSource;
import com.dcy.datamanage.biz.dsm.DefaultDataSource;

public class OracleDataSource extends DefaultDataSource {

    public OracleDataSource(BusDataSource busDataSource) {
        super(busDataSource);
        schema = username.toUpperCase();
        driverClass = "oracle.jdbc.driver.OracleDriver";
    }

    /**
     * 子类务必重写！！！！！
     *
     * @param sql
     * @param total
     * @param current
     * @param size
     * @return
     */
    @Override
    protected String getPageSql(String sql, long total, long current, long size) {
        sql = "SELECT page_temp_2.* FROM (SELECT page_temp_1.*,rownum as \"_NAV_ORDER_F_\" FROM (" +
                sql +
                ") page_temp_1 ) page_temp_2 WHERE page_temp_2.\"_NAV_ORDER_F_\" BETWEEN " +
                ((current - 1) * size + 1) + " AND " + (current * size);
        return sql;
    }

}
