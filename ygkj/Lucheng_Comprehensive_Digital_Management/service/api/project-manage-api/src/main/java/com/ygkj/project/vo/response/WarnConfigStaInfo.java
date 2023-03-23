package com.ygkj.project.vo.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class WarnConfigStaInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("测站编码")
    private String stCode;

    @ApiModelProperty("测站名")
    private String stName;

    @ApiModelProperty("所属流域")
    private String bas;

    @ApiModelProperty("行政区划")
    private String areaCode;

    @ApiModelProperty("行政区划名")
    private String adnm;

    @ApiModelProperty("降雨历时")
    private String rainfallPeriod;

    @ApiModelProperty("阈值")
    private Double threshold;

    @ApiModelProperty("报警阈值信息id")
    private String id;

    @ApiModelProperty(value = "水库面雨量计算参与测站")
    private Boolean isSurfaceRainStation;

    @ApiModelProperty(value = "排序号", hidden = true)
    private Integer sort;
}
