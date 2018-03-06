package com.wpx;

import com.wpx.dao.LoginTicketDAO;
import com.wpx.dao.NewsDAO;
import com.wpx.dao.UserDAO;
import com.wpx.model.LoginTicket;
import com.wpx.model.News;
import com.wpx.model.User;
import com.wpx.util.ToutiaoUtil;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;
import java.util.Random;
import java.util.UUID;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ToutiaoProjectApplication.class)
@Sql({"/init-schema.sql"})
public class InitDatabaseTests {
    @Autowired
    private UserDAO userDAO;

    @Autowired
    NewsDAO newsDAO;
    @Autowired
    LoginTicketDAO loginTicketDAO;

    /**
     * 添加管理员帐号
     */
    @Test
    public void InitDataBaseAdmin() {
        Random r = new Random();
        User user = new User();
        user.setHeadUrl(String.format("http://images.nowcoder.com/head/%dt.png", r.nextInt(1000)));
        user.setName("admin");
        user.setId(1);
        user.setSalt(UUID.randomUUID().toString().substring(0, 5));
        user.setPassword(ToutiaoUtil.MD5(123456 + user.getSalt()));
        userDAO.addUser(user);
    }

    @Test
    public void InitDataBase() {
        Random r = new Random();
        for (int i = 0; i < 11; i++) {
            User user = new User();
            user.setHeadUrl(String.format("http://images.nowcoder.com/head/%dt.png", r.nextInt(1000)));
            user.setName(String.format("USER%d", i));
            user.setSalt(UUID.randomUUID().toString().substring(0, 5));
            user.setPassword(ToutiaoUtil.MD5(123456 + user.getSalt()));
            userDAO.addUser(user);


            News news = new News();
            news.setCommentCount(i);
            Date date = new Date();
            date.setTime(date.getTime() + 1000 * 3600 * 5 * i);
            news.setCreatedDate(date);
            news.setImage(String.format("http://images.nowcoder.com/head/%dm.png", r.nextInt(1000)));
            news.setLikeCount(i + 1);
            news.setUserId(i + 1);
            news.setTitle(String.format("TITLE%d", i));
            news.setLink(String.format("http://www.nowcoder.com/%d.html", i));
            newsDAO.addNews(news);

            user.setPassword("newpassword");
            userDAO.updatePassword(user);

            LoginTicket ticket = new LoginTicket();
            ticket.setStatus(0);
            ticket.setUserId(i + 1);
            ticket.setExpired(date);
            ticket.setTicket(String.format("TICKET%d", i + 1));
            //loginTicketDAO.addLoginTicket(ticket);

            loginTicketDAO.updateStatus(ticket.getTicket(), 2);
        }
        Assert.assertEquals("newpassword", userDAO.selectById(1).getPassword());
        userDAO.deleteById(1);
        Assert.assertNull(userDAO.selectById(1));
    }

}
