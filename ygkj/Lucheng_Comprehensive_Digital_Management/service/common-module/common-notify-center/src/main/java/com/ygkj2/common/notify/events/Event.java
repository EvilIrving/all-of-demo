package com.ygkj.common.notify.events;

/**
 * @author Huang.zh
 * @date 2021/4/9 18:05
 * @Description: 通用事件抽象
 */
public interface Event {

    String description();

    Object data();

}
