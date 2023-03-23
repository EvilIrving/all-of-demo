package com.ygkj.visualization.vo.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.Data;

/**
 * @author xq
 * @Date 2021/2/26
 */
@Data
@ApiModel("气象可视化-点位收藏入参")
public class CollectPointAddReqVo {
    @ApiModelProperty("点位名称")
    String pointName;
    @ApiModelProperty("经度")
    Double longitude;
    @ApiModelProperty("纬度")
    Double latitude;
    @ApiModelProperty("地址")
    String location;
}
