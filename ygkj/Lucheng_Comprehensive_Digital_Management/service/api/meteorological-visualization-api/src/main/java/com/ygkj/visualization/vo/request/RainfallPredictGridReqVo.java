package com.ygkj.visualization.vo.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author xq
 * @Description
 * @Date 2021/9/15
 */
@Data
@ApiModel("短临预报入参")
public class RainfallPredictGridReqVo {
    @ApiModelProperty("统计类型：行政区划/流域")
    String type;
}
