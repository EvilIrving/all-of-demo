package com.ygkj.flood.vo.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class AttDictRequestVo {

    @ApiModelProperty("字典编码")
    private String dictCode;
}
