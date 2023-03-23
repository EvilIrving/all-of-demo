package com.ygkj.gragh.vo.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class KpakmaReqVo extends WainReqVo {

    @ApiModelProperty("年份")
    private Integer year;
}
