package com.ygkj.big.screen.vo.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author xq
 * @Description
 * @Date 2021/8/17
 */
@Data
@ApiModel("场景查询入参")
public class BsSceneQueryReqVo {

    @ApiModelProperty("大屏名称")
    String bsName;

    @ApiModelProperty("场景名称")
    String sceneName;

    @ApiModelProperty("模式：1为大屏模式，2为PC模式")
    Integer mode;

    @ApiModelProperty("当前用户ID")
    String userId;

    @ApiModelProperty("是否默认")
    Integer defaulted;
}
