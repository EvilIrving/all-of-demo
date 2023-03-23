package com.ygkj.warning.model;

import java.io.Serializable;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author Huangzh
 * @description send_log
 * @date 2021-04-10
 */
@Data
public class SendLog implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private Long id;

    /**
     * 短信发送内容
     */
    private String content;

    /**
     * 短信发送号码，可能存在多个
     */
    private String receivePhones;

    /**
     * 发送时间
     */
    private Date sendTime;

    /**
     * 响应内容
     */
    private String sendResponse;

    public SendLog() {
    }

    public SendLog(String content, String receivePhones, Date sendTime, String sendResponse) {
        this.content = content;
        this.receivePhones = receivePhones;
        this.sendTime = sendTime;
        this.sendResponse = sendResponse;
    }
}

