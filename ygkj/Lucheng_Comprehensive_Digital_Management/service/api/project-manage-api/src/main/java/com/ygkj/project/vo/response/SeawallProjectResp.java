package com.ygkj.project.vo.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel("海塘列表返回参数")
public class SeawallProjectResp implements Serializable {

    @ApiModelProperty("海塘名称")
    private String seawallName;

    @ApiModelProperty("工程名称")
    private String projectName;

    @ApiModelProperty("所在市")
    private String cityAdnm;

    @ApiModelProperty("所在县")
    private String countryAdnm;

    @ApiModelProperty("海塘级别")
    private String seawallLevel;

    @ApiModelProperty("海塘长度")
    private String seawallLength;

    @ApiModelProperty("管理层级")
    private String managementLevel;

    @ApiModelProperty("经度")
    private String longitude;

    @ApiModelProperty("纬度")
    private String latitude;
}
