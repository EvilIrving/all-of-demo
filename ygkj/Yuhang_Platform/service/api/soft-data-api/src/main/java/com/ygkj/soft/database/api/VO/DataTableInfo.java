package com.ygkj.soft.database.api.VO;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author fml
 * @date 2021-05-25 11:27
 * @description
 */
@Data
public class DataTableInfo {

    @ApiModelProperty("表名")
    private String tableName;
    @ApiModelProperty("表名注释")
    private String remarks;
    @ApiModelProperty("数据库")
    private String dataBaseName;
}
