package com.ygkj.water.project.service;

import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;

/**
 * @author xq
 * @Description
 * @Date 2021/5/27
 */
public interface AdcdBaseService {


    String adcdOrderSqlTemplate = "ORDER BY FIELD(CONCAT(SUBSTR( `&{columnName}` from 1 for 6),'000000'),'330383000000','330327000000','330329000000','330326000000','330328000000','330324000000','330381000000','330382000000','330322000000','330304000000','330303000000','330302000000') desc";

    String columnNameKey = "&{columnName}";

    default String adcdOrderSql(String columnName) {
        return adcdOrderSqlTemplate.replace(columnNameKey, columnName);
    }

    default String adcdOrderSql(String columnName, String secondColumnName, boolean ascDesc) {
        return adcdOrderSql(columnName)
                .concat(", `")
                .concat(secondColumnName)
                .concat("` ")
                .concat(ascDesc ? "ASC" : "DESC");
    }

    /**
     * 根据行政区划编码查询行政区划名称并组成以编码为key的map
     *
     * @param adcdList
     * @return
     */
    Map<String, String> selectAdnmByAdcd(List<String> adcdList);
}
