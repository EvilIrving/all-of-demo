package com.ygkj.big.screen.vo.request;

import com.ygkj.entity.PageEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("地图雨量测站查询参数封装")
public class SynRainRequestVo extends PageEntity {

    @ApiModelProperty(value = "测站名称")
    private String stationName;

    @ApiModelProperty(value = "行政区划")
    private String areaCode;

    @ApiModelProperty(value = "流域")
    private String bas;

    @ApiModelProperty(value = "降雨历时")
    private Integer minutes;

    @ApiModelProperty(value = "降雨范围最小值")
    private double min;

    @ApiModelProperty(value = "降雨范围最大值")
    private double max;

    @ApiModelProperty(value = "是否预警")
    private boolean warning;

    @ApiModelProperty(value = "是否参与面雨量计算")
    private Boolean isSurfaceRainStation = Boolean.TRUE;
}
