package com.ygkj.digitization.vo.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ProjectListReqVo extends BaseReqVo {

    @ApiModelProperty(value = "工程类型")
    private String projectType;

    @ApiModelProperty(value = "名称")
    private String projectName;

    @ApiModelProperty("水质是否正常 正常 不正常")
    private String status;
}
