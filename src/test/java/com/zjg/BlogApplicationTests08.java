package com.zjg;

import com.zjg.blog.controller.TagShowController;
import com.zjg.blog.controller.TypeShowController;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author zjg
 * @create 2019-10-29 23:28
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class BlogApplicationTests08 {
    @Autowired
    private TypeShowController typeShowController;
    @Autowired
    private TagShowController tagShowController;

    //获取分类页数据
    public void test01() {

    }


}
