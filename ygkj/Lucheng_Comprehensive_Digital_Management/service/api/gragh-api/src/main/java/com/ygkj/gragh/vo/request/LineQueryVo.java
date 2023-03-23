package com.ygkj.gragh.vo.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("查询参数封装")
public class LineQueryVo {

    @ApiModelProperty("岸线类型：治导线，保护范围线，管理范围线")
    private String lineType;
}
