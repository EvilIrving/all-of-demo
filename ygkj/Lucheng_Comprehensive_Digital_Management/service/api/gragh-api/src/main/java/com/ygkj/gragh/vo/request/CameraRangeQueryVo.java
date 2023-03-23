package com.ygkj.gragh.vo.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@ApiModel("视频点范围查询参数封装")
public class CameraRangeQueryVo {

    @ApiModelProperty("中心点纬度")
    private double latitude;
    @ApiModelProperty("中心点经度")
    private double longitude;
    @ApiModelProperty("距离(1/2/3)，单位公里")
    private Integer distance;
}
