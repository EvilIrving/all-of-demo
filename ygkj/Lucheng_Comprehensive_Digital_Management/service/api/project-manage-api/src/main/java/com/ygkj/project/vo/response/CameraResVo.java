package com.ygkj.project.vo.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("视频管理列表查询接口返回字段释义")
public class CameraResVo {

    @ApiModelProperty("视频编码")
    private String cameraIndexCode;

    @ApiModelProperty("视频名称")
    private String cameraName;

    @ApiModelProperty("状态")
    private String status;

    @ApiModelProperty("所属工程名称")
    private String projectName;

    @ApiModelProperty("所属工程类型")
    private String projectType;

    @ApiModelProperty("所属工程编码")
    private String projectCode;
    @ApiModelProperty("技术负责人")
    private String technologyPerson;
    @ApiModelProperty("技术负责人电话")
    private String technologyPersonMobile;
    @ApiModelProperty("工程等别")
    private String projectGrade;
    @ApiModelProperty("工程级别")
    private String projectScale;
    @ApiModelProperty("工程等别")
    private Integer outLineNumber;
    @ApiModelProperty("行政区划编码")
    private String areaCode;
}
