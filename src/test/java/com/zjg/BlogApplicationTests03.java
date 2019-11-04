package com.zjg;

import com.zjg.blog.pojo.Type;
import com.zjg.blog.service.TypeService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author zjg
 * @create 2019-10-07 21:11
 */

/*@RunWith(SpringRunner.class)
@SpringBootTest*/
public class BlogApplicationTests03 {

    @Autowired
    private TypeService typeService;

    //测试查询用户，新修改的
    /*@Test
    public void test01() {
        Type type = new Type();
        type.setTypeId(10l);
        type.setTypeName("type10");
        Type type1 = typeService.findType(type, "typeId", "typeName");
        System.out.println(type1);
        System.out.println("--------------------");
        type1 = typeService.findType(type, "typeId", "typeName");
        System.out.println(type1);
        System.out.println("--------------------");
        type1 = typeService.findType(type, "typeId");
        System.out.println(type1);
        System.out.println("--------------------");
        type1 = typeService.findType(type, "typeName");
        System.out.println(type1);
    }*/

    /*
        测试缓存能提高多少
        有缓存： 16593
        无缓冲： 42556
     */
    /*@Test
    public void test02() {
        long start = System.currentTimeMillis();
        for (int i = 0; i < 1000; i++) {
            Type type = new Type();
            type.setTypeId(10l);
            type.setTypeName("type10");
            Type type1 = typeService.findType(type, "typeId", "typeName");
            System.out.println(type1);
        }
        long end = System.currentTimeMillis();
        System.out.println("耗时： " + (end - start) + " 毫秒");
    }*/

}
