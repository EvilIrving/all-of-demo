package com.ygkj.project.vo.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class DutyResVo {

    @ApiModelProperty("姓名")
    private String name;

    @ApiModelProperty("电话")
    private String telphone;

    @ApiModelProperty("职务")
    private String duty;
}
