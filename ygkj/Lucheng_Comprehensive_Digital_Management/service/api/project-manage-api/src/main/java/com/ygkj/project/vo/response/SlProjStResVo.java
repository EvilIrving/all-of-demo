package com.ygkj.project.vo.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 工程测站关系表
 */
@Data
public class SlProjStResVo {

    @ApiModelProperty("工程编码")
    private String projectCode;

    @ApiModelProperty("测站编码")
    private String stcd;

    @ApiModelProperty("测站名称")
    private String stnm;

    @ApiModelProperty("测站类型")
    private String stType;

    @ApiModelProperty("工程类型")
    private String projectType;
}
