package com.ygkj.auth.VO.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: fml
 * @Date: 2020/10/10 11:40
 * @Description: 选择操作人员
 */
@Data
public class ChooseOperatorReqVo {

    @ApiModelProperty(value = "步骤(1企业申请，2经办人受理，3审批，4领导签批，5校对)")
    private Integer step;

    @ApiModelProperty(value = "操作人id，以逗号隔开")
    private String operator;
}
