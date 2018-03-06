package com.wpx.service;

import com.wpx.dao.MessageDAO;
import com.wpx.model.Message;
import com.wpx.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.util.List;

/**
 * 消息
 */
@Service
public class MessageService {
    @Autowired
    private MessageDAO messageDAO;

    public int addMessage(Message message) {
        return messageDAO.addMessage(message);
    }

    public List<Message> getConversationDetail(String conversationId, int offset, int limit) {
        return messageDAO.getConversationDetail(conversationId, offset, limit);
    }

    public List<Message> getConversationList(int userId, int offset, int limit) {
        return messageDAO.getConversationList(userId, offset, limit);
    }

    //获取会话中消息总数
    public int getConversationCount(int from, int to) {
        String conversationId = from + "_" + to;
        return messageDAO.getConversationCount(conversationId);
    }

    public int getConversationUnReadCount(int userId, String conversationId) {
        return messageDAO.getConversationUnReadCount(userId, conversationId);
    }

    public void emptyUnreadCount(String conversationId) {
        messageDAO.updateConversationHasRead(conversationId);
    }

    public void deleteConversationById(Message msg) {
        messageDAO.deleteConversationById(msg);
    }

    public void deleteMessageById(Message msg) {
        messageDAO.deleteMessageById(msg);
    }


    public List<User> getUsersByFromId(int localUserId) {
        return messageDAO.getUsersByFromId(localUserId);
    }
}
