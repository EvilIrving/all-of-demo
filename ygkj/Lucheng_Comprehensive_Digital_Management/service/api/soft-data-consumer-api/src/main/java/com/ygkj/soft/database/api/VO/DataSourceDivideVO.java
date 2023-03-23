package com.ygkj.soft.database.api.VO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author Huang.zh
 * @date 2020/9/17 9:22
 * @Description: 数源划分参数封装
 */
@Data
@ApiModel("数源划分参数")
public class DataSourceDivideVO {

    @ApiModelProperty("数据源名称")
    private String dataBaseName;
    @ApiModelProperty("表名")
    private String tableName;
    @ApiModelProperty("记录id，多条用逗号隔开")
    private String id;
    @ApiModelProperty("科室id")
    private String deptId;
}
