package com.zjg.blog.pojo;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;
import java.util.List;

/**
 * @author zjg
 * 标签类：
 *      一个标签可能对应多个博客
 *      一个博客可能对象对各标签
 */
public class Tag implements Serializable {
    private static final long serialVersionUID = -4178399081158196526L;
    private Long tagId;
    @NotBlank(message = "标签名不可以空为")
    @Length(max = 16, message = "标签名的长度不可以超过 16")
    private String tagName;

    private List<Blog> blogs;

    public List<Blog> getBlogs() {
        return blogs;
    }

    public void setBlogs(List<Blog> blogs) {
        this.blogs = blogs;
    }

    public Long getTagId() {
        return tagId;
    }

    public void setTagId(Long tagId) {
        this.tagId = tagId;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    @Override
    public String toString() {
        return "Tag{" +
                "tagId=" + tagId +
                ", tagName='" + tagName + '\'' +
                '}';
    }
}
