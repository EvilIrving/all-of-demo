package com.ygkj.project.vo.request;

import com.ygkj.entity.PageEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class AttStBaseReqVo extends PageEntity {

    @ApiModelProperty("测站编码")
    private String stCode;

    @ApiModelProperty("测站名称")
    private String stName;

    @ApiModelProperty("水文监测站类型（rr水库水位站，pp雨量站，dd闸坝水位站，tt潮位站，zg地下水位站，zq流量站，zz河道，ss墒情，pt泵站，es沉降.di位移）")
    private String stType;
}
