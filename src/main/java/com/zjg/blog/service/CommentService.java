package com.zjg.blog.service;

import com.zjg.blog.pojo.Comment;

import java.util.List;

/**
 * @author zjg
 */
public interface CommentService {

    /**
     * 保存 comment
     * @param comment
     * @return 返回带有 commentId 的 comment
     */
    Comment saveComment(Comment comment);

    /**
     * 根据 blogId 查找博客下父评论
     * @param blogId
     * @return
     */
    List<Comment> findCommentNotParentByBlogId(Long blogId);

    /**
     * 根据 blogId 查找博客下的 parentId 评论下的子评论
     * @param blogId
     * @param parentId
     * @return
     */
    List<Comment> findCommentByBlogId(Long blogId, Long parentId);

    /**
     * 将传进来的 comments 里的每一个 comment 的子评论都填充了
     * 无论子评论中有多少个子评论，都放到同级子评论中
     * @param comments
     * @return
     */
    void fillReplyComments(List<Comment> comments);


}
