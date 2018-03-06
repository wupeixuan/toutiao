package com.wpx.controller;

import com.wpx.async.EventModel;
import com.wpx.async.EventProducer;
import com.wpx.async.EventType;
import com.wpx.service.UserService;
import com.wpx.util.ToutiaoUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * 登录
 */
@Controller
public class LoginController {
    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);
    @Autowired
    UserService userService;

    @Autowired
    EventProducer eventProducer;

    /**
     * 注册
     *
     * @param username
     * @param password
     * @param remember
     * @param response
     * @return
     */
    @RequestMapping(value = {"/reg/"}, method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public String reg(@RequestParam("username") String username,
                      @RequestParam("password") String password,
                      @RequestParam(value = "rember", defaultValue = "0") int remember,
                      HttpServletResponse response) {
        try {
            Map<String, Object> map = userService.register(username, password);
            if (map.containsKey("ticket")) {
                Cookie cookie = new Cookie("ticket", map.get("ticket").toString());
                cookie.setPath("/");
                if (remember > 0) {
                    cookie.setMaxAge(3600 * 24 * 5);
                }
                response.addCookie(cookie);
                return ToutiaoUtil.getJSONString(0, "注册成功");
            } else {
                return ToutiaoUtil.getJSONString(1, map);
            }

        } catch (Exception e) {
            logger.error("注册异常", e.getMessage());
            return ToutiaoUtil.getJSONString(1, "注册异常");
        }

    }

    /**
     * 登录
     *
     * @param username
     * @param password
     * @param remember
     * @param response
     * @return
     */
    @RequestMapping(path = {"/login/"}, method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public String login(@RequestParam("username") String username,
                        @RequestParam("password") String password,
                        @RequestParam(value = "rember", defaultValue = "0") int remember,
                        HttpServletResponse response) {
        try {
            Map<String, Object> map = userService.login(username, password);
            if (map.containsKey("ticket")) {
                Cookie cookie = new Cookie("ticket", map.get("ticket").toString());
                cookie.setPath("/");
                if (remember > 0) {
                    cookie.setMaxAge(3600 * 24 * 5);
                }
                response.addCookie(cookie);
                eventProducer.fireEvent(new EventModel(EventType.LOGIN)
                        .setActorId((int) map.get("userId"))
                        .setExt("username", username)
                        .setExt("email", "928403374@qq.com"));
                return ToutiaoUtil.getJSONString(0, "成功");
            } else {
                return ToutiaoUtil.getJSONString(1, map);
            }
        } catch (Exception e) {
            logger.error("登陆异常", e.getMessage());
            return ToutiaoUtil.getJSONString(1, "登陆异常");
        }

    }

    /**
     * 退出
     *
     * @param ticket
     * @return
     */
    @RequestMapping(path = {"/logout/"}, method = {RequestMethod.POST, RequestMethod.GET})
    public String logout(@CookieValue("ticket") String ticket) {
        //ticket来自于cookie，当我访问logout的时候呢，从cookie里面把ticket读取出来，然后把status改掉
        userService.logout(ticket);
        return "redirect:/";
    }


}
