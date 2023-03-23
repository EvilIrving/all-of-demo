package com.ygkj.flood.vo.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class EmergencyDispatchRequestVo {

    @ApiModelProperty("测站编码")
    @NotBlank(message = "测站编码不允许为空")
    private String stationCode;
    @ApiModelProperty("队伍名称")
    private String teamName;
    @ApiModelProperty("纬度")
    private String latitude;
    @ApiModelProperty("经度")
    private String longitude;

}
