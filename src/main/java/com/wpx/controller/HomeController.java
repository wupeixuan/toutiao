package com.wpx.controller;

import com.wpx.model.*;
import com.wpx.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;


/**
 * 主页
 */
@Controller
public class HomeController {
    private static final Logger logger = LoggerFactory.getLogger(MessageContorller.class);

    @Autowired
    ToutiaoService toutiaoService;

    @Autowired
    UserService userService;

    @Autowired
    HostHolder hostHolder;

    @Autowired
    LikeService likeService;

    @Autowired
    MessageService messageService;


    @RequestMapping(path = {"/", "/index"}, method = {RequestMethod.GET, RequestMethod.POST})
    public String index(Model model,
                        @RequestParam(value = "pop", defaultValue = "0") int pop, @RequestParam(value = "page", defaultValue = "1") int page) {
        try {
            if (hostHolder.getUser()!=null){
                int localUserId = hostHolder.getUser().getId();
                List<ViewObject> conversations = new ArrayList<>();
                List<Message> conversationList = messageService.getConversationList(localUserId, 0, 30);
                for (Message msg : conversationList) {
                    ViewObject vo = new ViewObject();
                    vo.set("unreadCount", messageService.getConversationUnReadCount(localUserId, msg.getConversationId()));
                    conversations.add(vo);
                }
                model.addAttribute("conversations", conversations);
            }
        } catch (Exception e) {
            logger.error("获取站内信列表失败 " + e.getMessage());
        } finally {
            model.addAttribute("vos", getNews(0, (page - 1) * 10, page * 30));
            model.addAttribute("pop", pop);
            return "home";
        }
    }

    /**
     * 用户发布的资讯
     *
     * @param userId
     * @param model
     * @return
     */
    @RequestMapping(path = {"/user/{userId}"}, method = {RequestMethod.GET, RequestMethod.POST})
    public String userIndex(@PathVariable("userId") int userId,
                            Model model) {
        model.addAttribute("vos", getNews(userId, 0, 10));
        return "home";
    }


    /**
     * 加载资讯
     *
     * @param userId
     * @param offset
     * @param limit
     * @return
     */
    private List<ViewObject> getNews(int userId, int offset, int limit) {
        List<News> newsList = toutiaoService.getLatestNews(userId, offset, limit);
        int localUserId = hostHolder.getUser() != null ? hostHolder.getUser().getId() : 0;
        List<ViewObject> vos = new ArrayList<>();
        for (News news : newsList) {
            ViewObject vo = new ViewObject();
            vo.set("news", news);
            vo.set("user", userService.getUser(news.getUserId()));

            if (localUserId != 0) {
                vo.set("like", likeService.getLikeStatus(localUserId, EntityType.ENTITY_NEWS, news.getId()));
            } else {
                vo.set("like", 0);
            }
            vos.add(vo);
        }
        return vos;
    }
}
