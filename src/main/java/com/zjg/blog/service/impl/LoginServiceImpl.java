package com.zjg.blog.service.impl;

import com.zjg.blog.mapper.LoginMapper;
import com.zjg.blog.pojo.User;
import com.zjg.blog.service.LoginService;
import com.zjg.blog.utils.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author zjg
 */
@Service
public class LoginServiceImpl implements LoginService {
    @Autowired
    private LoginMapper loginMapper;


    @Override
    public User login(String username, String password) {
        return loginMapper.findUserByUsernameAndPassword(username, MD5Utils.code(password));
    }
}
