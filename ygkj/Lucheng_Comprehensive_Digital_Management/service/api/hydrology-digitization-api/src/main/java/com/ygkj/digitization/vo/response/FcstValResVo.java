package com.ygkj.digitization.vo.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class FcstValResVo {

    @ApiModelProperty("预报id")
    private String fcstId;

    @ApiModelProperty("站点编码")
    private String stCode;

    @ApiModelProperty("时刻")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date tm;

    @ApiModelProperty("潮位")
    private BigDecimal tdz;
}
