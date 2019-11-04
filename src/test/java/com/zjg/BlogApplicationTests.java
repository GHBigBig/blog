package com.zjg;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zjg.blog.mapper.TagMapper;
import com.zjg.blog.mapper.TypeMapper;
import com.zjg.blog.pojo.Tag;
import com.zjg.blog.pojo.Type;
import com.zjg.blog.service.TagService;
import com.zjg.blog.service.TypeService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.Cache;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

/*@RunWith(SpringRunner.class)
@SpringBootTest*/
public class BlogApplicationTests {

    /*
     * @Autowired按byType自动注入  按类型匹配注入
     * 若某一属性允许不被设置, 可以设置 @Authwired
     * 注解的 required 属性为 false
     */
    @Autowired
    private TypeService typeService;
    @Autowired
    private TypeMapper typeMapper;
    //key 和 value 都是 String 类型的
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    //key 和 value 都是 Object 类型的
    @Autowired
    @Qualifier(value = "redisTemplate")
    private RedisTemplate redisTemplate;
    /*@Autowired
    @Qualifier(value = "typeRedisTemplate")
    private RedisTemplate<Object, Type> typeRedisTemplate;*/
    @Autowired
    private TagService tagService;
    @Autowired
    private RedisCacheManager tagCacheManager;



    @Test
    public void contextLoads() {
        try {
            FileInputStream in = new FileInputStream("atguigushk.txt");
            int b; b = in.read();
            while (b != -1) {
                System.out.print((char) b);
                b = in.read();
            }
            in.close();
        } catch (IOException e) {
            //发生异常的请款下 catch 还是先执行的
            System.out.println(e);
        } finally {
            System.out.println(" It’s ok!");
        }
    }

    /*
        Throwable
     */
    /*@Test
    public void testException() {
        try {
            methodA();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("--------------------------------");
            e.printStackTrace();
            System.out.println("------------------------------");
            System.out.println(e);
        }
        methodB();
    }
    void methodA() {
        try {
            System.out.println("进入方法A");
            throw new RuntimeException("制造异常");
        }finally {
            System.out.println("用A方法的finally");
        }
    }
    void methodB() {
        try {
            System.out.println("进入方法B");
            return;
        } finally {
            System.out.println("调用B方法的finally");
        } }*/

    /**
     * 测试mybatis 自增主键
     */
    /*@Test
    public void test01() {
        Type type = new Type();
        type.setTypeName("-----类型---------");
        int i = typeMapper.insertType(type);
        System.out.println(i);
        System.out.println(type);
    }*/

    /**
     * 测试 springboot 的默认配置的 SimpleCacheConfiguration
     */
    /*@Test
    public void test02() {
        Type typeByTypeId = typeService.findTypeByTypeId(22);
        System.out.println(typeByTypeId);
        System.out.println("-------------------------------");
        typeByTypeId = typeService.findTypeByTypeId(22);
        System.out.println(typeByTypeId);
    }*/

    /*
        测试 @CachePut
        此注解，保证方法被调用，结果被缓存
     */
    /*@Test
    public void testCachePut() {
        Type type = typeService.findTypeByTypeId(22);
        System.out.println(type);
        System.out.println("type的名字被修改为 本来的名字 + | ");

        Type type1 = new Type();
        type1.setTypeId(type.getTypeId());
        type1.setTypeName(type.getTypeName() + "| ");

        type1 = typeService.modifyType(type1);
        if (type1 != null) {
//            Type type1 = typeService.findTypeByTypeId(22);
            //这里会输出 type ， 可能是因为 findTypeByTypeId() 被 @Cacheable 标注了，
            //所以 返回的值还是 type 的内存地址， type1==type 应该为 true
            //typ1 和 type 都是用 findTypeByTypeId() 获取的
            System.out.println(type1);
            System.out.println("--------------------");

//            System.out.println(type == type1);
        }
        Type typeByTypeId = typeService.findTypeByTypeId(22l);
        System.out.println(typeByTypeId);


    }*/

    /*@Test
    public void testCacheEvict() {
        Type typeByTypeId = typeService.findTypeByTypeId(22);
        System.out.println(typeByTypeId);
        int i = typeService.removeTypeByTypeId(22);
        if (i>0) {
            typeByTypeId = typeService.findTypeByTypeId(22);
            System.out.println(typeByTypeId);
        }
    }*/

    //序列：默认使用 jdk 自带序列化器
    /*@Test
    public void testRedis() {
        Type type = typeService.findTypeByTypeId(1);
        redisTemplate.opsForValue().set("type-01", type);
    }*/

    //序列化：使用自己配置的 jackson 序列化器
    /*
        1，将数据以 json 的方式保存
            自己将对象转为 json
            改变 redisTemplate 序列化规则
     */
    /*@Test
    public void testRedis01() {
        *//*Type type = typeService.findTypeByTypeId(1);
        typeRedisTemplate.opsForValue().set("type-03", type);*//*
        Type type = typeService.findTypeByTypeId(1);
        redisTemplate.opsForValue().set("type-01", type);
    }*/

    /*
        测试 redis 作为缓存
     */
    /*@Test
    public void testRedis02() {
        Type type = typeService.findTypeByTypeId(11);
        System.out.println(type);
        Type type1 = typeService.findTypeByTypeId(11);
        System.out.println(type1);
        System.out.println(type == type1);
    }*/

    /*
        测试 redis 作为缓存，cacheManager(RedisTemplate<Object, Type> 作为 cacheManger 时
            tag 类型的存取问题，存时没有问题，取出时报异常：
            org.springframework.data.redis.serializer.SerializationException: Could not read JSON:
            Unrecognized field "tagId" (class com.zjg.blog.pojo.Type),
            not marked as ignorable (3 known properties: "blogs", "typeId", "typeName"])
        应该在创建一个 cacheManager(RedisTemplate<Object, Tag> 类型的 cacheManage

     */
    @Test
    public void testRedis03() {
        Tag tag = tagService.findTagByTagId(2);
        System.out.println(tag);
        Tag tag1 = tagService.findTagByTagId(2);
        System.out.println(tag1);
    }

    /*
        编码操作 cacheManager
     */
//    @Test
////    public void testRedis04() {
////        Tag tag = new Tag();
////        tag.setTagName("tag1111");
////        tag.setTagId(1111);
////        Cache tagtagtag = tagCacheManager.getCache("tagtagtag");
////        tagtagtag.put("tag:1111", tag);
////    }

    /*
        测试 pagehelp 分页
     */
    /*@Test
    public void testPageHelper() {
        int pageNum = 1;
        int pageSize = 5;
        Page<Object> page = PageHelper.startPage(pageNum, pageSize);
        List<Type> typeAll = typeService.findTypeAll(pageNum, pageSize);
        for (Type type : typeAll) {
            System.out.println(type);
        }
        System.out.println(page.getPageNum());
        System.out.println(page.getPageSize());
        System.out.println(page.getPages());
    }*/

    /*
        测试 getAll
     */
    /*@Test
    public void test05() {
        int pageNum = 0;
        int pageSize = 0;
        List<Type> typeAll = typeService.findTypeAll(pageNum, pageSize);
        for (Type type : typeAll) {
            System.out.println(type);
        }
        System.out.println("--------------");
        typeAll = typeService.findTypeAll(pageNum, pageSize);
        for (Type type : typeAll) {
            System.out.println(type);
        }
    }*/

    @Test
    public void test06() {
    }
}

