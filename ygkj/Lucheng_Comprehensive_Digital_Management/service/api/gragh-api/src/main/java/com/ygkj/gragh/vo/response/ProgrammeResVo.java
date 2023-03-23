package com.ygkj.gragh.vo.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProgrammeResVo {

    @ApiModelProperty("工程名称")
    private String projectName;

    @ApiModelProperty("市名称")
    private String cityAdnm;

    @ApiModelProperty("县名称")
    private String countryAdnm;

    @ApiModelProperty("规划金额（万元）")
    private BigDecimal programmeInvestment;
}
