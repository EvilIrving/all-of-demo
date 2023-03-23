package com.ygkj.common.notify.events;

/**
 * @author Huang.zh
 * @date 2021/4/10 12:01
 * @Description: 短信发送事件
 */
public class SmsSendEvent implements Event {

    private Object data;

    public SmsSendEvent(Object data) {
        this.data = data;
    }

    @Override
    public String description() {
        return "短信机发送事件";
    }

    @Override
    public Object data() {
        return data;
    }
}
