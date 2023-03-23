package com.ygkj.digitization.vo.request;

import com.ygkj.digitization.model.PageEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class StationQueryVo extends PageEntity {

    @ApiModelProperty("测站名称")
    private String stationName;

    @ApiModelProperty("行政区划编码")
    private String areaCode;

    @ApiModelProperty("测站编码")
    private String stationCode;

    @ApiModelProperty("测站类型，RR水库水位站,ZZ河道水位站,DD闸坝,工情直接传中文类型（泵站/水闸）")
    private String stationType;

    @ApiModelProperty("开始时间")
    private String startTime;

    @ApiModelProperty("结束时间")
    private String endTime;

    @ApiModelProperty("是否重点关注")
    private Boolean isFocus;

    @ApiModelProperty("是否显示")
    private Boolean display;

    @ApiModelProperty("工程编码")
    private String projectCode;

    @ApiModelProperty("河道水位类型：平原河网/山区河道/其他")
    private String riverStationType;
}
