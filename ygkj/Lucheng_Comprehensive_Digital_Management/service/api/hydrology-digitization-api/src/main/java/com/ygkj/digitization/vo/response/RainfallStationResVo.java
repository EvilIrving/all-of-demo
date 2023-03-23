package com.ygkj.digitization.vo.response;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel("实时报表---实时雨量接口出参")
public class RainfallStationResVo {
    @ApiModelProperty("测站编码")
    private String stCode;
    @ApiModelProperty("测站名称")
    private String stName;
    @ApiModelProperty("行政区划编码")
    private String areaCode;
    @ApiModelProperty("行政区划名称")
    private String areaName;
    @ApiModelProperty("小时雨量列表")
    List<JSONObject> rainfalls;
    @ApiModelProperty("是否为水库主测站")
    private boolean mainStation;
    @ApiModelProperty("是否重点关注")
    private boolean isFocus;
    @ApiModelProperty("是否显示数据")
    private boolean display;
    @ApiModelProperty("雨量")
    private Double rainfall;
    private String projCode;
    @ApiModelProperty("是否参与水库面雨量计算")
    private boolean isReservoirSurfaceRainStation;
    @ApiModelProperty("水位")
    private Double waterLevel;
    @ApiModelProperty("潮位")
    private Double tdz;
    //闸上水位
    @ApiModelProperty("闸上水位")
    private Double upz;
    //闸下水位
    @ApiModelProperty("闸下水位")
    private Double dwz;
    //开度
    @ApiModelProperty("开度")
    private String openGate;
    //最大过闸流量
    @ApiModelProperty("最大过闸流量")
    private String flow;
    //流量
    @ApiModelProperty("流量")
    private String ll;
    @ApiModelProperty("累计雨量")
    private Double cumulativeRainfall;
}
