package com.ygkj.project.vo.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class MfdResVo {

    @ApiModelProperty("主键")
    private Integer id;

    @ApiModelProperty("年份")
    private Integer year;

    @ApiModelProperty("申报资金(万元)")
    private BigDecimal dclrFunds;

    @ApiModelProperty("上级下达资金（万元）")
    private BigDecimal superiorfunds;

    @ApiModelProperty("自筹资金(万元)")
    private BigDecimal selffunds;

    @ApiModelProperty("已使用的资金(万元)")
    private BigDecimal usedamount;

    @ApiModelProperty("已支付的资金(万元)")
    private BigDecimal paidamount;

    @ApiModelProperty("申报文件")
    private String aid;

    @ApiModelProperty("计划文件")
    private String mtnPlan;
}
