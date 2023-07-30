package com.chen.community.service.Impl;

import com.chen.community.dao.UserMapper;
import com.chen.community.entity.User;
import com.chen.community.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Description 用户
 * @date 2023-07-28 15:31
 **/
@Service
public class UserServiceImpl implements IUserService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public User findUserById(Integer userId) {
        return userMapper.selectById(userId);
    }
}
