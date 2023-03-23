package com.ygkj.vo.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class DeptReqVo {

    @ApiModelProperty(value = "部门名称")
    private String name;

    @ApiModelProperty(value = "部门状态（0、正常；1、停用）")
    private Integer status;

    @ApiModelProperty(value = "父id")
    private String pid;
}
