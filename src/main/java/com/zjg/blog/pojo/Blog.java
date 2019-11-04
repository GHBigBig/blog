package com.zjg.blog.pojo;

import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 博客类：
 *
 * @author zjg
 */
public class Blog implements Serializable {
    private static final long serialVersionUID = 6807383569218862124L;
    private Long blogId;

    @Length(max = 32)
    private String blogTitle;

    @Length( max = 1024)
    private String blogContent;

    @Length(max = 32)
    private String blogFirstPicture;

    @Length(max = 16)
    private String blogFlag;

    @Length(max = 128)
    private String blogDescription;

    private Long blogViews;
    private boolean blogRecommend;

    //赞赏
    private boolean blogAppreciation;
    //版权
    private boolean blogCopyrightabled;
    private boolean blogCommentabled;
    private boolean blogPublished;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date blogCreateTime;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date blogUpdateTime;

    //一（type）对多（blog）
    private Type type;
    //多（tag）对多（blog）
    private List<Tag> tags = new ArrayList<>();
    //一（user）对多（blog）
    private User user;
    ////一（blog）对多（comment）
    private List<Comment> comments = new ArrayList<>();

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Long getBlogId() {
        return blogId;
    }

    public void setBlogId(Long blogId) {
        this.blogId = blogId;
    }

    public String getBlogTitle() {
        return blogTitle;
    }

    public void setBlogTitle(String blogTitle) {
        this.blogTitle = blogTitle;
    }

    public String getBlogContent() {
        return blogContent;
    }

    public void setBlogContent(String blogContent) {
        this.blogContent = blogContent;
    }

    public String getBlogFirstPicture() {
        return blogFirstPicture;
    }

    public void setBlogFirstPicture(String blogFirstPicture) {
        this.blogFirstPicture = blogFirstPicture;
    }

    public String getBlogFlag() {
        return blogFlag;
    }

    public void setBlogFlag(String blogFlag) {
        this.blogFlag = blogFlag;
    }

    public Long getBlogViews() {
        return blogViews;
    }

    public void setBlogViews(Long blogViews) {
        this.blogViews = blogViews;
    }

    public boolean isBlogRecommend() {
        return blogRecommend;
    }

    public void setBlogRecommend(boolean blogRecommend) {
        this.blogRecommend = blogRecommend;
    }

    public boolean isBlogAppreciation() {
        return blogAppreciation;
    }

    public void setBlogAppreciation(boolean blogAppreciation) {
        this.blogAppreciation = blogAppreciation;
    }

    public boolean isBlogCopyrightabled() {
        return blogCopyrightabled;
    }

    public void setBlogCopyrightabled(boolean blogCopyrightabled) {
        this.blogCopyrightabled = blogCopyrightabled;
    }

    public boolean isBlogCommentabled() {
        return blogCommentabled;
    }

    public void setBlogCommentabled(boolean blogCommentabled) {
        this.blogCommentabled = blogCommentabled;
    }

    public boolean isBlogPublished() {
        return blogPublished;
    }

    public void setBlogPublished(boolean blogPublished) {
        this.blogPublished = blogPublished;
    }

    public Date getBlogCreateTime() {
        return blogCreateTime;
    }

    public void setBlogCreateTime(Date blogCreateTime) {
        this.blogCreateTime = blogCreateTime;
    }

    public Date getBlogUpdateTime() {
        return blogUpdateTime;
    }

    public void setBlogUpdateTime(Date blogUpdateTime) {
        this.blogUpdateTime = blogUpdateTime;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public String getBlogDescription() {
        return blogDescription;
    }

    public void setBlogDescription(String blogDescription) {
        this.blogDescription = blogDescription;
    }

    @Override
    public String toString() {
        return "Blog{" +
                "blogId=" + blogId +
                ", blogTitle='" + blogTitle + '\'' +
                ", blogContent='" + blogContent + '\'' +
                ", blogFirstPicture='" + blogFirstPicture + '\'' +
                ", blogFlag='" + blogFlag + '\'' +
                ", blogDescription='" + blogDescription + '\'' +
                ", blogViews=" + blogViews +
                ", blogRecommend=" + blogRecommend +
                ", blogAppreciation=" + blogAppreciation +
                ", blogCopyrightabled=" + blogCopyrightabled +
                ", blogCommentabled=" + blogCommentabled +
                ", blogPublished=" + blogPublished +
                ", blogCreateTime=" + blogCreateTime +
                ", blogUpdateTime=" + blogUpdateTime +
                ", type=" + type +
                ", tags=" + tags +
                ", user=" + user +
                ", comments=" + comments +
                '}';
    }
}
