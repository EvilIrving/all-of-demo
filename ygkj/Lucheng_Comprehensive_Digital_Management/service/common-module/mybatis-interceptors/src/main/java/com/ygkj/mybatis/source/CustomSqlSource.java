package com.ygkj.mybatis.source;

import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.SqlSource;

public class CustomSqlSource implements SqlSource {

    private BoundSql boundSql;

    public CustomSqlSource(BoundSql boundSql) {
        this.boundSql = boundSql;
    }

    @Override
    public BoundSql getBoundSql(Object o) {
        return boundSql;
    }
}
