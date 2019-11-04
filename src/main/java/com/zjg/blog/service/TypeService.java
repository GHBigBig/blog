package com.zjg.blog.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.sun.org.apache.bcel.internal.generic.LSTORE;
import com.zjg.blog.pojo.Type;

import java.util.List;

/**
 * @author zjg
 */
public interface TypeService {

    /**
     * 根据指定的属性查询用户
     * @param type 指定是属性的值
     * @param attributes 根据那些属性
     * @return

    Type findType(Type type, String... attributes);*/

    /**
     * 根据 typeId 查询用户
     * @param typeId 类别id
     * @return 类别对象
     */
    Type findTypeByTypeId(long typeId);

    /**
     * 根据 typeName 查询用户
     * @param typeName
     * @return
     */
    Type findTypeByTypeName(String typeName);

    /**
     * 查询查询所有的分类
     * @param pageNum 在缓存中起标记作用， number 和 size 都为 0 代表查询所有
     * @param pageSize 在缓存中起标记作用
     * @return 默认是返回所有的数据，但是经过 PageHelper 分页后就是分页的数据
     */
    PageInfo<Type> findTypeAll(int pageNum, int pageSize);

    /**
     * 带有 navigatePages 的分页
     * @param pageNum 在缓存中起标记作用， number 和 size 都为 0 代表查询所有
     * @param pageSize 在缓存中起标记作用
     * @param navigatePages 显示几页
     * @return 分页的带有 navigatePages 的数据
     */
    PageInfo<Type> findTypeAll(int pageNum, int pageSize, int navigatePages);

    /**
     * 添加一个新分类
     * @param type  分类对象
     */
     Type saveType(Type type);

    /**
     * 根据一个 typeId 删除分类
     * 先检查此分类下是否有博客
     * @param typeId  分类的 id
     */
    int removeTypeByTypeId(long typeId);


    /**
     * 修改分类
     * @param type 分类对象
     * @return
     */
    Type modifyType(Type type);

    /**
     * 查找分类下包含博客最多的几个分类
     * @param topCount
     * @return
     */
    List<Type> findTopType(int topCount);


}
