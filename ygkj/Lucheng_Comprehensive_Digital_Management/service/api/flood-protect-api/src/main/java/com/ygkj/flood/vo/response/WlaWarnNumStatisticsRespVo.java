package com.ygkj.flood.vo.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class WlaWarnNumStatisticsRespVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("测站编码")
    private String stType;

    @ApiModelProperty("数量")
    private Integer num;
}
