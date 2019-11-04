package com.zjg.blog.service.impl;

import com.zjg.blog.mapper.CommentMapper;
import com.zjg.blog.pojo.Comment;
import com.zjg.blog.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zjg
 */
@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentMapper commentMapper;

    @Transactional
    @Override
    public Comment saveComment(Comment comment) {
        int i = commentMapper.insertComment(comment);
        if (i>0) {
            return comment;
        }
        return null;
    }

    @Transactional(readOnly = true)
    @Override
    public List<Comment> findCommentNotParentByBlogId(Long blogId) {
        return commentMapper.selectCommentNotParentByBlogId(blogId);

    }

    @Transactional(readOnly = true)
    @Override
    public List<Comment> findCommentByBlogId(Long blogId, Long parentId) {
        return commentMapper.selectCommentByParentId(parentId, blogId);
    }

    @Transactional()
    @Override
    public void fillReplyComments(List<Comment> comments) {

        for (Comment comment : comments) {
            List<Comment> replyComments = commentMapper.selectCommentByParentId(comment.getCommentId(), comment.getCommentBlogId());
            if (replyComments.size()>0) {
                comment.setReplyComments(replyComments);
                ArrayList<Comment> valuesComment = new ArrayList<>();
                valuesComment.addAll(replyComments);
                //因为 subFillReplyComments 在添加完所有的子评论后，第一次 subFillReplyComments
                //递归 for 结束后 comments 的值会变， 因此第一次 comments 的内存地址和 comment
                //的内存相同
                subFillReplyComments(comment, valuesComment, comment.getCommentBlogId());
            }

        }

    }


    private void subFillReplyComments(Comment comment, List<Comment> comments, Long blogId) {
        for (int i = 0; i < comments.size(); i++) {
            List<Comment> replyComments = commentMapper.selectCommentByParentId(comments.get(i).getCommentId(), blogId);
            if (replyComments.size() > 0) {   //此评论有子评论
                //填充父评论的子评论，只用二级评论
                comment.getReplyComments().addAll(replyComments);
                //子评论的子评论也要填充到子评论
                subFillReplyComments(comment, replyComments, blogId);
            }
        }
    }

}
