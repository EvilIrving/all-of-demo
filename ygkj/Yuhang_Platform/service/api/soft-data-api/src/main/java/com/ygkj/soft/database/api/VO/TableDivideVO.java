package com.ygkj.soft.database.api.VO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author Huang.zh
 * @date 2020/9/15 11:36
 * @Description: 表划分目录请求参数封装
 */
@Data
@ApiModel("表划分目录请求参数")
public class TableDivideVO {

    @NotNull(message = "划分目录id不允许为空！")
    @ApiModelProperty("划分目录id")
    private String resourceId;

    @NotNull(message = "表id不允许为空！")
    @ApiModelProperty("表id，多个可以用英文逗号隔开")
    private String tableIds;
}
