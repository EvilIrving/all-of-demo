package com.ygkj.project.api.vo.res;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class RelProjectResVo {

    @ApiModelProperty("工程编码")
    private String prcd;

    @ApiModelProperty("工程名称")
    private String name;

    @ApiModelProperty("行政区划编码")
    private String adcd;

    @ApiModelProperty("行政区划名称")
    private String adnm;

    @ApiModelProperty("所在乡镇名称")
    private String townadnm;

    @ApiModelProperty("所在村")
    private String village;

    @ApiModelProperty("工程类型")
    private String projectCategory;

    @ApiModelProperty("所在圩区编码")
    private String polderCode;

    @ApiModelProperty("所在圩区名称")
    private String polderName;

    @ApiModelProperty("工程规模")
    private String projectScale;

    @ApiModelProperty("工程等别")
    private String prlv;

}
