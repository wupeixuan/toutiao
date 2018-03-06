package com.wpx;

import com.wpx.model.User;
import com.wpx.util.JedisAdapter;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * JedisTest
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ToutiaoProjectApplication.class)
public class JedisTests {
    @Autowired
    JedisAdapter jedisAdapter;
    @Test
    public void testObject(){
        User user = new User();
        user.setHeadUrl("http://image.nowcoder.com/head/100t.png");
        user.setName("user1");
        user.setPassword("pwd");
        user.setSalt("salt");
        jedisAdapter.setObject("user1xx",user);

        User u = jedisAdapter.getObject("user1xx",User.class);
        System.out.println(ToStringBuilder.reflectionToString(u));
    }
}
