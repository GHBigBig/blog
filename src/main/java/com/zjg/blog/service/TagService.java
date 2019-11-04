package com.zjg.blog.service;

import com.github.pagehelper.PageInfo;
import com.zjg.blog.pojo.Tag;
import sun.applet.resources.MsgAppletViewer_es;

import java.util.List;

/**
 * @author zjg
 */
public interface TagService {

    /**
     * 根据 tagId 获取 tag 对象
     * @param tagId tag 对象的 id
     * @return tag 对象
     */
    Tag findTagByTagId(long tagId);

    /**
     * 根据 tagName 查找标签
     * @param tagName
     * @return
     */
    Tag findTagByTagName(String tagName);

    /**
     * 分页查询数据
     * @param pageNum
     * @param pageSize
     * @return
     */
    PageInfo<Tag> findTagAll(int pageNum, int pageSize);

    /**
     * 分页查询数据带有 navigatePages
     * @param pageNum
     * @param pageSize
     * @param navigatePages 显示的页数
     * @return
     */
    PageInfo<Tag> findTagAll(int pageNum, int pageSize, int navigatePages);

    /**
     * 保存 tag
     * @param tag
     * @return 带有数据库生成主键的 tag
     */
    Tag saveTag(Tag tag);

    /**
     * 根据 tagId 删除用户
     * 也会将博客和标签的关联关系一起删除
     * @param tagId
     * @return 影响的行数
     */
    int removeTagByTagId(long tagId);

    /**
     * 修改标签根据 tagId
     * @param tag 此 tag 的 tagId 域不可以为赋值
     * @return
     */
    Tag modifyTagByTagId(Tag tag);

    /**
     * 查找关联博客最多的几个标签
     * @param topCount
     * @return
     */
    List<Tag> findTopTag(int topCount);
}
