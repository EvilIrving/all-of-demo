package com.dcy.datamanage.api.model;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class DataAssessFrequency {

    @ApiModelProperty("主键")
    private String id;

    @ApiModelProperty("数据源id")
    private String dataSourceId;

    @ApiModelProperty("表名")
    @TableField("`table_name`")
    private String tableName;

    @ApiModelProperty("频次单位 1.天 2.月 3.季度 4.年")
    private Integer frequencyUnit;
}
