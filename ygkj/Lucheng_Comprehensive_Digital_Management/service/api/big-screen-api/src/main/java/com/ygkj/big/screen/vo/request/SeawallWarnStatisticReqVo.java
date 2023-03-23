package com.ygkj.big.screen.vo.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author xq
 * @Description
 * @Date 2021/7/2
 */
@Data
@ApiModel("海塘报警统计入参")
public class SeawallWarnStatisticReqVo {
    @ApiModelProperty("是否演练，true为演练")
    Boolean drill = false;

    @ApiModelProperty("海塘演练表单入参")
    List<SeawallDrillFormReqVo> formList;
}
