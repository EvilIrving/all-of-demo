package com.ygkj.project.vo.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class AttStResVo {

    @ApiModelProperty("水文监测站代码")
    private String stCode;


    @ApiModelProperty("水文监测站名称")
    private String stName;


    @ApiModelProperty("水文监测站类型（rr水库水位站，pp雨量站，dd闸坝水位站，tt潮位站，zg地下水位站，zq流量站，zz河道）")
    private String stType;


    @ApiModelProperty("水文监测站经度")
    private Double stLong;


    @ApiModelProperty("水文监测站纬度")
    private Double stLat;


    @ApiModelProperty("站址")
    private String stLoc;
}
