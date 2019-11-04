package com.zjg.blog.controller;

import com.zjg.blog.pojo.Blog;
import com.zjg.blog.pojo.Tag;
import com.zjg.blog.service.BlogService;
import com.zjg.blog.service.TagService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * @author zjg
 */
@Controller
public class TagShowController {
    private static final Logger logger = LoggerFactory.getLogger(TagShowController.class);
    private static final String TAGS = "tags";

    @Autowired
    private TagService tagService;
    @Autowired
    private BlogService blogService;

    @GetMapping(value = {"/tags/{tagId}", "/tags"})
    public String tag(@PathVariable(value = "tagId", required = false) Long tagId,
                      Model model) {
        List<Tag> tags = tagService.findTopTag(Integer.MAX_VALUE);
        if (tags==null && tags.size()<=0) {
            return TAGS;
        }

        Long acitveTagId = null;
        if (tagId==null || tagId<=0) {
            tagId = tags.get(0).getTagId();
        }
        acitveTagId = tagId;

        List<Blog> blogs = blogService.findBlogByTagId(tagId);

        model.addAttribute("tags", tags);
        model.addAttribute("activeTagId", acitveTagId);
        model.addAttribute("blogs", blogs);

        logger.debug("标签类的数据 tags : {}", tags);
        logger.debug("标签类的数据 activeTagId : {}", acitveTagId);
        logger.debug("标签类的数据 blogs : {}", blogs);


        return TAGS;
    }
}
