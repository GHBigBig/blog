package com.zjg;

import com.github.pagehelper.PageInfo;
import com.zjg.blog.pojo.Blog;
import com.zjg.blog.pojo.Type;
import com.zjg.blog.service.BlogService;
import com.zjg.blog.service.TypeService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author zjg
 * @create 2019-10-22 11:01
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class BlogApplicationTests05 {

    @Autowired
    private BlogService blogService;
    @Autowired
    private TypeService typeService;

    //测试 mybatis 中 selectBlogByBlogId
    @Test
    public void test01() {
        Blog blogByBlogId = blogService.findBlogByBlogId(2l);
        System.out.println(blogByBlogId);
    }

    //测试 mybatis 中 selectBlogByBlogId
    @Test
    public void test02() {
        PageInfo<Blog> blogs = blogService.findBlogByTypeAndTitleAndRecommend(1, 5, 5, new Type(), null, null);
        if (blogs.getList() != null) {
            for (Blog blog : blogs.getList()) {
                System.out.println(blog);
            }
        }
    }


    //测试 type执行 dml 是，是否可以把 blog 的缓存都清理掉
    @Test
    public void test03() {
        int i = typeService.removeTypeByTypeId(41);
        System.out.println(i);
    }


    //分页的时候传入 负数
    /*@Test
    public void test04() {
        System.out.println(blogService.findBlogByTypeAndTitleAndRecommend(-1, -1, -1, new Type(), null, null));

    }*/

    //测试 tag 的新增是否可以 新增单个 tag 的缓存，和删除所有的缓存

}