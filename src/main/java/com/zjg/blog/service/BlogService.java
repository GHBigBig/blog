package com.zjg.blog.service;

import com.github.pagehelper.PageInfo;
import com.sun.org.apache.xpath.internal.operations.Bool;
import com.zjg.blog.pojo.Blog;
import com.zjg.blog.pojo.Type;

import java.util.List;

/**
 * @author zjg
 */
public interface BlogService {

    /**
     * 根据 blogId 查询博客
     * @param blogId
     * @return
     */
    Blog findBlogByBlogId(Long blogId);

    /**
     * 根据 blogName 查找博客
     * @param blogName
     * @return
     */
    Blog findBlogByBlogName(String blogName);

    /**
     * @see #findBlogByTypeAndTitleAndRecommend(int, int, int, Type, String, Boolean)
     * @param pageNum
     * @param pageSize
     * @param type
     * @param blogTitle
     * @param blogRecommend
     * @return
     */
    PageInfo<Blog> findBlogByTypeAndTitleAndRecommend(int pageNum, int pageSize, Type type,
                                                      String blogTitle, Boolean blogRecommend);

    /**
     * 根据 typeId 和 blogTitle 和 blogRecommend 查找分页所有博客
     * 哪个有值按那个
     * 若都没有值那么就查询所有博客
     * @param pageNum
     * @param pageSize
     * @param navigatePages
     * @param type
     * @param blogTitle 模糊查询，空格和 "" 会被转为 null
     * @param blogRecommend
     * @return
     */
    PageInfo<Blog> findBlogByTypeAndTitleAndRecommend(int pageNum, int pageSize, int navigatePages,
                                                             Type type, String blogTitle, Boolean blogRecommend);

    /**
     * 新增对象
     * @param blog
     * @return
     */
    Blog saveBlog(Blog blog);

    /**
     * 分局 blogId 修改用户
     * @param blog blogId不可以为空
     * @return
     */
    Blog modifyBlog(Blog blog);

    /**
     * 根据 blogId 删除 blog
     * 还会将 博客和标签关联表的数据一起删除
     * @param blogId
     * @return
     */
    int removeBlog(Long blogId);

    /**
     * 根据创建时间查找最近的几个博客
     * @param topCount
     * @return
     */
    List<Blog> findTopRecommend(int topCount);

    /**
     * 类别下的博客
     * @param typeId
     * @return
     */
    List<Blog> findBlogByTypeId(Long typeId);

    /**
     * 标签的博客
     * @param tagId
     * @return
     */
    List<Blog> findBlogByTagId(Long tagId);

    /**
     * 搜索标题和内容中含有关键字的
     * @param blogQuery
     * @return
     */
    PageInfo<Blog> findBlogByTitleAndContent(int pageNum, int pageSize, int navigatePages, String blogQuery);

    /**
     * 更新博客查看次数
     * @param blogId
     * @return
     */
    int modifyView(Long blogId);
}
