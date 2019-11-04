package com.zjg;

import com.github.pagehelper.PageInfo;
import com.zjg.blog.pojo.Type;
import com.zjg.blog.service.TypeService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author zjg
 * @create 2019-10-08 11:36
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class BlogApplicationTests04 {
    @Autowired
    private TypeService typeService;

    @Test
    public void test01() {
        Type typeByTypeId = typeService.findTypeByTypeId(22);
        System.out.println(typeByTypeId);
    }

    //测试分页
    @Test
    public void test02() {
        PageInfo<Type> types = typeService.findTypeAll(5, 4, 6);
        System.out.println(types);
        System.out.println("______________________________");
        types = typeService.findTypeAll(5, 4, 6);
        System.out.println(types);
    }

    //测试插入
    @Test
    public void test03() {
        Type type = new Type();
        type.setTypeName("type55");
        Type type1 = typeService.saveType(type);
        System.out.println(type1);
    }

    //测试更新
    @Test
    public void test04() {
        Type type = typeService.findTypeByTypeId(55);
        type.setTypeName(type.getTypeName() + " 】");
        Type type1 = typeService.modifyType(type);
        System.out.println(type1);
    }

}
