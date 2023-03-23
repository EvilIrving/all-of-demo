package com.ygkj.gragh.vo.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author xq
 * @Description
 * @Date 2021/4/21
 */
@Data
@ApiModel(description = "水质监测查询入参")
public class BswqReqVo {
    @ApiModelProperty("行政区划编码")
    String adcd;
    @ApiModelProperty("测站名称")
    String name;
}
