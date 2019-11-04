package com.zjg.blog.controller;

import com.github.pagehelper.PageInfo;
import com.zjg.blog.exception.NotFoundException;
import com.zjg.blog.pojo.Blog;
import com.zjg.blog.pojo.Tag;
import com.zjg.blog.pojo.Type;
import com.zjg.blog.service.BlogService;
import com.zjg.blog.service.TagService;
import com.zjg.blog.service.TypeService;
import com.zjg.blog.utils.MarkdownUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletContext;
import java.util.List;

/**
 * @author zjg
 */
@Controller
public class IndexController {
    private static final Logger logger = LoggerFactory.getLogger(IndexController.class);
    private static final String INDEX = "index";
    private static final String SEARCH = "search";
    private static final String BLOGS = "blog";
    private static final String REDIRECT_INDEX = "redirect:/";

    @Autowired
    private ServletContext servletContext;
    @Autowired
    private BlogService blogService;
    @Autowired
    private TypeService typeService;
    @Autowired
    private TagService tagService;

    /**
     * 首页

     * @param model
     * @return
     */
    @GetMapping(value = {"/{pageNum}", "/"})
    public String index(@PathVariable(value = "pageNum",required = false) Integer pageNum, Model model) {

        if (pageNum==null || pageNum<=0) {
            pageNum = 1;
        }

        String indexBlogCountStr = servletContext.getInitParameter("indexBlogCount");
        String indexTypeCountStr = servletContext.getInitParameter("indexTypeCount");
        String indexTagCountStr = servletContext.getInitParameter("indexTagCount");
        String indexRecommendCountStr = servletContext.getInitParameter("indexRecommendCount");

        Integer indexBlogCount = Integer.valueOf(indexBlogCountStr);
        Integer indexTypeCount = Integer.valueOf(indexTypeCountStr);
        Integer indexTagCount = Integer.valueOf(indexTagCountStr);
        Integer indexRecommendCount = Integer.valueOf(indexRecommendCountStr);

        logger.debug("首页中：博客条数 {}, 类别条数 {}, 标签条数 {}, 推荐博客条数 {}",
                indexBlogCount, indexTypeCount, indexTagCount, indexRecommendCount);


        PageInfo<Blog> pageInfo = blogService.findBlogByTypeAndTitleAndRecommend(
                pageNum, indexBlogCount, 5, new Type(), null, null);
        List<Type> topTypes = typeService.findTopType(indexTypeCount);
        List<Tag> topTags = tagService.findTopTag(indexTagCount);
        List<Blog> recommendBlogs = blogService.findTopRecommend(indexRecommendCount);

        logger.debug("首页数据: \n博客: {} \n类别: {} \n标签: {} \n推荐博客: {}",
                pageInfo, topTypes, topTags, recommendBlogs);

        model.addAttribute("pageInfo", pageInfo);
        model.addAttribute("topTypes", topTypes);
        model.addAttribute("topTags", topTags);
        model.addAttribute("recommendBlogs", recommendBlogs);

        return INDEX;
    }


    @RequestMapping(value = {"/search", "/search/{pageNum}/{query}"})
    public String search(@PathVariable(value = "pageNum",required = false) Integer pageNum,
                         @PathVariable(value = "query",required = false) String query,
                         String blogQuery,
                         Model model){
        if (pageNum==null || pageNum<=0) {
            pageNum = 1;
        }

        int pageSize = 5;
        int navigatePages = 5;

        PageInfo<Blog> pageInfo = null;
        if (query==null) {
            pageInfo = blogService.findBlogByTitleAndContent(
                    pageNum, pageSize, navigatePages, blogQuery);
        }else{
            blogQuery = query;
            pageInfo = blogService.findBlogByTitleAndContent(
                    pageNum, pageSize, navigatePages, query);
        }



        model.addAttribute("pageInfo", pageInfo);
        model.addAttribute("blogQuery", blogQuery);

        return SEARCH;
    }

    @GetMapping("/blog/{blogId}")
    public String blogs(@PathVariable(value = "blogId") Long blogId, Model model) {
        Blog blog = blogService.findBlogByBlogId(blogId);
        if (blog != null) {
            String blogContent = blog.getBlogContent();
            blogContent = MarkdownUtils.markdownToHtmlExtensions(blogContent);
            blog.setBlogContent(blogContent);
            blogService.modifyView(blog.getBlogId());

            model.addAttribute("blog", blog);
        }else{
            return REDIRECT_INDEX;
        }

        return BLOGS;
    }

}
