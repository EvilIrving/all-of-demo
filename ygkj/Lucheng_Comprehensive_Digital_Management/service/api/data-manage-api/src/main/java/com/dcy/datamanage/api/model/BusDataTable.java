package com.dcy.datamanage.api.model;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author Huang.zh
 * @date 2020/9/8 8:54
 * @Description: 记录数据源中的表
 */

@Data
@ApiModel("表别名信息")
public class BusDataTable implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键")
    private String id;

    @ApiModelProperty("所属数据源的id")
    private String dataSourceId;

    @ApiModelProperty("表格名称")
    private String tableName;

    @ApiModelProperty("表别名")
    private String customTableName;

}

