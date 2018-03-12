package com.wpx.aspect;


import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


/**
 * 日志
 */
@Aspect
@Component
public class LogAspect {
    private static final Logger logger = LoggerFactory.getLogger(LogAspect.class);

    //第一个*代表匹配所有类型返回值，第二个*代表这个类所有方法。
    @Before("execution(* com.wpx.controller.HomeController.*(..))")
    public void beforeMethod(JoinPoint joinPoint) {
        StringBuilder sb = new StringBuilder();
        for (Object arg : joinPoint.getArgs()) {
            sb.append("arg:").append(arg.toString()).append("|");
        }

        logger.info("before method:" + sb.toString());
    }

    @After("execution(* com.wpx.controller.HomeController.*(..))")
    public void afterMethod() {
        logger.info("after method:");

    }
}
