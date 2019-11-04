package com.zjg.blog.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * 异常处理器的日志切面
 * @author zjg
 */
@Component
@Aspect
@Order(2)
public class HandlerLoggingAspect {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Pointcut("execution(* com.zjg.blog.handler.*.*(..))")
    public void declareJointPointExpression() {}

    @Before("declareJointPointExpression()")
    public void hanlderBefore(JoinPoint joinPoint) {
        logger.info("{}() args: {}", joinPoint.getSignature().getName(), joinPoint.getArgs());
    }
}
