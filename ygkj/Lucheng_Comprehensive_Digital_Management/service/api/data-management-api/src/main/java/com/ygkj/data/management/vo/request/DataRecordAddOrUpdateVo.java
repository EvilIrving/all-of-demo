package com.ygkj.data.management.vo.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class DataRecordAddOrUpdateVo {

    @ApiModelProperty("数据类型：基础数据/业务数据")
    @NotBlank(message = "请指定数据类型")
    private String schemaName;
    @ApiModelProperty("表名")
    @NotBlank(message = "请指定表名")
    private String tableName;
    @ApiModelProperty("主键的值，即第一个字段的值,修改用")
    private String primaryValue;
    @ApiModelProperty("数据json，所有字段拼凑成一个json")
    private String dataDetail;
}
