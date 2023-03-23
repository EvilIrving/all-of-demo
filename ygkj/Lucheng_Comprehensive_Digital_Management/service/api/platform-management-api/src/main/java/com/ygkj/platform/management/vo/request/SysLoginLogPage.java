package com.ygkj.platform.management.vo.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel
public class SysLoginLogPage{

    @ApiModelProperty("登录人名称")
    private String userName;

    @ApiModelProperty("登录时间：yyyy-MM-dd")
    private String loginTime;

    @ApiModelProperty("登录开始时间：yyyy-MM-dd")
    private String startTime;

    @ApiModelProperty("登录结束时间：yyyy-MM-dd")
    private String endTime;

    @ApiModelProperty("部门名称")
    private String department;

    @ApiModelProperty(value = "第几页")
    private int pageNum = 1;

    @ApiModelProperty(value = "分页数量")
    private int pageSize = 10;

}
