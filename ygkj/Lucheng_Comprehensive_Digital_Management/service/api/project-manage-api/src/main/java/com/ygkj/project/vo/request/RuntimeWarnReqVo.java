package com.ygkj.project.vo.request;

import com.ygkj.entity.PageEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class RuntimeWarnReqVo {

    @ApiModelProperty("工程类型")
    private String projectType;

    @ApiModelProperty("工程编码")
    private String projectCode;

    @ApiModelProperty("是否携带工程落点悬浮窗信息")
    private Boolean isProjInfo = false;
}
