package com.ygkj.warning.vo.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class SimpleWarnReqVo {

    @ApiModelProperty("类型 1短信 2浙政钉")
    private Integer type;

    @ApiModelProperty("内容")
    private String context;

    @ApiModelProperty("手机号")
    private String phone;
}
