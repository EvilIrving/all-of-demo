package com.ygkj.data.management.vo.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel
public class TableRecordPageQueryVo {

    @ApiModelProperty("页码")
    private Integer pageNum;
    @ApiModelProperty("每页记录数")
    private Integer pageSize;
    @ApiModelProperty("id，查单条用")
    private String id;
    @ApiModelProperty("资源id")
    private String tableId;
    @ApiModelProperty("数据类型:基础数据/业务数据")
    private String dataType;
    @ApiModelProperty("表名")
    private String tableName;
    @ApiModelProperty("查询参数json")
    private String paramJson;
}
