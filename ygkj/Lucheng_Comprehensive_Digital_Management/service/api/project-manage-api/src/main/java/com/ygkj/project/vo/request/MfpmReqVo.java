package com.ygkj.project.vo.request;

import com.ygkj.entity.PageEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class MfpmReqVo extends PageEntity {

    @ApiModelProperty("工程编码")
    private String seawallCode;

    @ApiModelProperty("工程类型 1工程 2设备 3环境 4其他")
    private String mfType;
}
