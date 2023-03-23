package com.dcy.datamanage.biz.dsm.datasource;

import com.dcy.datamanage.api.model.BusDataSource;
import com.dcy.datamanage.biz.dsm.DefaultDataSource;

public class DmDataSource extends DefaultDataSource {

    public DmDataSource(BusDataSource busDataSource) {
        super(busDataSource);
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
    @Override
    protected String getPageSql(String sql, long total, long current, long size) {
        long fromIndex = (current - 1) * size;
        return sql + " LIMIT " + fromIndex + "," + size;
    }
}
