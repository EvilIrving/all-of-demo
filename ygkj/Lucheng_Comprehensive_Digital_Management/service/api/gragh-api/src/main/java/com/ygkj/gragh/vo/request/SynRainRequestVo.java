package com.ygkj.gragh.vo.request;

import com.ygkj.gragh.model.PageEntity;
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
    private Double min;

    @ApiModelProperty(value = "降雨范围最大值")
    private Double max;

    @ApiModelProperty(value = "是否预警")
    private boolean warning;

    @ApiModelProperty(value = "是否参与面雨量计算")
    private Boolean isSurfaceRainStation = Boolean.TRUE;

    @ApiModelProperty(value = "自定义起始时间")
    private String start;

    @ApiModelProperty(value = "自定义结束时间")
    private String end;

    /**
     * 数据来源
     */
    @ApiModelProperty(value = "数据来源 1-水文站  2-永嘉  null-全部")
    private Integer dataSource;

    /**
     * 判断是否重点关注内容
     */
    @ApiModelProperty(value = "是否重点关注 true-是  false-否  不传-全部")
    private Boolean isFocus;

    @ApiModelProperty(value = "是否显示 true-是  false-否  不传-全部")
    private Boolean isDisplay;


}
