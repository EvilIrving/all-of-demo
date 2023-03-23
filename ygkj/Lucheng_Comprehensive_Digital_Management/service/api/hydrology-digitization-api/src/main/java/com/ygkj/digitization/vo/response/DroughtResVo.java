package com.ygkj.digitization.vo.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 旱情预警返回类
 *
 * @author lxl
 */
@Data
@ApiModel
public class DroughtResVo {

    @ApiModelProperty("指标")
    private String index;

    @ApiModelProperty("行政区划编码")
    private String areaCode;

    @ApiModelProperty("行政区划")
    private String areaName;

    @ApiModelProperty("分析结果")
    private String analyzeResult;

    @ApiModelProperty("范围")
    private String scope;

    @ApiModelProperty("预警状态")
    private String wrState;

}
