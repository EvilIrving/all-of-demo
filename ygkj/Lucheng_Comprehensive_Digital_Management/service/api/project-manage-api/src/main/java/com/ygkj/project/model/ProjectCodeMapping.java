package com.ygkj.project.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProjectCodeMapping {

    @ApiModelProperty("本地工程编码")
    private String localPrcd;

    @ApiModelProperty(value = "外部工程编码",hidden = true)
    private String foreignPrcd;

    @ApiModelProperty(value = "来源 1标化平台",hidden = true)
    private Integer type;

    @ApiModelProperty("工程类型 1海塘 2水库 3灌区 4圩区 5堤防 6水闸 7泵站 8渡槽 9倒虹吸 10输供水隧洞 11闸站")
    private Integer projectType;
}
