package com.zjg.blog.controller;

import com.zjg.blog.pojo.Blog;
import com.zjg.blog.pojo.Type;
import com.zjg.blog.service.BlogService;
import com.zjg.blog.service.TagService;
import com.zjg.blog.service.TypeService;
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
public class TypeShowController {
    private static final Logger logger = LoggerFactory.getLogger(TypeShowController.class);
    private static final String TYPES = "types";

    @Autowired
    private TypeService typeService;
    @Autowired
    private BlogService blogService;

    @GetMapping(value = {"/types/{typeId}", "/types"})
    public String type(@PathVariable(value = "typeId", required = false) Long typeId, Model model){
        List<Type> types = typeService.findTopType(Integer.MAX_VALUE);

        if (types==null || types.size()<=0) {
            //没有分类
            return TYPES;
        }

        Long activeTypeId = null;

        if (typeId == null || typeId<=0) {
            logger.debug("types也请求没有 typeId，返回分类下博客最多的博客");
            typeId = types.get(0).getTypeId();
        }
        //选中的哪个博客
        activeTypeId = typeId;

        List<Blog> blogs = blogService.findBlogByTypeId(typeId);

        model.addAttribute("types", types);
        model.addAttribute("activeTypeId", activeTypeId);
        model.addAttribute("blogs", blogs);

        logger.debug("分类页的数据 types: {}", types);
        logger.debug("分类页的数据 activeTypeId: {}", activeTypeId);
        logger.debug("分类页的数据 blogs: {}", blogs);
        return TYPES;
    }
}
