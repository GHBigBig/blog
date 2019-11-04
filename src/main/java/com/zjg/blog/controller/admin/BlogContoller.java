package com.zjg.blog.controller.admin;

import com.github.pagehelper.PageInfo;
import com.zjg.blog.pojo.Blog;
import com.zjg.blog.pojo.Tag;
import com.zjg.blog.pojo.Type;
import com.zjg.blog.pojo.User;
import com.zjg.blog.service.BlogService;
import com.zjg.blog.service.TagService;
import com.zjg.blog.service.TypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.Date;
import java.util.List;

/**
 * @author zjg
 */
@Controller
@RequestMapping("/admin")
public class BlogContoller {

    private static final String INPUT = "admin/blogs-input";
    private static final String LIST = "admin/blogs";
    private static final String REDIRECT_LIST = "redirect:/admin/blogs";
    private static final String LIST_SEARCH = "admin/blogs :: blogList";
    private static final String ERROR = "error/error";

    @Autowired
    private TagService tagService;
    @Autowired
    private TypeService typeService;
    @Autowired
    private BlogService blogService;

    /**
     * 这里的分页使用 ajax 实现，所以此方法不需要考虑分页
     * 此方法仅为，第一次点入 博客页面的分页
     * @param model
     * @return
     */
    @GetMapping("/blogs")
    public String blogs(Model model) {

        PageInfo<Blog> pageInfo = blogService.findBlogByTypeAndTitleAndRecommend(
                1, 5, 5, new Type(), null, false);
        model.addAttribute("pageInfo", pageInfo);

        PageInfo<Type> pageInfoTypes = typeService.findTypeAll(0, 0, 0);
        List<Type> types = pageInfoTypes.getList();
        model.addAttribute("types", types);

        return LIST;
    }

    /**
     * 分页和带有筛选条件的分页
     * @param model
     * @param pageNum 没有传值的情况下，默认是 1
     * @param pageSize 没有传值的情况下，默认是 5
     * @param navigatePages 没有传值的情况下，默认是 5
     * @param blogTitle 模糊查询，空格和 "" 会被转为 null
     * @param type
     * @param blogRecommend
     * @return
     */
    @PostMapping("/blogs/search")
    public String search(Model model, Integer pageNum, Integer pageSize, Integer navigatePages,
                         String blogTitle, Type type, Boolean blogRecommend) {

        //如果为传值那么就默认赋值
        if (pageNum == null) {
            pageNum = 1;
        }
        if (pageSize == null) {
            pageSize = 5;
        }
        if (navigatePages == null) {
            navigatePages = 5;
        }
        if (blogTitle!=null){
            if ("".equals(blogTitle)) {
                blogTitle = null;
            }
        }


        PageInfo<Blog> pageInfo = blogService.findBlogByTypeAndTitleAndRecommend(
                pageNum, pageSize, navigatePages, type, blogTitle, blogRecommend);
        model.addAttribute("pageInfo", pageInfo);

        return LIST_SEARCH;
    }

    /**
     * 新增页面
     * @param model
     * @return
     */
    @GetMapping("/blogs/input")
    public String input(Model model) {
        model.addAttribute("blog", new Blog());

        PageInfo<Type> pageInfoTypes = typeService.findTypeAll(0, 0, 0);
        List<Type> types = pageInfoTypes.getList();
        model.addAttribute("types", types);

        PageInfo<Tag> pageInfoTags = tagService.findTagAll(0, 0, 0);
        List<Tag> tags = pageInfoTags.getList();
        model.addAttribute("tags", tags);

        return INPUT;
    }

    /**
     * 新增请求
     * @param blog
     * @param session
     * @param attributes
     * @return
     */
    @PostMapping("/blogs/input")
    public String addBlog(Blog blog, HttpSession session, RedirectAttributes attributes) {
        //哪个用户的 博客
        User user = (User) session.getAttribute("user");
        blog.setUser(user);
        //创建时间和更新时间
        blog.setBlogCreateTime(new Date());
        blog.setBlogUpdateTime(new Date());

        Blog saveBlog = blogService.saveBlog(blog);
        if (saveBlog.getBlogId() == null) {
            attributes.addFlashAttribute("message", "添加失败");
        }else {
            attributes.addFlashAttribute("message", "添加成功");
        }


        return REDIRECT_LIST;
    }

    /**
     * 修改页面
     * @param blogId
     * @return
     */
    @GetMapping("/blogs/{blogId}/input")
    public String modify(@PathVariable("blogId") Long blogId, Model model) {
        Blog blog = blogService.findBlogByBlogId(blogId);

        model.addAttribute("blog", blog);

        PageInfo<Type> pageInfoTypes = typeService.findTypeAll(0, 0, 0);
        List<Type> types = pageInfoTypes.getList();
        model.addAttribute("types", types);

        PageInfo<Tag> pageInfoTags = tagService.findTagAll(0, 0, 0);
        List<Tag> tags = pageInfoTags.getList();
        model.addAttribute("tags", tags);

        return INPUT;
    }

    /**
     * 修改请求
     * @param blog
     * @param attributes
     * @return
     */
    @PutMapping("/blogs/input")
    public String modify(@Valid Blog blog,BindingResult result, RedirectAttributes attributes) {
        blog.setBlogUpdateTime(new Date());

        Blog blog1 = blogService.modifyBlog(blog);

        if (blog1 != null) {
            attributes.addFlashAttribute("message", "操作成功");
        }else {
            attributes.addFlashAttribute("message", "操作失败");
        }

        return REDIRECT_LIST;
    }

    /**
     * 这里暂时使用 get方式
     * 应该是使用 delete方法
     * @param blogId
     * @param attributes
     * @return
     */
    @GetMapping("/blogs/{blogId}/delete")
    public String delete(@PathVariable("blogId") Long blogId, RedirectAttributes attributes) {
        if (blogService.removeBlog(blogId)>0) {
            attributes.addFlashAttribute("message", "操作成功");
        }else {
            attributes.addFlashAttribute("message", "操作失败");
        }
        return REDIRECT_LIST;
    }
}
