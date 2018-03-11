package com.wpx.controller;

import com.wpx.async.EventModel;
import com.wpx.async.EventProducer;
import com.wpx.async.EventType;
import com.wpx.model.EntityType;
import com.wpx.model.HostHolder;
import com.wpx.model.News;
import com.wpx.service.LikeService;
import com.wpx.service.NewsService;
import com.wpx.util.ToutiaoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 点赞
 */
@Controller
public class LikeController {
    @Autowired
    HostHolder hostHolder;

    @Autowired
    LikeService likeService;

    @Autowired
    NewsService newsService;

    @Autowired
    EventProducer eventProducer;

    /**
     * 赞
     *
     * @param newsId
     * @return
     */
    @RequestMapping(path = {"/like"}, method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public String like(@RequestParam("newsId") int newsId) {
        News news = newsService.getById(newsId);
        if (hostHolder.getUser()==null){
            return ToutiaoUtil.getJSONString(1);
        }
        int userId = hostHolder.getUser().getId();
        long likeCount = likeService.like(userId, EntityType.ENTITY_NEWS, newsId);
        //更新赞数
        newsService.updateLikeCount(newsId, (int) likeCount);

        eventProducer.fireEvent(new EventModel(EventType.LIKE)
                .setActorId(hostHolder.getUser().getId())
                .setEntityId(newsId)
                .setEntityType(EntityType.ENTITY_NEWS)
                .setEntityOwnerId(news.getUserId()));

        return ToutiaoUtil.getJSONString(0, String.valueOf(likeCount));
    }

    /**
     * 踩
     *
     * @param newsId
     * @return
     */
    @RequestMapping(path = {"/dislike"}, method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public String disLike(@RequestParam("newsId") int newsId) {
        if (hostHolder.getUser()==null){
            return ToutiaoUtil.getJSONString(1);
        }
        int userId = hostHolder.getUser().getId();
        long likeCount = likeService.disLike(userId, EntityType.ENTITY_NEWS, newsId);
        newsService.updateLikeCount(newsId, (int) likeCount);
        return ToutiaoUtil.getJSONString(0, String.valueOf(likeCount));
    }
}
