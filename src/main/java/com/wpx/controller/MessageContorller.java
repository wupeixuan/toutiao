package com.wpx.controller;

import com.alibaba.fastjson.JSON;
import com.wpx.model.HostHolder;
import com.wpx.model.Message;
import com.wpx.model.User;
import com.wpx.model.ViewObject;
import com.wpx.service.MessageService;
import com.wpx.service.UserService;
import com.wpx.util.ToutiaoUtil;
import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

/**
 * 消息控制层
 */
@Controller
public class MessageContorller {
    private static final Logger logger = LoggerFactory.getLogger(MessageContorller.class);
    @Autowired
    MessageService messageService;
    @Autowired
    UserService userService;
    @Autowired
    HostHolder hostHolder;

    /**
     * 会话页面
     *
     * @param model
     * @return
     */
    @RequestMapping(path = {"/msg/list"}, method = {RequestMethod.GET})
    public String conversationList(Model model) {
        try {
            int localUserId = hostHolder.getUser().getId();
            List<ViewObject> conversations = new ArrayList<>();
            List<Message> conversationList = messageService.getConversationList(localUserId, 0, 10);
            for (Message msg : conversationList) {
                ViewObject vo = new ViewObject();
                vo.set("conversation", msg);
                int targetId = (msg.getFromId() == localUserId) ? msg.getToId() : msg.getFromId();
                int from = (targetId < localUserId) ? targetId : localUserId;
                int to = (targetId > localUserId) ? targetId : localUserId;
                int count = messageService.getConversationCount(from, to);
                vo.set("count", count);
                User user = userService.getUser(targetId);
                vo.set("user", user);
                vo.set("unreadCount", messageService.getConversationUnReadCount(localUserId, msg.getConversationId()));
                conversations.add(vo);
            }
            model.addAttribute("conversations", conversations);
        } catch (Exception e) {
            logger.error("获取会话失败 " + e.getMessage());
        }
        return "letter";
    }

    /**
     * 消息详情页
     *
     * @param model
     * @param conversationId
     * @return
     */
    @RequestMapping(path = {"/msg/detail"}, method = {RequestMethod.GET, RequestMethod.POST})
    public String conversationDetail(Model model, @Param("conversationId") String conversationId) {
        try {
            int localUserId = hostHolder.getUser().getId();
            List<Message> conversationList = messageService.getConversationDetail(conversationId, 0, 10);
            List<ViewObject> messages = new ArrayList<ViewObject>();
            for (Message msg : conversationList) {
                ViewObject vo = new ViewObject();
                vo.set("message", msg);
                int targetId = (msg.getFromId() == localUserId) ? msg.getToId() : msg.getFromId();
                User user = userService.getUser(msg.getFromId());
                if (user == null) {
                    continue;
                }
                vo.set("headUrl", user.getHeadUrl());
                vo.set("userId", user.getId());
                vo.set("fromId", targetId);
                vo.set("toId", localUserId);
                messages.add(vo);
            }
            model.addAttribute("messages", messages);
        } catch (Exception e) {
            logger.error("获取详情消息失败" + e.getMessage());
        }
        messageService.emptyUnreadCount(conversationId);
        return "letterDetail";
    }




    /**
     * 最近联系
     *
     * @return
     */
    @RequestMapping(path = {"/msg/latestSend"}, method = {RequestMethod.GET})
    @ResponseBody
    public String latestSend() {
        if (hostHolder.getUser()==null){
            return ToutiaoUtil.getJSONString(1, "没有最近");
        }
        int localUserId = hostHolder.getUser().getId();
        List<User> list = messageService.getUsersByFromId(localUserId);
        List<Object> userList = new ArrayList<>();
        for (User u :
                list) {
            userList.add(JSON.toJSONString(u));
        }
        if (list.isEmpty()) {
            return ToutiaoUtil.getJSONString(1, "没有最近");
        } else {
            Map<String, Object> map = new HashMap<>();
            map.put("list", userList);
            return ToutiaoUtil.getJSONString(0, map);
        }
    }

    /**
     * 实时查询用户名
     *
     * @param username
     * @return
     */
    @RequestMapping(path = {"/fuzzyqueryname"}, method = {RequestMethod.GET})
    @ResponseBody
    public String fuzzyqueryname(
            @RequestParam("username") String username) {
        List<User> list = userService.getUsersByName(username);
        Map<String, Object> map = new HashMap<>();
        map.put("list", JSON.toJSONString(list));
        if (map.isEmpty()) {
            return ToutiaoUtil.getJSONString(0, "用户名不存在");
        } else {
            return ToutiaoUtil.getJSONString(0, map);
        }


    }

    /**
     * 通过name获取User
     */
    @RequestMapping(value = {"/valname"}, method = {RequestMethod.POST})
    @ResponseBody
    public String valName(
            @RequestParam("username") String username) {
        try {
            User user = userService.getUserByName(username);
            if (user != null) {
                Map<String, Object> map = new TreeMap<>();
                map.put("user", user);
                return ToutiaoUtil.getJSONString(0, map);
            } else {
                return ToutiaoUtil.getJSONString(1, "用户名不存在");
            }
        } catch (Exception e) {
            logger.error("查找失败" + e.getMessage());
            return ToutiaoUtil.getJSONString(1, "查询异常");
        }
    }

    /**
     * 发送私信
     *
     * @param username
     * @param content
     * @return
     */
    @RequestMapping(value = {"/msg/send"}, method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public String addMsg(@RequestParam("username") String username,
                         @RequestParam("content") String content) {
        try {
            int localUserId = hostHolder.getUser().getId();
            User toUser = userService.getUserByName(username);
            int targetId = toUser.getId();
            Message msg = new Message();
            msg.setContent(content);
            msg.setFromId(localUserId);
            msg.setToId(targetId);
            msg.setCreatedDate(new Date());
            msg.setConversationId(localUserId < targetId ? String.format("%d_%d", localUserId, targetId) : String.format("%d_%d", targetId, localUserId));
            messageService.addMessage(msg);
            return ToutiaoUtil.getJSONString(msg.getId());
        } catch (Exception e) {
            logger.error("增加消息失败" + e.getMessage());
            return ToutiaoUtil.getJSONString(1, "插入消息失败");
        } finally {
            return "letter";
        }
    }

    /**
     * 回复私信
     *
     * @param fromId
     * @param toId
     * @param content
     * @return
     */
    @RequestMapping(path = {"/msg/add"}, method = {RequestMethod.POST})
    public String add(@RequestParam("fromId") int fromId, @RequestParam("toId") int toId,
                      @RequestParam("content") String content) {
        int localUserId = hostHolder.getUser().getId();
        int targetId = (toId == localUserId) ? fromId : toId;
        try {
            Message msg = new Message();
            msg.setContent(content);
            msg.setFromId(localUserId);
            msg.setToId(targetId);
            msg.setCreatedDate(new Date());
            msg.setConversationId(localUserId < fromId ? String.format("%d_%d", localUserId, targetId) : String.format("%d_%d", targetId, localUserId));
            messageService.addMessage(msg);
            return ToutiaoUtil.getJSONString(msg.getId());
        } catch (Exception e) {
            logger.error("增加消息失败" + e.getMessage());
            return ToutiaoUtil.getJSONString(1, "插入消息失败");
        } finally {
            String conversationId = localUserId < targetId ? String.format("%d_%d", localUserId, targetId) : String.format("%d_%d", targetId, localUserId);
            return "redirect:/msg/detail?conversationId=" + conversationId;
        }
    }

    /**
     * 删除会话
     */
    @RequestMapping(value = {" /msg/delcvst"}, method = {RequestMethod.POST})
    @ResponseBody
    public String deleteConversation(@RequestParam("conversationId") String conversationId) {
        try {
            Message msg = new Message();
            msg.setConversationId(conversationId);
            messageService.deleteConversationById(msg);
        } catch (Exception e) {
            logger.error("删除会话失败" + e.getMessage());
            return ToutiaoUtil.getJSONString(1, "删除会话失败");
        }
        return "letter";
    }

    /**
     * 删除消息
     */
    @RequestMapping(value = {" /msg/delete"}, method = {RequestMethod.POST})
    @ResponseBody
    public String deleteMessage(@RequestParam("msgId") int msgId) {
        try {
            Message msg = new Message();
            msg.setId(msgId);
            messageService.deleteMessageById(msg);
        } catch (Exception e) {
            logger.error("删除消息失败" + e.getMessage());
            return ToutiaoUtil.getJSONString(1, "删除消息失败");
        }
        return "letterDetail";
    }
}
