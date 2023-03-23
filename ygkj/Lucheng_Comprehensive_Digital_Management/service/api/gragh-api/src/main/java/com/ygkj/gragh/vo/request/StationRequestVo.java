package com.ygkj.gragh.vo.request;


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
    @ApiModelProperty(value = "是否显示")
    private Boolean display;
    @ApiModelProperty(value = "是否温瑞平水闸，传true")
    private Boolean isWrpWaga;
    @ApiModelProperty(value = "是否是永嘉测站，传true")
    private Boolean isYongJiaStation;

    @ApiModelProperty(value = "是否是水文站测站")
    private Boolean isShuiWenStation;

    @ApiModelProperty(value = "是否重点关注")
    private Boolean isFocus;

    @ApiModelProperty("水库类型：大中型，小（Ⅰ）型，小（Ⅱ）型")
    private String engScal;
    @ApiModelProperty("河道水位类型：平原河网/山区河道/其他")
    private String riverStationType;
    @ApiModelProperty("是否只显示主测站")
    private Boolean main = Boolean.FALSE;

    @ApiModelProperty("水库筛选项，是否绑定工程")
    private Boolean bindProj;
}
