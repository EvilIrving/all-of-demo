package com.ygkj.big.screen.drill.notify.event;

import com.ygkj.common.notify.events.Event;

/**
 * @author xq
 * @Description
 * @Date 2021/7/10
 */
public class RainfallDrillCaseEvent implements Event {

    private String data;

    public RainfallDrillCaseEvent(String data) {
        this.data = data;
    }

    @Override
    public String description() {
        return "降雨演练事件";
    }

    @Override
    public Object data() {
        return data;
    }
}
