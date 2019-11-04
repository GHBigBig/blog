package com.zjg.blog.service;

import com.zjg.blog.pojo.User;

/**
 * @author zjg
 */
public interface LoginService {
    /**
     * 根据用户名和密码查询用户
     * @param username  用户名
     * @param password  经过MD5加密的密码
     * @return 查询到的值 如果为 null则用户名和密码不匹配
     */
    User login(String username, String password);
}
