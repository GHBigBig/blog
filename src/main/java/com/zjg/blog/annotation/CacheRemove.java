package com.zjg.blog.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author zjg
 * 功能描述：使用正则表达式删除缓存
 * 为了删除 cache 中所有的分页数据
 *
 * 缺点：key 不支持 spEL 表达式，需要写死
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CacheRemove {
    /**
     * 需要清除的 key，可以使用正则表达式。
     * @return
     */
    String[] key() default "";
}


