package com.zjg;

import com.zjg.blog.pojo.Type;
import com.zjg.blog.service.TypeService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author zjg
 * @create 2019-09-26 23:03
 */

/*@RunWith(SpringRunner.class)
@SpringBootTest*/
public class BlogApplicationTests01 {

    @Autowired
    TypeService typeService;
    @Autowired
    RedisTemplate<String,Object> redisTemplate;

    /*@Test
    public void test01() {
        Type type = typeService.findTypeByTypeId(11);
        System.out.println(type);
        System.out.println("------------------------");
        typeService.findTypeByTypeId(11);
        System.out.println(type);
    }*/

    @Test
    public void test02() {
        Type type = new Type();
        type.setTypeName("tttttt");
        type.setTypeId(111l);
        redisTemplate.opsForValue().set("type-01", type);
    }
}
