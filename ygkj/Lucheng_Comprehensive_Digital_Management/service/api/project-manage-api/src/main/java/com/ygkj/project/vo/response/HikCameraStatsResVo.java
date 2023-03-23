package com.ygkj.project.vo.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author xq
 * @Description
 * @Date 2021/4/14
 */
@Data
@ApiModel("摄像头根据工程/行政区划/流域统计回参")
public class HikCameraStatsResVo {
    @ApiModelProperty("在线数量")
    List<ChartResVo<String, Long>> onlineNum;
    @ApiModelProperty("全部数量")
    List<ChartResVo<String, Long>> totalNum;
    @ApiModelProperty("离线数量")
    List<ChartResVo<String, Long>> offlineNum;
}
