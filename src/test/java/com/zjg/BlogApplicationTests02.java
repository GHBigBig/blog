package com.zjg;

import com.github.pagehelper.PageInfo;
import com.zjg.blog.pojo.Tag;
import com.zjg.blog.pojo.Type;
import com.zjg.blog.service.TagService;
import com.zjg.blog.service.TypeService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Set;

/**
 * @author zjg
 * @create 2019-10-01 14:35
 */
/*@RunWith(SpringRunner.class)
@SpringBootTest*/
public class BlogApplicationTests02 {

    @Autowired
    RedisTemplate redisTemplate;
    @Autowired
    TypeService typeService;
    @Autowired
    TagService tagService;

    /*
        测试单个值的缓存
     */
    /*@Test
    public void test01() {
        Type type = typeService.findTypeByTypeId(8);
        System.out.println(type);
        System.out.println("------------------------------");
        type = typeService.findTypeByTypeId(8);
        System.out.println(type);
    }*/

    //测试集合类型的缓存
   /* @Test
    public void test02() {
        int pageNum = 0;
        int pageSize = 0;
        List<Type> typeAll = typeService.findTypeAll(pageNum, pageSize);
        System.out.println(typeAll);
        System.out.println("------------------------------");
        typeAll = typeService.findTypeAll(pageNum, pageSize);
        System.out.println(typeAll);
    }*/

    @Test
    public void test03() {
        Tag tagByTagId = tagService.findTagByTagId(1);
        System.out.println(tagByTagId);
        System.out.println("_______________________________");
        tagByTagId = tagService.findTagByTagId(1);
        System.out.println(tagByTagId);
    }

    //测试分页
    /*@Test
    public void test04() {
        int pageNum = 1;
        int pageSize = 5;
        Page<Type> types = PageHelper.startPage(pageNum, pageSize);
        List<Type> typeAll = typeService.findTypeAll(pageNum, pageSize);

        System.out.println(types);
        System.out.println(typeAll);
        System.out.println(types==typeAll);
        System.out.println(typeAll.getClass());
        for (Type type : typeAll) {
            System.out.println(type);
        }
        System.out.println("------------pageInfo------------");
        PageInfo<Type> pageInfo = new PageInfo<>(types);
        System.out.println(pageInfo);
        System.out.println("------------------------------");
        typeAll = typeService.findTypeAll(pageNum, pageSize);
        System.out.println(typeAll);
        System.out.println(typeAll.getClass());
        for (Type type : typeAll) {
            System.out.println(type);
        }
    }*/

    //测试 service 里分页
    @Test
    public void test05() {
        int pageNum = 1;
        int pageSize = 5;
        PageInfo<Type> pageInfo = typeService.findTypeAll(pageNum, pageSize);

        System.out.println(pageInfo);
        for (Type type : pageInfo.getList()) {
            System.out.println(type);
        }

        System.out.println("--------------------------");

        pageInfo = typeService.findTypeAll(pageNum, pageSize);

        System.out.println(pageInfo);
        for (Type type : pageInfo.getList()) {
            System.out.println(type);
        }
    }

    //测试删除
    @Test
    public void test06() {
        int i = typeService.removeTypeByTypeId(7);
        System.out.println(i);
    }

    //测试正则表达式
    @Test
    public void test07() {
        String key = "*TypeServiceImpl \\[*";
        Set keys = redisTemplate.keys(key);
        System.out.println(keys);
        for (Object o : keys) {
            System.out.println(o);
        }
    }

    //测试更新
    /*@Test
    public void test08() {
        Type type = typeService.findTypeByTypeId(8);
        System.out.println(type);
        System.out.println("----------------------");
        type.setTypeName(type.getTypeName() + " " + type.getTypeName());
        Type type1 = typeService.modifyType(type);
        System.out.println(type1);
    }*/

}