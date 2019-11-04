package com.zjg.blog.aspect;

import com.zjg.blog.annotation.CacheRemove;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Set;

/**
 * @author zjg
 *
 * 此切面用于处理清除缓存
 * 缺点：现在 @CacheRemove 不支持正则表达式
 * 标注此注解后，无论什么注解都会被删除，无法判断
 * 以后可以将其作为类似 springboot 自带的注解那种，
 * 使用 spel 加上各种判断条件
 */
@Component
@Aspect
public class CacheRemoveAspect {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private RedisTemplate redisTemplate;

    //截获标有@CacheRemove的方法的切点表达式
    @Pointcut(value = "(execution(* *.*(..)) && @annotation(com.zjg.blog.annotation.CacheRemove))")
    private void pointcut() {}

    @AfterReturning(value = "pointcut()")
    private void process(JoinPoint joinPoint) {
        //获取被代理类
        Object target = joinPoint.getTarget();
        //获取切入方法的数据
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        //获取切入方法
        Method method = signature.getMethod();
        //获取注解
        CacheRemove cacheRemove = method.getAnnotation(CacheRemove.class);

        if (cacheRemove.key() != null) {
            removeRedisfCache(cacheRemove.key());
        }
    }

    /**
     * 删除 key，key 可以使用正则
     * @param keys
     */
    private void removeRedisfCache(String[] keys) {
        for (String key : keys) {
            Set keySet = redisTemplate.keys(key);
            redisTemplate.delete(keySet);
            logger.info("清除缓存：{}" , keySet);
        }
    }

}
