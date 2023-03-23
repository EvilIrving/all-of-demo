package com.ygkj.soft.database.util;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

/**
 * <B>系统名称：</B>通用系统功能<BR>
 * <B>模块名称：</B>数据访问通用功能<BR>
 * <B>中文类名：</B>JSON数据行映射器<BR>
 * <B>概要说明：</B><BR>
 * 
 * @author dlm
 * @since 2016-5-30
 */
@Component
public class JsonRowMapper implements RowMapper<JSONObject> {

	private boolean transferCamel =false;	// 是否将key转换为驼峰命名
	
    /**
     * <B>方法名称：</B>映射行数据<BR>
     * <B>概要说明：</B><BR>
     * 
     * @param rs 结果集
     * @param row 行号
     * @return JSONObject 数据
     * @throws SQLException SQL异常错误
     * @see RowMapper#mapRow(ResultSet,
     *      int)
     */
    public JSONObject mapRow(ResultSet rs, int row) throws SQLException {
        String key = null;
        Object obj = null;
        JSONObject json = new JSONObject();
        ResultSetMetaData rsmd = rs.getMetaData();
        int count = rsmd.getColumnCount();
        for (int i = 1; i <= count; i++) {
            key = JdbcUtils.lookupColumnName(rsmd, i);
            obj = JdbcUtils.getResultSetValue(rs, i);
            try {
                json.put(key, obj);
            }
            catch (JSONException e) {
            }
        }
        return json;
    }

	public boolean isTransferCamel() {
		return transferCamel;
	}

	public void setTransferCamel(boolean transferCamel) {
		this.transferCamel = transferCamel;
	}
}
