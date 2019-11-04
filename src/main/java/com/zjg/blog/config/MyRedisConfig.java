package com.zjg.blog.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.zjg.blog.pojo.Tag;
import com.zjg.blog.pojo.Type;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;

import java.net.UnknownHostException;
import java.util.List;

/**
 * @author zjg
 */
//@Configuration
public class MyRedisConfig {

    /*
        关闭spring boot jackson的FAIL_ON_EMPTY_BEANS
     */
    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper().disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
    }
    /**
     * 向容器中添加自己的 RedisTemplate<Object, Type> 修改序列化器
     * @param redisConnectionFactory
     * @return  RedisTemplate<Object, Type>
     * @throws UnknownHostException
     */
    @Bean   //方法名为 id
    public RedisTemplate<Object, Type> typeRedisTemplate(
            RedisConnectionFactory redisConnectionFactory)
            throws UnknownHostException {
        RedisTemplate<Object, Type> template = new RedisTemplate<Object, Type>();
        template.setConnectionFactory(redisConnectionFactory);
        Jackson2JsonRedisSerializer<Type> serializer = new Jackson2JsonRedisSerializer<Type>(Type.class);
        //设置ObjectMapper为自己定义的Bean  -- 解决SerializationFeature.FAIL_ON_EMPTY_BEANS
        serializer.setObjectMapper(objectMapper());

        template.setDefaultSerializer(serializer);
        return template;
    }
    @Bean   //方法名为 id
    public RedisTemplate<Object, Tag> tagRedisTemplate(
            RedisConnectionFactory redisConnectionFactory)
            throws UnknownHostException {
        RedisTemplate<Object, Tag> template = new RedisTemplate<Object, Tag>();
        template.setConnectionFactory(redisConnectionFactory);
        Jackson2JsonRedisSerializer<Tag> serializer = new Jackson2JsonRedisSerializer<Tag>(Tag.class);
        template.setDefaultSerializer(serializer);
        return template;
    }


    //CacheManagerCustomizers可以来定制缓存的一些规则
    @Primary    //指定这是主 cacheMange
    @Bean
    public RedisCacheManager typeCacheManager(RedisTemplate<Object, Type> typeRedisTemplate) {
        RedisCacheManager cacheManager = new RedisCacheManager(typeRedisTemplate);
        //给 key 设置一个前缀，默认会将CacheName作为key的前缀
        cacheManager.setUsePrefix(true);

        return cacheManager;
    }

    //CacheManagerCustomizers可以来定制缓存的一些规则
    @Bean
    public RedisCacheManager tagCacheManager(RedisTemplate<Object, Tag> tagRedisTemplate) {
        RedisCacheManager cacheManager = new RedisCacheManager(tagRedisTemplate);
        //给 key 设置一个前缀，默认会将CacheName作为key的前缀
        cacheManager.setUsePrefix(true);

        return cacheManager;
    }




    /*
        使用这个也可以是 type 类型的，序列化时存入的时 字符串，我猜测可能是使用的 toString()
        应该不是 toString() 因为 type 的 toString() 没有 blogs 而 redis 里的数据有这一项
        报异常：
            java.lang.ClassCastException:
                java.util.LinkedHashMap cannot be cast to com.zjg.blog.pojo.Type
        先老老实实为每一个 实体类写一个 template 和 manager 吧
     */

    /*
     * 在 springboot 自动配置的基础上，设置序列化器为 Jackson2JsonRedisSerializer
     * @param redisConnectionFactory
     * @return redisTemplate
     * @throws UnknownHostException
     */
    /*@Bean
    public RedisTemplate<Object, Object> redisTemplate(
            RedisConnectionFactory redisConnectionFactory)
            throws UnknownHostException {
        RedisTemplate<Object, Object> template = new RedisTemplate<Object, Object>();
        template.setConnectionFactory(redisConnectionFactory);
        //相对于 RedisAutoConfiguration 的配置，这里多了下面两行
        Jackson2JsonRedisSerializer<Object> serializer = new Jackson2JsonRedisSerializer<Object>(Object.class);
        template.setDefaultSerializer(serializer);
        return template;
    }*/


}
