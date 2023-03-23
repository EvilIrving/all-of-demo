package com.ygkj.warning.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class MsgSendRecord {

    @ApiModelProperty("主键")
    @TableId(type = IdType.INPUT)
    private String id;

    @ApiModelProperty("接收人手机号 必填")
    private String receivePhone;

    @ApiModelProperty("接收人姓名")
    private String receiveName;

    @ApiModelProperty("发送人id")
    private String sendId;

    @ApiModelProperty("发送人姓名")
    private String sendName;

    @ApiModelProperty("发送人手机号")
    private String sendPhone;

    @ApiModelProperty("发送方式 1短信 2浙政钉 必填")
    private Integer sendType;

    @ApiModelProperty("所属业务 1一键督办 2责任人预警 3验证码,4预警模块")
    private Integer businessType;

    @ApiModelProperty("发送内容")
    private String context;

    @ApiModelProperty("发送时间")
    private Date createTime;

    @ApiModelProperty("发送结果 0成功 1失败")
    private Integer result = 1;

    @ApiModelProperty("错误信息")
    private String errMsg;
}
