package com.wpx.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/error")
public class BaseErrorController implements ErrorController {

    Logger logger = LoggerFactory.getLogger(BaseErrorController.class);

    @Override
    public String getErrorPath() {
        logger.info("进入自定义错误页面");
        return "/error/error";
    }

    @RequestMapping
    public String error() {
        return getErrorPath();
    }

}
