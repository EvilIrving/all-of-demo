package com.ygkj.digitization.vo.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class RelDetailReqVo {

    @ApiModelProperty("主键")
    private String id;

    @ApiModelProperty(value = "类型 1旱情 2水位", required = true)
    private Integer type;
}
