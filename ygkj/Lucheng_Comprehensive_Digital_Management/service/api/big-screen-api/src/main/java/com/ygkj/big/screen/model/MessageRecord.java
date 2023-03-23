package com.ygkj.big.screen.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Huangzh
 * @description 短信发送记录
 * @date 2021-06-19
 */
@Data
public class MessageRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private Integer id;

    /**
     * 短信内容
     */
    private String messageContent;

    /**
     * 短信批次号
     */
    private String batchId;

    /**
     * 接收手机号码
     */
    private String receivePhone;

    /**
     * 接收人
     */
    private String receivePerson;

    /**
     * 发送时间
     */
    private Date sendTime;

    /**
     * 短信发送预警类型
     */
    private String sendType;

    /**
     * 发送状态：已发送/未发送...
     */
    private String sendStatus;

    public MessageRecord() {
    }

    public MessageRecord(String messageContent, String batchId, String receivePhone,
                         String receivePerson, Date sendTime, String sendType, String sendStatus) {
        this.messageContent = messageContent;
        this.batchId = batchId;
        this.receivePhone = receivePhone;
        this.receivePerson = receivePerson;
        this.sendTime = sendTime;
        this.sendType = sendType;
        this.sendStatus = sendStatus;
    }
}

