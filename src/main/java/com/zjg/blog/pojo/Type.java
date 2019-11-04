package com.zjg.blog.pojo;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author zjg
 * 分类：
 *      一个分类下可以有 N 个博客
 */
public class Type implements Serializable {
    private static final long serialVersionUID = -270994547212829724L;
    private Long typeId;

    @NotBlank(message = "类型名不可以为空")
    @Length(max = 16, message = "类型名长度不可以超过 16 位")
    private String typeName;

    private List<Blog> blogs = new ArrayList<>();

    public List<Blog> getBlogs() {
        return blogs;
    }

    public void setBlogs(List<Blog> blogs) {
        this.blogs = blogs;
    }

    public Long getTypeId() {
        return typeId;
    }

    public void setTypeId(Long typeId) {
        this.typeId = typeId;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    @Override
    public String toString() {
        return "Type{" +
                "typeId=" + typeId +
                ", typeName='" + typeName + '\'' +
                '}';
    }
}
