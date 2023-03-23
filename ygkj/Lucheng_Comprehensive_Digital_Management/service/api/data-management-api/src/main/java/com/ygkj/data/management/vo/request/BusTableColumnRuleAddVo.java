package com.ygkj.data.management.vo.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class BusTableColumnRuleAddVo {
    @ApiModelProperty("前端忽略")
    private String id;
    @ApiModelProperty("表id")
    private String tableId;
    @ApiModelProperty("字段名称")
    private String columnName;
    @ApiModelProperty("字段中文释义")
    private String columnComment;
    @ApiModelProperty("规则id")
    private String ruleId;
    @ApiModelProperty("备注")
    private String remark;
}
