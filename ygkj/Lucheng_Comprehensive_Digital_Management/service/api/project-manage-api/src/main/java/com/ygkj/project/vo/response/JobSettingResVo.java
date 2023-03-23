package com.ygkj.project.vo.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class JobSettingResVo {

    @ApiModelProperty("类别")
    private String jobType;

    @ApiModelProperty("标化工程编码")
    private String pjcd;

    @ApiModelProperty("类别名称")
    private String jobTypeName;
}
