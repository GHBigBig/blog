package com.zjg;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

/**
 * @author zjg
 * @create 2019-10-27 12:37
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class BlogApplicationTests06 {
    @Autowired
    private ServletContext servletContext;

    @Test
    public void test01() {
        System.out.println(servletContext);
        String parameter = servletContext.getInitParameter("indexBlogCount");
        System.out.println(parameter);
        Integer indexBlogCount = Integer.valueOf(parameter);
        System.out.println(indexBlogCount);

    }

}
