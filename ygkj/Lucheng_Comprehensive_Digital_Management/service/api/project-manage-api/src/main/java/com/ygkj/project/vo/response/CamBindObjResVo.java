package com.ygkj.project.vo.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author xq
 * @Description
 * @Date 2021/8/25
 */
@Data
@ApiModel("摄像头绑定对象回参")
public class CamBindObjResVo {

    @ApiModelProperty("对象编码")
    String projectCode;

    @ApiModelProperty("对象名称")
    String projectName;

    @ApiModelProperty("经度")
    String longitude;

    @ApiModelProperty("纬度")
    String latitude;

    @ApiModelProperty("绑定对象类型")
    String projectType;

    @ApiModelProperty("行政区划编码")
    String adcd;

    @ApiModelProperty("流域")
    String bas;

    @ApiModelProperty("工程规模")
    String engScal;

    @ApiModelProperty("工程等别")
    String engGrad;
}
