package com.zjg.blog.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zjg.blog.annotation.CacheRemove;
import com.zjg.blog.mapper.BlogMapper;
import com.zjg.blog.pojo.Blog;
import com.zjg.blog.pojo.Tag;
import com.zjg.blog.pojo.Type;
import com.zjg.blog.service.BlogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zjg
 *
 * 无法准确更新的缓存都使用  类名 + 空格 + [ + 参数的方法 + ] 作为 key
 * 在增，删，改的时候直接删除
 */
@Service
@CacheConfig(cacheNames = "blogCache") //公共配置
public class BlogServiceImpl implements BlogService {

    private static final Logger logger = LoggerFactory.getLogger(BlogServiceImpl.class);

    @Autowired
    private BlogMapper blogMapper;

    @Transactional(readOnly = true)
    @Cacheable
    @Override
    public Blog findBlogByBlogId(Long blogId) {
        return blogMapper.selectBlogByBlogId(blogId);
    }

    @Transactional(readOnly = true)
    @Cacheable(key = "#root.targetClass.getSimpleName() + ' ' + '[' + #blogName + ']'")
    @Override
    public Blog findBlogByBlogName(String blogName) {
        return blogMapper.selectBlogByBlogName(blogName);
    }

    @Transactional(readOnly = true)
    @Cacheable
    @Override
    public PageInfo<Blog> findBlogByTypeAndTitleAndRecommend(int pageNum, int pageSize, Type type, String blogTitle, Boolean blogRecommend) {
        return findBlogByTypeAndTitleAndRecommend(pageNum, pageSize, 0, type, blogTitle, blogRecommend);
    }

    @Transactional(readOnly = true)
    @Cacheable
    @Override
    public PageInfo<Blog> findBlogByTypeAndTitleAndRecommend(int pageNum, int pageSize, int navigatePages, Type type, String blogTitle, Boolean blogRecommend) {
        //空格和 "" 会被处理为 null
        if (blogTitle != null) {
            if ("".equals(blogTitle.trim())) {
                blogTitle = null;
            }
        }

        PageHelper.startPage(pageNum, pageSize);
        List<Blog> blogs = blogMapper.selectBlogAllByBlogTypeAndBlogTitleAndBlogRecommend(type.getTypeId(), blogTitle, blogRecommend);
        PageInfo<Blog> pageInfo = new PageInfo<>(blogs, navigatePages);
        return pageInfo;
    }

    @Transactional
    // @cacheput 是可以获取到 blog 的id，那就证明是在方法完成后进行的缓存
    @CachePut(key = "#root.targetClass.getSimpleName() + ' ' + #blog.getBlogId()", unless = "#result==null")
    @CacheRemove(key = {"*BlogServiceImpl \\[*"})
    @Override
    public Blog saveBlog(Blog blog) {
        int i = blogMapper.insertBlog(blog);
        //向博客和标签的关联表里插入信息
        if (blog.getTags().size()>0) {
            Map<Long,Long> insertMap = new HashMap<>();

            for (Tag tag : blog.getTags()) {
                insertMap.put(tag.getTagId(), blog.getBlogId());
            }

            logger.debug("即将向博客标签关联的记录表插入信息：{}" ,insertMap);

            int j = blogMapper.insertBlogTag(insertMap);
            if (j<=0) {
                return null;
            }
        }
        if (i>0) {
            return blog;
        }

        return null;
    }

    @Transactional
    @CachePut( key = "#root.targetClass.getSimpleName() + ' ' + #blog.getBlogId()", unless = "#result == null")
    @CacheRemove( key = {"*BlogServiceImpl \\[*"})
    @Override
    public Blog modifyBlog(Blog blog) {
        blogMapper.deleteBlogTagByBlogId(blog.getBlogId());

        Map<Long, Long> insertMap = new HashMap<>();
        List<Tag> tags = blog.getTags();
        for (Tag tag : tags) {
            insertMap.put(tag.getTagId(), blog.getBlogId());
        }
        blogMapper.insertBlogTag(insertMap);

        if (blogMapper.modifyBlog(blog)>0) {
            return blog;
        }else{
            //事务回滚
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return null;
        }
    }

    @Transactional
    @CacheEvict
    @CacheRemove(key = "*BlogServiceImpl \\[*")
    @Override
    public int removeBlog(Long blogId) {
        blogMapper.deleteBlogTagByBlogId(blogId);
        return blogMapper.deleteBlogByBlogId(blogId);
    }

    @Transactional(readOnly = true)
    @Cacheable(key = "#root.targetClass.getSimpleName()+ '[ recommend' +#topCount + ' ]' ")
    @Override
    public List<Blog> findTopRecommend(int topCount) {
        return blogMapper.topRecommend(topCount);
    }

    @Transactional(readOnly = true)
    @Cacheable(key = "#root.targetClass.getSimpleName() + ' [ type' + #typeId + ' ]' ")
    @Override
    public List<Blog> findBlogByTypeId(Long typeId) {
        return blogMapper.selectBlogByTypdId(typeId);
    }

    @Transactional(readOnly = true)
    @Cacheable(key = "#root.targetClass.getSimpleName() + ' [ type' + #tagId + ' ]' ")
    @Override
    public List<Blog> findBlogByTagId(Long tagId) {
        return blogMapper.selectBlogByTagId(tagId);
    }

    @Transactional(readOnly = true)
    @Cacheable(key = "#root.targetClass.getSimpleName() + ' [ blogQuery ' + #blogQuery + ' ' + #pageNum + ' ' + #navigatePages + ' ]' ")
    @Override
    public PageInfo<Blog> findBlogByTitleAndContent(int pageNum, int pageSize, int navigatePages, String blogQuery) {
        PageHelper.startPage(pageNum, pageSize);
        List<Blog> blogs = blogMapper.selectBlogByTitleAndContent(blogQuery);
        PageInfo<Blog> pageInfo = new PageInfo<>(blogs, navigatePages);
        return pageInfo;
    }

    @Transactional
    @CacheEvict(allEntries = true)
    @Override
    public int modifyView(Long blogId) {
        int i = blogMapper.updateView(blogId);

        return i;
    }
}
