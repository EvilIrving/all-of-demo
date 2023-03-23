package com.ygkj.digitization.vo.request;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class StationRequestVo {

    @ApiModelProperty(value = "测站编码")
    private String stationCodes;

    @ApiModelProperty(value = "工程编码")
    private String projectCodes;

    @ApiModelProperty(value = "流域")
    private String bas;

    @ApiModelProperty(value = "测站类型")
    private String stationType;
    @ApiModelProperty(value = "测站名称")
    private String stationName;
    @ApiModelProperty(value = "行政区划编码")
    private String areaCode;
    @ApiModelProperty(value = "传大中型水库可查询19座大中型水库的实时水位")
    private String resGrade;
    @ApiModelProperty(value = "雨量站查询专用参数，传true")
    private Boolean isRainStation;
    @ApiModelProperty(value = "雨量站参与面雨量计算查询专用参数，传true")
    private Boolean isSurfaceRainStation;
    @ApiModelProperty(value = "水库面雨量计算参与测站查询专用参数，传true")
    private Boolean isReservoirSurfaceRainStation;

    @ApiModelProperty("开始时间 日雨量报表的格式为yyyy-MM-dd;其他报表的格式为yyyy-MM-dd HH")
    private String startTime;

    @ApiModelProperty("结束时间 日雨量报表的格式为yyyy-MM-dd;其他报表的格式为yyyy-MM-dd HH")
    private String endTime;

    @ApiModelProperty("时段(默认1小时) 1:1小时 3:3小时 6:6小时 12:12小时 24:24小时")
    private String timeTnterval;

    @ApiModelProperty("暴雨警报时间类型 1:1小时 2:3小时 3:1天")
    private String rainstormType;
}
