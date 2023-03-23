package com.ygkj.big.screen.drill.notify.event;

import com.ygkj.common.notify.events.Event;

/**
 * @author xq
 * @Description
 * @Date 2021/7/10
 */
public class RsvrDrillCaseEvent implements Event {

    private String data;

    public RsvrDrillCaseEvent(String data) {
        this.data = data;
    }

    @Override
    public String description() {
        return "";
    }

    @Override
    public Object data() {
        return data;
    }
}
