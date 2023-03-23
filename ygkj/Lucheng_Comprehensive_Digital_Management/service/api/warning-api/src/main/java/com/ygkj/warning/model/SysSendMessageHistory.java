package com.ygkj.warning.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Date;

/**
 * @author Huangzh
 * @description 提醒消息处理历史记录表
 * @date 2021-07-13
 */
@Data
public class SysSendMessageHistory implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private Long id;

    /**
     * 消息id，关联到sys_send_message_log的id
     */
    @ApiModelProperty("消息id")
    @NotBlank(message = "请指定一条要操作的消息！")
    private String traceId;

    /**
     * 用户id
     */
    @ApiModelProperty("用户id，前端忽略")
    private String handleUserId;

    /**
     * 用户姓名
     */
    @ApiModelProperty("用户姓名，前端忽略")
    private String handleUserName;

    /**
     * 行为操作
     */
    @ApiModelProperty("行为操作")
    private String action;

    /**
     * 操作时间
     */
    @ApiModelProperty("前端忽略")
    private Date actionTime;

    public SysSendMessageHistory() {
    }

    public SysSendMessageHistory(String traceId, String handleUserId,
                                 String handleUserName, String action, Date actionTime) {
        this.traceId = traceId;
        this.handleUserId = handleUserId;
        this.handleUserName = handleUserName;
        this.action = action;
        this.actionTime = actionTime;
    }
}

