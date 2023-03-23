package com.ygkj.project.vo.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class WarnUserInfoReq {

    @ApiModelProperty("报警配置id")
    private String configId;

    @ApiModelProperty("用户id")
    private String userId;

    @ApiModelProperty("用户名")
    private String userName;

    @ApiModelProperty("联系方式")
    private String phone;

    @ApiModelProperty("部门id")
    private String deptId;

    @ApiModelProperty("部门名称")
    private String deptName;
}
