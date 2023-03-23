package com.ygkj.project.vo.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel("水闸列表返回参数")
public class WagaProjectResp implements Serializable {

    @ApiModelProperty("海塘名称")
    private String wagaName;

    @ApiModelProperty("工程名称")
    private String projectName;

    @ApiModelProperty("所在市")
    private String cityAdnm;

    @ApiModelProperty("所在县")
    private String countryAdnm;

    @ApiModelProperty("水闸规模")
    private String engScal;

    @ApiModelProperty("所在河流")
    private String bas;

    @ApiModelProperty("最大过闸流量(m³/s)")
    private String desLockDisc;

    @ApiModelProperty("管理层级")
    private String managementLevel;

    @ApiModelProperty("经度")
    private String longitude;

    @ApiModelProperty("纬度")
    private String latitude;
}
