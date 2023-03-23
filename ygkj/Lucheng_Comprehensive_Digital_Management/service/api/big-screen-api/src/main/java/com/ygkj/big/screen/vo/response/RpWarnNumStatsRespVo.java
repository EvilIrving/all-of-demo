package com.ygkj.big.screen.vo.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class RpWarnNumStatsRespVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("状态")
    private String status;

    @ApiModelProperty("数量")
    private Integer num = 0;

}
