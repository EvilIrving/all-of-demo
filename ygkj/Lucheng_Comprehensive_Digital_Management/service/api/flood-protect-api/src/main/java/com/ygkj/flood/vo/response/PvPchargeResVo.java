package com.ygkj.flood.vo.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author lxl
 * @create 2022-05-06 17:12
 * @description
 */
@Data
@ApiModel("山洪灾害责任人返回")
public class PvPchargeResVo {

    @ApiModelProperty("责任人编码")
    private String pchargeCode;

    @ApiModelProperty("行政村")
    private String adminAdnm;

    @ApiModelProperty("自然村")
    private String naturalAdnm;

    @ApiModelProperty("责任人类型")
    private String chargeType;

    @ApiModelProperty("责任人级别")
    private String chargeLevel;

    @ApiModelProperty("责任人姓名")
    private String pchargeName;

    @ApiModelProperty("手机号码")
    private String mobile;

    @ApiModelProperty("所在单位")
    private String unit;

    @ApiModelProperty("单位职务")
    private String position;

    @ApiModelProperty("镇（街道）")
    private String adName;
}
