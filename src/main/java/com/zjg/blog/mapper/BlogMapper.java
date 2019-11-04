package com.zjg.blog.mapper;

import com.zjg.blog.pojo.Blog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author zjg
 */
@Mapper
public interface BlogMapper {

    /**
     * 根据 blogId 查找博客
     * @param blogId
     * @return
     */
    Blog selectBlogByBlogId(long blogId);

    /**
     * 根据 blogName 查询
     * @param blogName
     * @return
     */
    Blog selectBlogByBlogName(String blogName);

    /**
     * 根据 blogTitle 和 blogRecommend 查找符合博客
     * 哪个有值就按照哪个查
     * 若 blogTitle 和 blogRecommend 都没有值就查找所有的博客
     * @param typeId
     * @param blogTitle 模糊查询
     * @param blogRecommend
     * @return
     */
    List<Blog> selectBlogAllByBlogTypeAndBlogTitleAndBlogRecommend(@Param("typeId") Long typeId,
                                                                  @Param("blogTitle") String blogTitle,
                                                                  @Param("blogRecommend") Boolean blogRecommend);

    /**
     * 根据 typeId 查询博客
     * @param typeId
     * @return
     */
    List<Blog> selectBlogByTypeId(Long typeId);

    /**
     * 新增博客
     * @param blog
     * @return
     */
    int insertBlog(Blog blog);

    /**
     * 向插入博客和标签关联表插入数据
     * @param insertMap key为 blogId， vlaue 为 tagId
     * @return
     */
    int insertBlogTag(@Param("insertMap") Map<Long,Long> insertMap);

    /**
     * 根据 blogId 删除博客与标签关联表的数据
     * @param blogId
     * @return
     */
    int deleteBlogTagByBlogId(Long blogId);

    /**
     * 修改博客表的 blog 的内容
     * @param blog
     * @return
     */
    int modifyBlog(Blog blog);

    /**
     * 根据 blogId 删除 blog
     * @param blogId
     * @return
     */
    int deleteBlogByBlogId(Long blogId);

    /**
     * 根据创建时候返回推荐博客，
     * 创建时间越晚，越推荐
     * @param topCount
     * @return
     */
    List<Blog> topRecommend(int topCount);

    /**
     * 查询类别下的博客
     * @return
     */
    List<Blog> selectBlogByTypdId(Long typeId);

    /**
     * 根据标签查询博客
     * @param TagId
     * @return
     */
    List<Blog> selectBlogByTagId(Long TagId);

    /**
     * 模糊查询
     * @param blogQuery
     * @return
     */
    List<Blog> selectBlogByTitleAndContent(String blogQuery);

    /**
     * 更新博客的查看次数
     * @param blogId
     * @return
     */
    int updateView(Long blogId);

}
