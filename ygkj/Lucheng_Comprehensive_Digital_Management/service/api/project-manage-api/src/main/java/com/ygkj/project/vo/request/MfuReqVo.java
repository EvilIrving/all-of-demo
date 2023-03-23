package com.ygkj.project.vo.request;

import com.ygkj.entity.PageEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class MfuReqVo extends PageEntity {

    @ApiModelProperty("海塘编码")
    private String seawallCode;

    @ApiModelProperty("工程类型 1工程 2设备 3环境 4其他")
    private String mfType;

    @ApiModelProperty("年份")
    private Integer year;
}
