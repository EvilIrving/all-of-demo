package com.ygkj.soft.database.api.VO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author Huang.zh
 * @date 2020/9/9 17:29
 * @Description: 中间库数据查询参数封装
 */
@Data
@ApiModel("中间库数据查询参数")
public class SoftDataQueryVO {
    @ApiModelProperty("主键")
    private String globalId;
    @ApiModelProperty("数据库名称")
    @NotNull(message = "数据库名称不允许为空！")
    private String dataBaseName;
    @ApiModelProperty("表名")
    @NotNull(message = "表名称不允许为空！")
    private String tableName;
    @ApiModelProperty("审核状态 0未审核 1通过 2驳回")
    private String status;
    @ApiModelProperty("操作类型，1新增，2修改，3删除")
    private String operationType;
    @ApiModelProperty("当前页")
    private Integer page;
    @ApiModelProperty("每页规定记录数")
    private Integer rows;
}
