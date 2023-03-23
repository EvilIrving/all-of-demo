package com.ygkj.project.vo.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class JobSettingReqVo {

    @ApiModelProperty("标化工程编码")
    private String pjcd;

    @ApiModelProperty("岗位类型")
    private String jobType;
}
