package com.ygkj.project.vo.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel("统计摄像头在线、离线数量回参")
public class CountCameraStatus implements Serializable {
    @ApiModelProperty("在线状态:0为下线,1为在线")
    private String status;
    @ApiModelProperty("数量")
    private Integer num;
    @ApiModelProperty("总数")
    private Integer total;

}
