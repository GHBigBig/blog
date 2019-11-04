package com.zjg.blog.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zjg.blog.annotation.CacheRemove;
import com.zjg.blog.mapper.BlogMapper;
import com.zjg.blog.mapper.TagMapper;
import com.zjg.blog.pojo.Tag;
import com.zjg.blog.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author zjg
 */
@CacheConfig(cacheNames = "tagCache"/*, cacheManager = "tagCacheManager"*/)
@Service
public class TagServiceImpl implements TagService {

    @Autowired
    private TagMapper tagMapper;
    @Autowired
    private BlogMapper blogMapper;

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    @Cacheable
    @Override
    public Tag findTagByTagId(long tagId) {
        return tagMapper.selectTagByTagId(tagId);
    }

    /*
        这个方法用的少并很难在缓存中标识出唯一 key，key 中无法包含 tagId
     */
    @Transactional(readOnly = true)
    @Override
    public Tag findTagByTagName(String tagName) {
        return tagMapper.selectTagByTagName(tagName);
    }

    @Transactional
    @Cacheable
    @Override
    public PageInfo<Tag> findTagAll(int pageNum, int pageSize) {
        return findTagAll(pageNum, pageSize, 0);
    }

    @Transactional
    @Cacheable
    @Override
    public PageInfo<Tag> findTagAll(int pageNum, int pageSize, int navigatePages) {
        PageHelper.startPage(pageNum, pageSize);
        List<Tag> tags = tagMapper.selectTagAll();
        PageInfo<Tag> pageInfo = new PageInfo<>(tags, navigatePages);
        return pageInfo;
    }

    @Transactional
    @CachePut(key = "#root.targetClass.getSimpleName() + ' ' + #tag.getTagId()", unless = "#result == null")
    @CacheRemove( key = {"*TagServiceImpl \\[*","*BlogServiceImpl*"})
    @Override
    public Tag saveTag(Tag tag) {
        int i = tagMapper.insertTag(tag);
        if (i > 0) {
            return tag;
        }
        return null;
    }

    @Transactional
    @CacheEvict
    @CacheRemove( key = {"*TagServiceImpl \\[*","*BlogServiceImpl*"})
    @Override
    public int removeTagByTagId(long tagId) {
        tagMapper.deleteTagBlog(tagId);
        return tagMapper.deleteTagByTagId(tagId);
    }

    @Transactional
    @CachePut( key = "#root.targetClass.getSimpleName() + ' ' + #tag.getTagId()", unless = "#result == null")
    @CacheRemove( key = {"*TagServiceImpl \\[*","*BlogServiceImpl*"})
    @Override
    public Tag modifyTagByTagId(Tag tag) {
        // tagId 是唯一的所有判断 ==1 比较合理
        if (tagMapper.updateTag(tag) == 1) {
            return tag;
        }else {
            return null;
        }
    }

    @Transactional(readOnly = true)
    @Cacheable(key = "#root.targetClass.getSimpleName()+' [ top_'+ #topCount +' ]'")
    @Override
    public List<Tag> findTopTag(int topCount) {
        List<Tag> tags = tagMapper.topTag(topCount);
        for (Tag tag : tags) {
            tag.setBlogs(blogMapper.selectBlogByTagId(tag.getTagId()));
        }
        return tags;
    }
}
