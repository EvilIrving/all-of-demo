package com.ygkj.gragh.vo.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel("摄像头查询参数封装")
public class CameraQueryVo {

    @ApiModelProperty("摄像头名称")
    private String cameraName;

    @ApiModelProperty("行政区划代码")
    private String areaCode;

    @ApiModelProperty("摄像头状态：正常/异常")
    private String status;

    @ApiModelProperty("摄像头编码，查询单条记录的时候用")
    private String cameraIndexCode;

    @ApiModelProperty("工程编码，查询单个工程绑定的摄像头时用")
    private String projectCode;

    private List<String> projectCodes;
}
