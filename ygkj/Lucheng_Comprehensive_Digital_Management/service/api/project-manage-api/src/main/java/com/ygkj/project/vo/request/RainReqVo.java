package com.ygkj.project.vo.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("雨量列表入参")
public class RainReqVo {

    @ApiModelProperty("名称")
    private String stName;

    @ApiModelProperty("行政区划")
    private String areaCode;

    @ApiModelProperty("降雨历时")
    private Integer minutes;

    @ApiModelProperty("1超过阈值 2全部")
    private Integer type;
}
