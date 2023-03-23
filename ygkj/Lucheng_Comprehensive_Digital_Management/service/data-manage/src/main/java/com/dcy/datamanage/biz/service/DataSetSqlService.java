package com.dcy.datamanage.biz.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dcy.datamanage.api.model.BusDataSetParam;
import com.dcy.datamanage.api.model.BusDataSetResField;
import com.dcy.datamanage.api.model.BusDataSetSql;
import com.dcy.datamanage.biz.constant.DataSetFieldType;
import com.dcy.datamanage.biz.converter.DataSetSqlConverter;
import com.dcy.datamanage.biz.dsm.DataSourceRegistry;
import com.dcy.datamanage.biz.dsm.DefaultDataSource;
import com.dcy.datamanage.biz.dto.input.BusDataSetPreviewInputDTO;
import com.dcy.datamanage.biz.dto.output.BusDataSetPreviewOutputDTO;
import com.dcy.datamanage.biz.dto.output.BusDataSetSqlOutputDTO;
import com.dcy.datamanage.biz.mapper.DataSetSqlMapper;
import com.dcy.datamanage.biz.util.DataSetUtil;
import com.dcy.datamanage.biz.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class DataSetSqlService extends ServiceImpl<DataSetSqlMapper, BusDataSetSql> {

    @Autowired
    private DataSourceRegistry dataSourceRegistry;

    private Pattern pattern = Pattern.compile("[#$]\\{\\w+}");

    /**
     * sql预览
     *
     * @param dto
     * @return
     */
    public BusDataSetPreviewOutputDTO preview(BusDataSetPreviewInputDTO dto) {

        //首先获取数据源
        JSONObject setDetail = dto.getSetDetail();
        BusDataSetSql setSql = setDetail.toJavaObject(BusDataSetSql.class);
        DefaultDataSource dataSource = dataSourceRegistry.getDataSource(setSql.getDataSourceId());
        JdbcTemplate jdbcTemplate = dataSource.getJdbcTemplate();

        //sql占位符替换
        List<Object> paramList = new ArrayList<>();
        String sql = dealWithParam(dto, setSql, paramList);

        //执行sql
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql, paramList.size() == 0 ? null : paramList.toArray());

        //格式化返回值
        return dealWithRes(list, dto);
    }

    private BusDataSetPreviewOutputDTO dealWithRes(List<Map<String, Object>> list, BusDataSetPreviewInputDTO dto) {
        BusDataSetPreviewOutputDTO result = new BusDataSetPreviewOutputDTO();
        result.setRes(list);
        if (!CollectionUtils.isEmpty(list)) {
            Map<String, BusDataSetResField> oldMap = DataSetUtil.getOldFieldMap(dto);
            //是否要更新字段
            if (dto.isFieldUpdate()) {
                List<BusDataSetResField> busDataSetResFieldList = new ArrayList<>();
                //返回新字段列表
                Map<String, BusDataSetResField> completeMap = new HashMap<>();
                Map<String, BusDataSetResField> pendingMap = new HashMap<>();
                for (Map<String, Object> map : list) {
                    if (map == null) {
                        continue;
                    }
                    //所有字段都已经知道了就直接返回
                    if (completeMap.size() == map.size()) {
                        break;
                    }
                    for (Map.Entry<String, Object> entry : map.entrySet()) {

                        BusDataSetResField busDataSetResField = DataSetUtil.busDataSetResField(entry.getKey(), entry.getValue(), oldMap,
                                busDataSetResFieldList, completeMap, pendingMap, valToType);

                        //返回值格式化
                        formatRes(busDataSetResField, entry);
                    }
                }
                result.setFieldList(busDataSetResFieldList);
            } else {
                for (Map<String, Object> map : list) {
                    Iterator<Map.Entry<String, Object>> it = map.entrySet().iterator();
                    while (it.hasNext()) {
                        Map.Entry<String, Object> entry = it.next();
                        String key = entry.getKey();
                        if (oldMap.containsKey(key)) {
                            BusDataSetResField busDataSetResField = oldMap.get(key);
                            //返回值格式化
                            formatRes(busDataSetResField, entry);
                        } else {
                            //删除不需要的字段
                            it.remove();
                        }
                    }
                }
            }
        }
        return result;
    }

    private String dealWithParam(BusDataSetPreviewInputDTO dto, BusDataSetSql setSql, List<Object> paramList) {
        String sql = setSql.getSqlText();
        List<BusDataSetParam> paramFieldList = dto.getParamList();
        if (!CollectionUtils.isEmpty(paramFieldList)) {
            Map<String, Object> param = dto.getParam();
            Map<String, BusDataSetParam> paramMap = paramFieldList.stream().collect(Collectors.toMap(BusDataSetParam::getParamName, Function.identity()));

            Matcher matcher = pattern.matcher(sql);
            StringBuffer sb = new StringBuffer();
            while (matcher.find()) {
                String group = matcher.group();
                String key = group.substring(2, group.length() - 1);
                Object val = param.get(key);
                if (group.startsWith("$")) {
                    if (val == null) {
                        val = "";
                    }
                    //直接替换
                    matcher.appendReplacement(sb, String.valueOf(val));
                } else {
                    //sql条件
                    matcher.appendReplacement(sb, "?");
                    if (val != null) {
                        val = formatParam(paramMap.get(key).getParamType(), val);
                    }
                    paramList.add(val);
                }
            }
            matcher.appendTail(sb);
            sql = sb.toString();
        }
        return sql;
    }

    /**
     * 格式化返回值
     *
     * @param busDataSetResField
     * @param entry
     */
    private void formatRes(BusDataSetResField busDataSetResField, Map.Entry<String, Object> entry) {
        if (entry.getValue() != null) {
            Integer fieldType = busDataSetResField.getFieldType();
            BiFunction sqlConverter = DataSetSqlConverter.getSqlConverter(fieldType);
            if (sqlConverter != null) {
                entry.setValue(
                        sqlConverter.apply(entry.getValue(), busDataSetResField.getFieldFormat())
                );
            }
        }
    }


    private Function<Object, Integer> valToType = val -> {
        if (val instanceof Double || val instanceof Float || val instanceof BigDecimal) {
            return DataSetFieldType.DECIMAL;
        } else if (val instanceof Integer) {
            return DataSetFieldType.INTEGER;
        } else if (val instanceof Number) {
            return DataSetFieldType.NUMBER;
        } else if (val instanceof String) {
            return DataSetFieldType.STRING;
        } else if (val instanceof java.sql.Date) {
            return DataSetFieldType.DATE;
        } else if (val instanceof java.sql.Time) {
            return DataSetFieldType.TIME;
        } else if (val instanceof java.sql.Timestamp || val instanceof LocalDateTime) {
            return DataSetFieldType.DATETIME;
        } else if (val instanceof Boolean) {
            return DataSetFieldType.BOOLEAN;
        }
        return DataSetFieldType.UNKNOWN;
    };


    /**
     * 格式化值
     *
     * @param type
     * @param val
     */
    private Object formatParam(Integer type, Object val) {
        switch (type) {
            case DataSetFieldType.DATE:
                val = DateUtil.strToDate((String) val, DateUtil.SDF_DATE);
                break;
            case DataSetFieldType.TIME:
                val = DateUtil.strToDate((String) val, DateUtil.SDF_TIME);
                break;
            case DataSetFieldType.DATETIME:
                val = DateUtil.strToDate((String) val, DateUtil.SDF_DATETIME);
                break;
            default:
        }
        return val;
    }


    public BusDataSetSqlOutputDTO selectByDataSetId(String dataSetId) {
        return baseMapper.selectByDataSetId(dataSetId);
    }
}
