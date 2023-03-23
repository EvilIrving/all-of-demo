package com.ygkj.data.management.vo.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel
public class BusDataTableApplyPageQueryVo {
    @ApiModelProperty("页码")
    private Integer pageNum;
    @ApiModelProperty("每页限定记录数")
    private Integer pageSize;
    @ApiModelProperty("id，查单条记录用")
    private String id;
    @ApiModelProperty("前端忽略")
    private String userId;
    @ApiModelProperty("审核人,前端忽略")
    private String examinePerson;
    @ApiModelProperty("状态：待审核/已通过/已驳回")
    private String status;
    @ApiModelProperty("查询字段")
    private String queryStr;
    @ApiModelProperty("数据表id")
    private String tableId;
}
