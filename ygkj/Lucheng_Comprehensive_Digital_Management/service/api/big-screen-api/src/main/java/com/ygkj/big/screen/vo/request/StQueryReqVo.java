package com.ygkj.big.screen.vo.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author xq
 * @Description
 * @Date 2021/4/15
 */
@Data
@ApiModel("测站查询入参")
public class StQueryReqVo {

    @ApiModelProperty("测站编码")
    String stCode;

    @ApiModelProperty("测站名")
    String stName;

    @ApiModelProperty("测站类型")
    String stType;

    @ApiModelProperty("流域名")
    String bas;
}
