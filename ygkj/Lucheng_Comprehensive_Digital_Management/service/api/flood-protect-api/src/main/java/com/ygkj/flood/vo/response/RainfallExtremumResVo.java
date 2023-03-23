package com.ygkj.flood.vo.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author xq
 * @Description 各个站点降雨量极值
 * @Date 2021/1/25
 */
@Data
@ApiModel("各个站点降雨量极值")
public class RainfallExtremumResVo {
    @ApiModelProperty("水文监测站代码")
    String stCode;
    @ApiModelProperty("水文监测站名称")
    String stName;
    @ApiModelProperty("历史极值")
    Double extremum;
    @ApiModelProperty("历史极值发生日期")
    Date occurredTime;
    @ApiModelProperty("历史极值持续时间")
    Integer durationTime;
}
