package com.ygkj.digitization.vo.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ProjectResVo {

    @ApiModelProperty("工程编码")
    private String projectCode;

    @ApiModelProperty("工程名称")
    private String projectName;

    @ApiModelProperty("所属流域")
    private String bas;

    @ApiModelProperty("行政区划编码")
    private String adcd;

    @ApiModelProperty("行政区划名称")
    private String adnm;
}
