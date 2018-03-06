package com.wpx.async;

import java.util.List;

/**
 * 处理接口
 */
public interface EventHandler {
    //每个handler对事件的处理是不一样的，抽象成接口
    void doHandler(EventModel model);

    //这个EventHandler要关注哪些EventTpes的事件
    List<EventType> getSupportEventTypes();

}
