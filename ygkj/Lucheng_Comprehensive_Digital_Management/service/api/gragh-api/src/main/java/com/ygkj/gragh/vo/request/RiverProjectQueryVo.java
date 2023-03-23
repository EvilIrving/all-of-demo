package com.ygkj.gragh.vo.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("涉河项目参数查询封装")
public class RiverProjectQueryVo {

    @ApiModelProperty("行政区划代码")
    private String areaCode;
    @ApiModelProperty("工程名称")
    private String projectName;
    @ApiModelProperty("工程编码，查询单条记录详情的时候用")
    private String projectCode;
}
