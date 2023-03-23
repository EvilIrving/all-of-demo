package com.ygkj.data.management.vo.response;

import lombok.Data;

/**
 * 水利厅接口表映射
 */
@Data
public class WcpTableMappingResVo {
    private String tableName;
    private String columnName;
    private String mappingName;
    private Integer isPrimary;
}
