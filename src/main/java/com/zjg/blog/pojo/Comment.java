package com.zjg.blog.pojo;

import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author zjg
 * 评论类：
 *      一个 blog 可以有多个 comment
 *
 *  这个类比较特殊，有自关联：
 *      一个评论可能有多个子评论
 *      一个评论向上只有一个相邻的父评论
 *      当前子评论和父评论也都可能没有
 */
public class Comment implements Serializable {
    private static final long serialVersionUID = -5299096128163448074L;
    private Long commentId;
    private String commentNickName;
    private String commentEmail;
    private String commentPortrait;
    private String commentContent;
    private Boolean commentIsAdmin;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date commentCreateTime;

    private Long commentBlogId;
    //父评论Id
    private Long commentParentId;
    //父评论Name
    private String commentParentNickName;

    //子评论，数据库中只记录父评论的 Id，相当于一对多中子评论是多的一方，父评论是少的一方
    private List<Comment> replyComments = new ArrayList<>();


    public Long getCommentId() {
        return commentId;
    }

    public void setCommentId(Long commentId) {
        this.commentId = commentId;
    }

    public String getCommentNickName() {
        return commentNickName;
    }

    public void setCommentNickName(String commentNickName) {
        this.commentNickName = commentNickName;
    }

    public String getCommentEmail() {
        return commentEmail;
    }

    public void setCommentEmail(String commentEmail) {
        this.commentEmail = commentEmail;
    }

    public String getCommentPortrait() {
        return commentPortrait;
    }

    public void setCommentPortrait(String commentPortrait) {
        this.commentPortrait = commentPortrait;
    }

    public String getCommentContent() {
        return commentContent;
    }

    public void setCommentContent(String commentContent) {
        this.commentContent = commentContent;
    }

    public Boolean getCommentIsAdmin() {
        return commentIsAdmin;
    }

    public void setCommentIsAdmin(Boolean commentIsAdmin) {
        this.commentIsAdmin = commentIsAdmin;
    }

    public Date getCommentCreateTime() {
        return commentCreateTime;
    }

    public void setCommentCreateTime(Date commentCreateTime) {
        this.commentCreateTime = commentCreateTime;
    }

    public Long getCommentBlogId() {
        return commentBlogId;
    }

    public void setCommentBlogId(Long commentBlogId) {
        this.commentBlogId = commentBlogId;
    }

    public Long getCommentParentId() {
        return commentParentId;
    }

    public void setCommentParentId(Long commentParentId) {
        this.commentParentId = commentParentId;
    }

    public String getCommentParentNickName() {
        return commentParentNickName;
    }

    public void setCommentParentNickName(String commentParentNickName) {
        this.commentParentNickName = commentParentNickName;
    }

    public List<Comment> getReplyComments() {
        return replyComments;
    }

    public void setReplyComments(List<Comment> replyComments) {
        this.replyComments = replyComments;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "commentId=" + commentId +
                ", commentNickName='" + commentNickName + '\'' +
                ", commentEmail='" + commentEmail + '\'' +
                ", commentPortrait='" + commentPortrait + '\'' +
                ", commentContent='" + commentContent + '\'' +
                ", commentIsAdmin=" + commentIsAdmin +
                ", commentCreateTime=" + commentCreateTime +
                ", commentBlogId=" + commentBlogId +
                ", commentParentId=" + commentParentId +
                ", commentParentNickName='" + commentParentNickName + '\'' +
                '}';
    }
}
