package com.zjg.blog.mapper;

import com.zjg.blog.pojo.Tag;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author zjg
 */
@Mapper
public interface TagMapper {
    /**
     * 根据 tagId 查找标签
     * @param tagId
     * @return
     */
    Tag selectTagByTagId(Long tagId);

    /**
     * 根据 tagName 查找标签
     * @param tagName
     * @return
     */
    Tag selectTagByTagName(String tagName);

    /**
     * 查询所有的标签
     * @return
     */
    List<Tag> selectTagAll();

    /**
     * 新增标签
     * @param tag
     * @return
     */
    int insertTag(Tag tag);

    /**
     * 根据 tagId 删除标签
     * @param tagId
     * @return
     */
    int deleteTagByTagId(long tagId);

    /**
     * 删除博客与标签关联表里的与此 tagId 相关的数据
     * @param tagId
     * @return
     */
    int deleteTagBlog(long tagId);

    /**
     * 根据 tagId 修改标签
     * @param tag
     * @return
     */
    int updateTag(Tag tag);

    /**
     * 返回关联博客最多几个标签
     * @param topCount
     * @return
     */
    List<Tag> topTag(int topCount);

}
