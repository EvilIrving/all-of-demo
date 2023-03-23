package com.ygkj.big.screen.vo.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author xq
 * @Description
 * @Date 2021/5/1
 */
@Data
@ApiModel(description = "新版-水旱灾害防御-工程站点统计入参")
public class FddProjStaStatsReqVo {
    @ApiModelProperty("统计类型：工程、测站")
    String type;

    @ApiModelProperty("行政区划编码")
    String adcd;

    @ApiModelProperty("流域")
    String basin;
}
