package com.ygkj.flood.vo.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author xq
 * @Description
 * @Date 2021/4/15
 */
@Data
@ApiModel("防洪减灾调度-降雨通用回参")
public class RainfallGenResVo {
    @ApiModelProperty("名称")
    String name;

    @ApiModelProperty("编码")
    String code;

    @ApiModelProperty("经度")
    String longitude;

    @ApiModelProperty("纬度")
    String latitude;

    @ApiModelProperty("累计降雨量,时长取决于入参")
    Double drp;

    @ApiModelProperty("1小时累计降雨量")
    Double drp1H;

    @ApiModelProperty("2小时累计降雨量")
    Double drp2H;

    @ApiModelProperty("3小时累计降雨量")
    Double drp3H;

    @ApiModelProperty("6小时累计降雨量")
    Double drp6H;

    @ApiModelProperty("12小时累计降雨量")
    Double drp12H;

    @ApiModelProperty("24小时累计降雨量")
    Double drp24H;

    @ApiModelProperty("72小时累计降雨量")
    Double drp72H;
}
