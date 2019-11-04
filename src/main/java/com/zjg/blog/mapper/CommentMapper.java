package com.zjg.blog.mapper;

import com.zjg.blog.pojo.Comment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author zjg
 */
@Mapper
public interface CommentMapper {

    /**
     * 新增评论
     * @param comment
     * @return
     */
    int insertComment(Comment comment);

    /**
     * 查找没有父类的指定的博客下的评论
     * @param blogId
     * @return
     */
    List<Comment> selectCommentNotParentByBlogId(Long blogId);

    /**
     * 根据父类查找指定博客下的评论
     * @param parentId
     * @return
     */
    List<Comment> selectCommentByParentId(@Param("parentId") Long parentId,
                                                   @Param("blogId") Long blogId);
}
