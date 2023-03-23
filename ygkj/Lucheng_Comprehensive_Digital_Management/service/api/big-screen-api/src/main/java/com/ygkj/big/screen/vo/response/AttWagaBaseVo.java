package com.ygkj.big.screen.vo.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author  wgf
 * @date  2021/12/24 11:15
 */
@Data
public class AttWagaBaseVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("水闸代码")
    private String wagaCode;

    @ApiModelProperty("水闸名称")
    private String wagaName;

    @ApiModelProperty("闸口数量")
    private Integer gaorNum;

    @ApiModelProperty("开度")
    private String ovs;

    @ApiModelProperty("开度监测时间")
    private String tm;

    @ApiModelProperty("绑定的监测闸站编码")
    private String stcd;

    @ApiModelProperty("河道名称")
    private String riverName;

    @ApiModelProperty("闸上水位")
    private Double upz;

    @ApiModelProperty("闸下水位")
    private Double dwz;

    @ApiModelProperty("流量")
    private String ll;

    @ApiModelProperty("水闸的最大过闸流量")
    private BigDecimal desLockDisc;

    @ApiModelProperty("测站的最大过闸流量")
    private String mxgtq;

    @ApiModelProperty("兴利死水位")
    private BigDecimal hennessyDealLevel;

    @ApiModelProperty("0正常、1接近满负荷、2满负荷、-1无数据")
    private Integer llStatus = 0;

    @ApiModelProperty("排水分析0正常、1强排")
    private Integer psStatus = 0;

    @ApiModelProperty("控运分析0正常、1超控运、2无控运")
    private Integer kyStatus = 0;
}

