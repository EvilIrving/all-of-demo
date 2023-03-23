package com.ygkj.data.management.vo.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class ColumnRuleBindVo {

    @ApiModelProperty("字段id")
    @NotBlank(message = "必须指定一个字段")
    private String columnId;

    @ApiModelProperty("规则id，多个用逗号隔开")
    private String ruleId;

    @ApiModelProperty("字段中文释义")
    private String columnComment;

    @ApiModelProperty("字段名")
    private String columnName;

    @ApiModelProperty("表id")
    private String tableId;

    @ApiModelProperty("表名称")
    private String tableName;

    public ColumnRuleBindVo() {
    }

    public ColumnRuleBindVo(String columnId, String ruleId,
                            String columnComment, String columnName,
                            String tableId, String tableName) {
        this.columnId = columnId;
        this.ruleId = ruleId;
        this.columnComment = columnComment;
        this.columnName = columnName;
        this.tableId = tableId;
        this.tableName = tableName;
    }
}
