package com.zjg.blog.mapper;

import com.zjg.blog.pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author zjg
 */
@Mapper
public interface LoginMapper {
    User findUserByUsernameAndPassword(@Param("username") String username,@Param("password") String password);

    User findUerByUserId(long userId);
}
