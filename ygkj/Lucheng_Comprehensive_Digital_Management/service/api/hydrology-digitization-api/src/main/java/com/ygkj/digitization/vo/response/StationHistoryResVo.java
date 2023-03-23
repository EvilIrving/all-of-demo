package com.ygkj.digitization.vo.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("历史报表导出返回数据")
public class StationHistoryResVo {

    @ApiModelProperty("行政区划")
    private String adcd;

    @ApiModelProperty("名称")
    private String name;

    @ApiModelProperty("时间")
    private String tm;

    @ApiModelProperty("实时水位")
    private Double z;

    @ApiModelProperty("行政区划编码")
    private String stcd;

    @ApiModelProperty("闸上水位")
    private Double UPZ;

}
