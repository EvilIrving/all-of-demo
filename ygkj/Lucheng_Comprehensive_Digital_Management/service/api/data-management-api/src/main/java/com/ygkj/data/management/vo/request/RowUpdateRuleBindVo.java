package com.ygkj.data.management.vo.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class RowUpdateRuleBindVo {

    @ApiModelProperty("表id")
    private String tableId;

    @ApiModelProperty("表名")
    private String tableName;

    @ApiModelProperty("主键字段名")
    @NotBlank(message = "主键字段值不能为空")
    private String primaryName;

    @ApiModelProperty("主键值")
    @NotBlank(message = "必须指定一行记录")
    private String primaryValue;

    @ApiModelProperty("规则id")
    private String ruleId;

    public RowUpdateRuleBindVo() {
    }

    public RowUpdateRuleBindVo(String tableId, String tableName, String primaryName, String primaryValue, String ruleId) {
        this.tableId = tableId;
        this.tableName = tableName;
        this.primaryName = primaryName;
        this.primaryValue = primaryValue;
        this.ruleId = ruleId;
    }
}
