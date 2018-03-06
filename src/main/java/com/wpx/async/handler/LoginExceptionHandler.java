package com.wpx.async.handler;

import com.wpx.async.EventHandler;
import com.wpx.async.EventModel;
import com.wpx.async.EventType;
import com.wpx.model.Message;
import com.wpx.model.User;
import com.wpx.service.MessageService;
import com.wpx.service.UserService;
import com.wpx.util.MailSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * 登录异常处理
 */
@Component
public class LoginExceptionHandler implements EventHandler {
    @Autowired
    MessageService messageService;
    @Autowired
    UserService userService;
    @Autowired
    MailSender mailSender;

    @Override
    public void doHandler(EventModel model) {
        //判断是否有异常登录
        Message message = new Message();
        User user = userService.getUser(model.getActorId());
        message.setToId(model.getActorId());
        message.setContent("您好，欢迎您的登录");
        message.setFromId(1);
        message.setCreatedDate(new Date());
        message.setConversationId("1_" + user.getId());
        messageService.addMessage(message);

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("username", model.getExt("username"));
        mailSender.sendWithHTMLTemplate(model.getExt("email"), "欢迎登录", "mails/welcome.html", map);
    }

    @Override
    public List<EventType> getSupportEventTypes() {
        return Arrays.asList(EventType.LOGIN);
    }
}
