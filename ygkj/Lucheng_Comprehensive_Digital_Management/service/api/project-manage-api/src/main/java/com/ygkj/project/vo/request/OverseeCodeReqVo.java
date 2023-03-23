package com.ygkj.project.vo.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class OverseeCodeReqVo {

    @ApiModelProperty("督办id")
    private String id;

    @ApiModelProperty("手机号")
    private String phone;
}
