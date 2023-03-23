package com.ygkj.flood.vo.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author xq
 * @Description 各行政区划累计降雨量合计
 * @Date 2021/1/18
 */
@Data
@ApiModel("各行政区划累计降雨量合计")
public class AdRealtimeRainResVo {
    @ApiModelProperty("行政区划名称")
    String adnm;
    @ApiModelProperty("行政区划编码")
    String adcd;
    @ApiModelProperty("累计降雨量")
    Double drpSum;
}
