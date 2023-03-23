package com.ygkj.digitization.vo.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class AttTideFcstValResVo {

    /**
     * 预报id
     */
    @ApiModelProperty("预报id")
    private String fcstId;

    /**
     * 潮位站编码
     */
    @ApiModelProperty("潮位站编码")
    private String stCode;

    /**
     * 时刻
     */
    @ApiModelProperty("时刻")
    private Date dataTime;

    /**
     * 潮位
     */
    @ApiModelProperty("潮位")
    private BigDecimal drillTdz;
}
