package com.ygkj.soft.database.api.VO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author Huang.zh
 * @date 2020/9/8 16:35
 * @Description: 业务数据查询参数封装
 */
@Data
@ApiModel("业务库数据查询实体")
public class BusinessDataQueryVO {
    @ApiModelProperty("数据库名称")
    @NotNull(message = "数据库名称不允许为空！")
    private String dataBaseName;
    @ApiModelProperty("表名")
    @NotNull(message = "表名称不允许为空！")
    private String tableName;

    @ApiModelProperty("id")
    private String id;

    @ApiModelProperty("当前页")
    private Integer page;
    @ApiModelProperty("每页规定记录数")
    private Integer rows;
    @ApiModelProperty("划分状态 1已划分 0未划分")
    private String divideStatus;

    public BusinessDataQueryVO() {
    }

    public BusinessDataQueryVO(@NotNull(message = "数据库名称不允许为空！") String dataBaseName
            , @NotNull(message = "表名称不允许为空！") String tableName) {
        this.dataBaseName = dataBaseName;
        this.tableName = tableName;
    }
}
