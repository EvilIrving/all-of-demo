package com.ygkj.soft.database.api.VO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author Huang.zh
 * @date 2020/9/10 11:22
 * @Description: 更改中间数据审核状态所需参数
 */
@Data
@ApiModel("更改中间数据审核状态所需参数")
public class SoftDataUpdateVO {

    @ApiModelProperty("数据库名称")
    @NotNull(message = "数据库名称不允许为空！")
    private String dataBaseName;
    @ApiModelProperty("表名")
    @NotNull(message = "表名称不允许为空！")
    private String tableName;
    @ApiModelProperty("审核操作 1通过 2驳回")
    @NotNull(message = "缺少审核操作！")
    private String status;
    @ApiModelProperty("主键，修改时务必携带")
    @NotNull(message = "请指定一条数据！")
    private String globalId;
    @ApiModelProperty("审核理由")
    private String reason;
}
