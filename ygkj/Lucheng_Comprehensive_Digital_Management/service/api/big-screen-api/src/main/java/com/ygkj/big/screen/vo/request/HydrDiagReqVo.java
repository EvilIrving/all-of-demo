package com.ygkj.big.screen.vo.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author xq
 * @Description
 * @Date 2021/6/2
 */
@Data
@ApiModel("水文工程测站概化图请求入参")
public class HydrDiagReqVo {
    @ApiModelProperty("概化图地图点位名称")
    String name;

    @ApiModelProperty("概化图地图点位对应的工程测站编码")
    String code;

    @ApiModelProperty("流域")
    String bas;

    @ApiModelProperty("类型")
    String type;

    @ApiModelProperty("复盘时间")
    Date dataTime;
}
