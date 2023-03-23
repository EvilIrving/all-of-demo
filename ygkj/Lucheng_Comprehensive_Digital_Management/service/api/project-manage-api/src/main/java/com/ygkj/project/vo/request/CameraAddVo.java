package com.ygkj.project.vo.request;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class CameraAddVo {

    @ApiModelProperty("视频编码")
    private String cameraIndexCode;
    @ApiModelProperty("视频名称")
    private String cameraName;
    @ApiModelProperty("行政区划编码")
    private String areaCode;
    @ApiModelProperty("工程编码")
    private String projectCode;
}
