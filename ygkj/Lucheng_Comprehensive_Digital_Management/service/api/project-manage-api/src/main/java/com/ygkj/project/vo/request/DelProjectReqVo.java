package com.ygkj.project.vo.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel("批量删除工程入参")
public class DelProjectReqVo {

    @ApiModelProperty("工程类型")
    private String projectType;

    @ApiModelProperty("id 逗号分隔")
    private String ids;

    @ApiModelProperty(hidden = true)
    private List<String> idList;
}
