package com.ygkj.gragh.vo.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class WainReqVo {

    @ApiModelProperty("类型 1行政区划 2流域")
    private int type;

    @ApiModelProperty("行政区划代码/流域代码")
    private String areaCode;

    @ApiModelProperty("编码或名称")
    private String codeOrName;
}
