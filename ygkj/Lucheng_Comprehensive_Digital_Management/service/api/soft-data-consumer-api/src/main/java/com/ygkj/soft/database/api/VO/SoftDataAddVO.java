package com.ygkj.soft.database.api.VO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author Huang.zh
 * @date 2020/9/9 14:38
 * @Description: 中间数据记录实体
 */
@Data
@ApiModel("中间操作数据实体")
public class SoftDataAddVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("全局ID，后台代码自动生成")
    private String globalId;

    @ApiModelProperty("id")
    private String id;

    @ApiModelProperty("表记录，json格式")
    private String data;

    @ApiModelProperty("数据库名称")
    @NotNull(message = "数据库名称不允许为空！")
    private String dataBaseName;
    @ApiModelProperty("表名")
    @NotNull(message = "表名称不允许为空！")
    private String tableName;

    @ApiModelProperty("操作类型，1新增，2修改，3删除")
    @NotNull(message = "操作类型不允许为空！")
    private String operationType;
}
