package com.ygkj.project.vo.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author xq
 * @Description
 * @Date 2021/8/1
 */
@Data
@ApiModel("海康摄像头统计回参")
public class HikCamStatsResVo {

    @ApiModelProperty("行政区划编码")
    String areaCode;

    @ApiModelProperty("行政区划名称")
    String areaName;

    @ApiModelProperty("流域")
    String bas;

    @ApiModelProperty("工程类型")
    String projectType;

    @ApiModelProperty("全部数量")
    Integer totalNum = 0;
    @ApiModelProperty("在线数量")
    Integer onlineNum = 0;
    @ApiModelProperty("离线数量")
    Integer offlineNum = 0;
    @ApiModelProperty("在线率")
    Integer onlineRate = 0;

    public void computeOnlineRate() {
        if (totalNum != null && totalNum > 0 && onlineNum != null) {
            onlineRate = onlineNum * 100 / totalNum;
        }
    }
}
