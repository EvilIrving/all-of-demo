package com.ygkj.big.screen.vo.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author fml
 * @date 2021-03-29 18:17
 * @description 水位测站相关信息
 */
@Data
public class FrWaterLevelStaRespVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("测站代码")
    private String stCode;

    @ApiModelProperty("测站名称")
    private String stName;

    @ApiModelProperty("测站类型（RR水库水位站，PP雨量站，DD闸坝水位站，TT潮位站，ZG地下水位站，ZQ流量站，ZZ河道）")
    private String stType;

    /*@ApiModelProperty("汛限水位")
    private Double limitLevel;

    @ApiModelProperty("警戒水位")
    private Double warningLevel;*/

    @ApiModelProperty("实时水位")
    private Double z;

    @ApiModelProperty("闸上水位")
    private Double upz;

    @ApiModelProperty("闸下水位")
    private Double dwz;

    @ApiModelProperty("开度")
    private String ovs;

    @ApiModelProperty("状态（0不正常，1正常）")
    private String status = "1";
}
