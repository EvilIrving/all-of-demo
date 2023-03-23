package com.ygkj.soft.database.api.VO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author Huang.zh
 * @date 2020/11/9 14:05
 * @Description:
 */
@Data
public class BusDataTableAddVo {
    @ApiModelProperty("表名")
    @NotBlank(message = "表名不允许为空")
    private String tableName;
    @ApiModelProperty("数据源id")
    @NotBlank(message = "请指定一个数据源")
    private String dataSourceId;
    @ApiModelProperty("备注")
    private String remarks;
    @ApiModelProperty("库表划分目录目录id")
    private String resourceId;


}
