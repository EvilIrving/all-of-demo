package com.ygkj.project.vo.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@Data
@ApiModel("三个责任人出参")
public class ThreeChargeResVo {

    @ApiModelProperty("主键")
    private String id;

    @ApiModelProperty("用户名")
    private String username;

    @ApiModelProperty("联系方式")
    private String phone;

    @ApiModelProperty("职务")
    private String job;

    @ApiModelProperty("部门")
    private String deptName;

    @ApiModelProperty("责任人类型")
    private String type;
}
