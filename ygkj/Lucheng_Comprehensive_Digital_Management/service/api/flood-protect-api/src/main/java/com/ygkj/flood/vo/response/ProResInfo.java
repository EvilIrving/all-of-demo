package com.ygkj.flood.vo.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class ProResInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("工程编码")
    private String projectCode;

    @ApiModelProperty("工程名")
    private String projectName;

    @ApiModelProperty("经度")
    private Double lng;

    @ApiModelProperty("纬度")
    private Double lat;

    @ApiModelProperty("汛限水位")
    private Double limitLevel;

    @ApiModelProperty("时间")
    private String forecastTime;

    @ApiModelProperty("测站编码")
    private String stCode;

    @ApiModelProperty("测站名")
    private String stName;

    @ApiModelProperty("实时水位")
    private Double z;

    @ApiModelProperty(value = "超汛状态（1超汛）", hidden = true)
    private String warnStatus;

    @ApiModelProperty(value = "洪水预报对应的工程id")
    private Integer floodId;
}
