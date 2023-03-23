package com.ygkj.warning.vo.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel
public class RelWarnGroupAddVo {

    @ApiModelProperty("模板id")
    private Long templateId;

    @ApiModelProperty("当前需要绑定的对象id")
    private String objId;
}
