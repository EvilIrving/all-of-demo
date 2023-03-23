package com.ygkj.gragh.vo.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ProjectResVo {

    @ApiModelProperty("主键")
    private String id;

    @ApiModelProperty("工程编码")
    private String projectCode;

    @ApiModelProperty("工程名称")
    private String projectName;

    @ApiModelProperty("经度")
    private String longitude;

    @ApiModelProperty("纬度")
    private String latitude;
}
