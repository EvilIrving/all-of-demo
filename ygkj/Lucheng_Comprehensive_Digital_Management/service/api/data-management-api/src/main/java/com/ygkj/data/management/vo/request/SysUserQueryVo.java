package com.ygkj.data.management.vo.request;

import com.ygkj.entity.PageEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("系统用户查询入参封装")
public class SysUserQueryVo extends PageEntity {

    @ApiModelProperty("部门id")
    private String deptId;

    @ApiModelProperty("用户名称")
    private String username;

    @ApiModelProperty("授权标记：已授权true，未授权false，默认不传")
    private Boolean permitted;

    @ApiModelProperty("业务类型 BASIC 基础数据  REALTIME 实时数据")
    private String busType;
}
