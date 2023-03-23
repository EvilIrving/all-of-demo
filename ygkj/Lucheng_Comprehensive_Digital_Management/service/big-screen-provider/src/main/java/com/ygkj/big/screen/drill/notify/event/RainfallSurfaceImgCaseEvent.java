package com.ygkj.big.screen.drill.notify.event;

import com.ygkj.common.notify.events.Event;

/**
 * @author xq
 * @Description
 * @Date 2021/8/27
 */
public class RainfallSurfaceImgCaseEvent implements Event {

    private String data;

    public RainfallSurfaceImgCaseEvent(String data) {
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

