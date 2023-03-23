package com.ygkj.flood.vo.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class WlaOverWarnNumRespVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("超汛限数量")
    private Integer limitNum;

    @ApiModelProperty("超警戒数量")
    private Integer warnNum;

    @ApiModelProperty("超保证数量")
    private Integer guaranteeNum;
}
