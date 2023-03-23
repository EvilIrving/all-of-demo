package com.ygkj.warning.vo.request;

import com.ygkj.entity.PageEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("系统消息发送历史查询参数封装")
public class SysSendMessageQueryVo extends PageEntity {

    @ApiModelProperty("发送者id，前端忽略")
    private String fromUserId;

    @ApiModelProperty("发送者名称，前端忽略")
    private String fromUserName;

    @ApiModelProperty("接收者id，前端忽略")
    private String destinationUserId;

    @ApiModelProperty("接收者名称，前端忽略")
    private String destinationUserName;

    @ApiModelProperty("发送类型：浙政钉/短信/站内")
    private String sendType;

    @ApiModelProperty("消息类型：由业务系统自定义，比如（雨量预警、水文预警、OA报销流程...)")
    private String type;

    @ApiModelProperty("消息大类：提醒/预警")
    private String messageType;

    @ApiModelProperty("系统类型：数据管理系统/水文数字化系统...")
    private String system;

    @ApiModelProperty("处理状态：已处理、待处理、超时未处理")
    private String handleStatus;
    @ApiModelProperty("内容关键字，支持该字段的模糊搜索")
    private String content;
}

