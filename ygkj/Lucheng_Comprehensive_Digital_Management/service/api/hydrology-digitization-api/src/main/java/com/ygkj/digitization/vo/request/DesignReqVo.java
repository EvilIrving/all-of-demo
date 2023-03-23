package com.ygkj.digitization.vo.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("设计安全分析入参")
public class DesignReqVo extends AnalysisListReqVo {

    @ApiModelProperty("工程类型")
    private String projectType;
}
