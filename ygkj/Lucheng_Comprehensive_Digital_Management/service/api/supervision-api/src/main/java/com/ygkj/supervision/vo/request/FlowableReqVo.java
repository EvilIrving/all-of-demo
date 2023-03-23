package com.ygkj.supervision.vo.request;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 流程过程请求参数
 *
 * @author lxl
 * @date 2021/05/17
 */
@Data
@ApiModel
public class FlowableReqVo {

    /**
     * 流程主键
     */
    @ApiModelProperty("流程主键 --除了发起流程后续都需要传递")
    private String processId;

    /**
     * 事项编码
     */
    @ApiModelProperty("事项编码 --必须传递")
    private String matterCode;


    @ApiModelProperty("被检查用户id  --传递被检查人id")
    private String UserId;

}
