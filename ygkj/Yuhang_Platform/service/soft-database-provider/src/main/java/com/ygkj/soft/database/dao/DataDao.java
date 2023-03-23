package com.ygkj.soft.database.dao;

import com.alibaba.fastjson.JSONObject;
import com.google.common.base.CaseFormat;
import com.ygkj.soft.database.constant.Constants;
import com.ygkj.soft.database.util.JsonRowMapper;
import org.bouncycastle.crypto.modes.GOFBBlockCipher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author Huang.zh
 * @date 2020/9/1 16:07
 * @Description: 数据服务持久层
 */
@Repository("dataDao")
public class DataDao {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private JsonRowMapper jsonRowMapper;


    public List<JSONObject> queryByCondition(Map<String,Object> params,
                                 @NotNull(message = "tableName不允许为空！") String tableName){
        return queryByCondition(params, tableName,null);
    }


    /**
     * @Author Huang.zh
     * @Description 条件查询，动态拼接表名和条件字段
     * @Date 2020/9/1 16:37
     */
    public List<JSONObject> queryByCondition(Map<String,Object> params,
                                             @NotNull(message = "tableName不允许为空！") String tableName,
                                             String limit){
        String sql =  concatQuerySql(tableName,params)
                .concat(" ORDER BY update_time DESC,version DESC ")
                .concat(limit == null?"":limit);
        List<JSONObject> list;
        try {
            list = jdbcTemplate.query(sql, params.values().toArray(),jsonRowMapper);
        }catch (Exception e){
            list = new ArrayList<>();
        }
        return list;
    }

    /**
     * @Author Huang.zh
     * @Description 向指定表插入记录，只包含params参数中所有的字段
     * @Date 2020/9/2 9:26
     */
    public int saveData(String tableName,Map<String,Object> params){
        String sql = concatInsertSql(tableName, params,true);
        try {
            return jdbcTemplate.update(sql, params.values().toArray());
        }catch (Exception e){
            e.printStackTrace();
            return 0;
        }
    }


    /**
     * @Author Huang.zh
     * @Description 修改数据的状态
     * @Date 2020/9/2 9:45
     */
    public int changeStatus(String tableName,String globalId,int status){
        Map<String,Object> params = new HashMap<>();
        params.put("status",String.valueOf(status));
        String sql = concatUpdateSql(tableName, globalId,"",params,true);
        int updateRows = jdbcTemplate.update(sql, status);
        return updateRows;
    }

    /**
     * @Author Huang.zh
     * @Description 组装指定表的UPDATE语句
     * @Date 2020/9/2 9:45
     */
    public String concatUpdateSql(String tableName,String globalId,String id,Map<String,Object> params,boolean needToConvertLowerCase){
        String sql = "UPDATE ".concat(tableName).concat(Constants.SET);
        StringBuffer sourceBuffer = new StringBuffer(sql);
        AtomicReference<String> comma = new AtomicReference<>("");
        params.forEach((fieldName,columnValue)->{
            String columnName = "";
            if (needToConvertLowerCase){
                columnName = convertLowerCase(fieldName);
            }else{
                columnName = fieldName;
            }
            sourceBuffer.append(comma.get().concat(columnName).concat(Constants.EQUALS).concat(Constants.QUESTION_MARK));
            if (StringUtils.isEmpty(comma.get())){
                comma.set(Constants.COMMA);
            }
        });
        if (org.apache.commons.lang3.StringUtils.isNotBlank(globalId)){
            sourceBuffer.append(Constants.WHERE).append("global_id").append(Constants.EQUALS).append("'"+globalId+"'");
        }else{
            if (id.contains(Constants.COMMA)){
                //批量更新
                sourceBuffer.append(Constants.WHERE).append("FIND_IN_SET(dts_cmf_id,'"+id+"') ");
            }else{
                //单条更新
                sourceBuffer.append(Constants.WHERE).append("dts_cmf_id").append(Constants.EQUALS).append("'"+id+"'");
            }
        }
        return sourceBuffer.toString();
    }

    /**
     * @Author Huang.zh
     * @Description 组装指定表的INSERT语句
     * @Date 2020/9/2 9:27
     */
    public String concatInsertSql(String tableName,Map<String,Object> params,boolean needToConvertLowerCase){
        String sql = "INSERT INTO ".concat(tableName);
        StringBuffer sourceBuffer = new StringBuffer(sql);
        StringBuffer columns = new StringBuffer(Constants.LEFT_PARENTHESIS);
        StringBuffer values = new StringBuffer(Constants.LEFT_PARENTHESIS);
        AtomicReference<String> comma = new AtomicReference<>("");
        params.forEach((fieldName,columnValue)->{
            if (needToConvertLowerCase){
                columns.append(comma.get().concat(convertLowerCase(fieldName)));
            }else{
                columns.append(comma.get().concat(fieldName));
            }
            values.append(comma.get().concat(Constants.QUESTION_MARK));
            if (StringUtils.isEmpty(comma.get())){
                comma.set(Constants.COMMA);
            }
        });
        columns.append(Constants.RIGHT_PARENTHESIS);
        values.append(Constants.RIGHT_PARENTHESIS);
        sourceBuffer.append(columns).append(Constants.VALUES).append(values);
        return sourceBuffer.toString();
    }



    /**
     * @Author Huang.zh
     * @Description 组装指定表的SELECT语句
     * @Date 2020/9/1 17:26
     */
    public String concatQuerySql(String tableName,Map<String,Object> params){
        String sql = Constants.SELECT+tableName+" WHERE 1=1 ";
        StringBuffer buffer = new StringBuffer(sql);
        params.forEach((fieldName,columnValue)->{
            if (columnValue != null){
                String columnName = convertLowerCase(fieldName);
                buffer.append(Constants.AND).append(columnName).append(Constants.PLACE_HOLDER);
            }
        });
        return buffer.toString();
    }

    /**
     * @Author Huang.zh
     * @Description 驼峰命名转下划线小写
     * @Date 2020/9/1 16:31
     */
    private String convertLowerCase(String columnName){
        return CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, columnName);
    }
}
