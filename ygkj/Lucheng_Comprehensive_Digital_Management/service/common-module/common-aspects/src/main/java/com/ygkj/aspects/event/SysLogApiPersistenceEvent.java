package com.ygkj.aspects.event;

import com.ygkj.common.notify.events.Event;
import com.ygkj.platform.management.model.SysApiLog;

public class SysLogApiPersistenceEvent implements Event {

    private SysApiLog sysApiLog;

    public SysLogApiPersistenceEvent(SysApiLog sysApiLog) {
        this.sysApiLog = sysApiLog;
    }

    @Override
    public String description() {
        return "系统api路由日志持久化事件";
    }

    @Override
    public Object data() {
        return sysApiLog;
    }
}
