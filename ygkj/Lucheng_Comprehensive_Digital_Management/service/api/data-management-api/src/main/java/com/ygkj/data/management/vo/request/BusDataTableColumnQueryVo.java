package com.ygkj.data.management.vo.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@Data
@ApiModel
public class BusDataTableColumnQueryVo {

    @ApiModelProperty("数据表库名称")
    private String schema;

    @ApiModelProperty("数据表名称")
    private String table;

    @ApiModelProperty("数据列集合")
    private List<String> columnList;

    @ApiModelProperty("文本查询参数，是or的关系")
    private Map<String, Object> params;

    @ApiModelProperty("下拉框查询参数，是and的关系")
    private Map<String, Object> dropDownParams;

    @ApiModelProperty("主键列名称")
    private String primaryColumn;

    @ApiModelProperty("数据id，及主键值")
    private String dataId;

    @ApiModelProperty("有权限的行")
    private Collection<String> permittedTableRows;

    @ApiModelProperty("排序")
    private String orderSql;

    public BusDataTableColumnQueryVo(String schema, String table, List<String> columnList, Map<String, Object> params, Map<String, Object> dropDownParams, String primaryColumn, String dataId, Collection<String> permittedTableRows, String orderSql) {
        this.schema = schema;
        this.table = table;
        this.columnList = columnList;
        this.params = params;
        this.dropDownParams = dropDownParams;
        this.primaryColumn = primaryColumn;
        this.dataId = dataId;
        this.permittedTableRows = permittedTableRows;
        this.orderSql = orderSql;
    }

}
