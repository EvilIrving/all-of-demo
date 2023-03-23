package com.ygkj.project.vo.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("雨量列表出参")
public class RainResVo {

    @ApiModelProperty("测站编码")
    private String stCode;

    @ApiModelProperty("测站名称")
    private String stName;

    @ApiModelProperty("降雨量")
    private Double drp;

    @ApiModelProperty("阈值")
    private Double threshold;

    @ApiModelProperty("经度")
    private Double stLong;

    @ApiModelProperty("纬度")
    private Double stLat;
}
