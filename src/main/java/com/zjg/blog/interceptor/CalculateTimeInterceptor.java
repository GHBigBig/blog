package com.zjg.blog.interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * @author zjg
 * 计算一次请求响应的时间
 */
public class CalculateTimeInterceptor extends HandlerInterceptorAdapter {

    private static final Logger logger = LoggerFactory.getLogger(CalculateTimeInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //虽然不用在这里打印是时间，计时也不太准确，但是这里进一步了解 jdk8 时间api 的使用
        /*DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
        LocalDateTime time = LocalDateTime.now();
        long startTime = time.toInstant(OffsetDateTime.now().getOffset()).toEpochMilli();
        String localDateTime = df.format(time);*/

        long startTime = System.currentTimeMillis();

        logger.info("请求的 URL::{}::{} :: 开始计时......" ,request.getRequestURL().toString(), request.getMethod());
        request.setAttribute("startTime", startTime);

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        long startTime = (long) request.getAttribute("startTime");
        long endTime = System.currentTimeMillis() - startTime;
        logger.info("请求的 URL::{}::{} :: handler 方法相应时间 {}", request.getRequestURL().toString(), request.getMethod(), endTime);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        long startTime = (long) request.getAttribute("startTime");

        ////虽然不用在这里打印是时间，计时也不太准确，但是这里进一步了解 jdk8 时间api 的使用
        /*DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
        LocalDateTime time = LocalDateTime.now();
        long endTime = time.toInstant(OffsetDateTime.now().getOffset()).toEpochMilli();
        String localDateTime = df.format(time);*/

        logger.info("请求的 URL::{}::{} :: 请求耗时 {}", request.getRequestURL().toString(), request.getMethod(), (System.currentTimeMillis()-startTime));

    }
}
