package com.wpx;

import com.wpx.dao.CommentDAO;
import com.wpx.dao.LoginTicketDAO;
import com.wpx.dao.NewsDAO;
import com.wpx.dao.UserDAO;
import com.wpx.model.Comment;
import com.wpx.model.EntityType;
import com.wpx.model.News;
import com.wpx.model.User;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;

/**
 * 评论测试
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ToutiaoProjectApplication.class)
public class CommentTests {
    @Autowired
    private UserDAO userDAO;

    @Autowired
    NewsDAO newsDAO;
    @Autowired
    LoginTicketDAO loginTicketDAO;

    @Autowired
    CommentDAO commentDAO;

    @Test
    public void commentTest(){

        for(int i = 9;i<=11;i++){
            News news = newsDAO.selectById(i);
            for(int j=0;j<3;j++){
                Comment comment = new Comment();
                comment.setUserId(i+1);
                comment.setEntityId(news.getId());
                comment.setEntityType(EntityType.ENTITY_NEWS);
                comment.setStatus(0);
                comment.setCreatedDate(new Date());
                comment.setContent("Comment "+String.valueOf(j+1));
                commentDAO.addComment(comment);
            }
        }
        Assert.assertNotNull(commentDAO.selectByEntity(1,EntityType.ENTITY_NEWS).get(0));
    }
}
