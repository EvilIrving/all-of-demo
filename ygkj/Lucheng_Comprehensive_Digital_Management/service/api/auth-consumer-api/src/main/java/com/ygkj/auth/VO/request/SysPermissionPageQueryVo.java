package com.ygkj.auth.VO.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel
public class SysPermissionPageQueryVo {

    private Integer pageSize;

    private Integer pageNum;

    @ApiModelProperty("工作名称")
    String name;
    @ApiModelProperty("工作描述")
    String description;
    @ApiModelProperty("是否查询我的工作")
    Boolean job;
    @ApiModelProperty("前端忽略")
    String ids;

}
