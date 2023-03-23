package com.ygkj.project.vo.request;

import com.ygkj.entity.PageEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class MonitoringStationReqVo extends PageEntity {

    @ApiModelProperty("监测站点名称")
    private String stName;

    @ApiModelProperty("状态 0离线,1在线")
    private Integer status;

    @ApiModelProperty("监测站点类型 RR水库水位站，ZZ河道，DD闸坝水位站，TT潮位站，PP雨量站，SC视频")
    private String stType;
}
