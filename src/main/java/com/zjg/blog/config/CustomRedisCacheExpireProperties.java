package com.zjg.blog.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zjg
 * 自定义的 redis 缓存中的过期时间属性
 */

@Data
@ConfigurationProperties(prefix = "spring.cache")
@Component
public class CustomRedisCacheExpireProperties {
    //该属性在 spring cache 框架自己的类中也会被获取
    //此处获取是为了对长度进行校验，防止  缓存名字-缓存时间  没有一一匹配
    private List<String> cacheNames;

    //缓存时间，和缓存名一一对应
    private List<Long> cacheExpires;

    /*
        生成 Map， 用来放入 RedisManager 中
     */
    public Map<String, Long> generateExireMap() {
        Map<String, Long> expireMap = new HashMap<>();
        /*
            校验参数值
         */
        //如果未配置 cacheNames 属性，返回空 map
        //如果未配置 cacheExpires 属性，也返回空 map
        if (CollectionUtils.isEmpty(cacheNames)||CollectionUtils.isEmpty(cacheExpires)) {
            return expireMap;
        }
        /*
            长度校验：只要数组不为空，有 x 个 cacheNames，就需要 x 个 cacheExpires， 如果某个 name 无需
                缓存时间，设置为 0 即可
            其内部实现就是使用 Map 生成若干个 RedisCacheMetadata，该对象和 cacheName 一一对应，并且其中
                的默认时间就是 0
            不对.我在redis中试了下,将key过期时间设为0或负数,该key会直接过期
            找了很久..没找到其判断过期时间的代码
         */
        if (cacheNames.size()!=cacheExpires.size()) {
            //此处随便抛出一个非法状态异常,可自定义异常抛出
            throw new IllegalStateException("cacheExpires 设置非法，cacheNames 和 cacheExpires 长度不一致。");
        }
        //遍历cacheNames
        for (int i = 0; i < cacheNames.size(); i++) {
            //只有当 cacheExpires 设置的大于 0 时，才放入 map
            Long expire = cacheExpires.get(i);
            if (expire>0) {
                expireMap.put(cacheNames.get(i), expire);
            }
        }
        return expireMap;
    }
}
