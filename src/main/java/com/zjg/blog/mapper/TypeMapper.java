package com.zjg.blog.mapper;

import com.zjg.blog.pojo.Type;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author zjg
 */

@Mapper
public interface TypeMapper {
    /**
     * 查询用户根据指定的属性
     * @param type 查询属性的值
     * @param attributes 指定的属性，可以是多个
     * @return

    Type selectType(@Param("type") Type type, @Param("attributes") String... attributes);*/

    /**
     * 根据 typeId 查询用户
     * @param typeId
     * @return
     */
    Type selectTypeByTypeId(long typeId);

    /**
     * 根据 typeName 查询类型
     * @param typeName
     * @return
     */
    Type selectTypeByTypeName(String typeName);

    /**
     * 查询所有的分类
     * @return 所有的分类
     */
    List<Type> selectTypeAll();

    /**
     * 添加一个新分类
     * @param type  分类对象
     */
    int insertType(Type type);

    /**
     * 根据一个 typeId 删除分类
     * @param typeId  分类的 id
     */
    int deleteTypeByTypeId(long typeId);

    /**
     * 修改分类
     * @param type 分类对象
     */
    int updateType(Type type);

    /**
     * 展示包含博客数量最多的 top
     * @param topCount
     * @return
     */
    List<Type> topType(int topCount);
}
