package com.ygkj.project.vo.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author wgf
 * @date 2022/4/29 17:04
 * @Description:
 */
@Data
public class AttResBaseVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("工程编码")
    public String resCode;

    @ApiModelProperty("工程名称")
    public String resName;

    @ApiModelProperty("汛限水位对应的库容")
    public Double flLowLimLevCap;

    @ApiModelProperty("安全鉴定状态")
    public String safetyConclusion;

    @ApiModelProperty("绑定的水位主测站")
    public String stcd;

    @ApiModelProperty("警戒水位")
    private Double warningLevel;

    @ApiModelProperty("正常蓄水位")
    private Double normWatLev;

    @ApiModelProperty("正常蓄水位相应库容")
    private Double normPoolStagCap;

    @ApiModelProperty("台汛期限制水位")
    private Double yphoonFloodLlimitWaterLevel;

    @ApiModelProperty("汛限水位")
    private Double limitLevel;
}
