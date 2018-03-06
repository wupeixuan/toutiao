package com.wpx.async.handler;

import com.wpx.async.EventHandler;
import com.wpx.async.EventModel;
import com.wpx.async.EventType;
import com.wpx.model.Message;
import com.wpx.model.User;
import com.wpx.service.MessageService;
import com.wpx.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * 点赞处理
 */
@Component
public class LikeHandler implements EventHandler {
    @Autowired
    UserService userService;

    @Autowired
    MessageService messageService;

    @Override
    public void doHandler(EventModel model) {
        Message message = new Message();
        //Admin账户
        message.setFromId(1);
        message.setToId(model.getEntityOwnerId());
        User user = userService.getUser(model.getActorId());
        message.setContent("用户" + user.getName() + "赞了你的咨询，http://127.0.0.1:8080/news/" + String.valueOf(model.getEntityId()));
        message.setCreatedDate(new Date());
        message.setConversationId("1_" + user.getId());
        messageService.addMessage(message);
    }

    @Override
    public List<EventType> getSupportEventTypes() {
        return Arrays.asList(EventType.LIKE);
    }
}
