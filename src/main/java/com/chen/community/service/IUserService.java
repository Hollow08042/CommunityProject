package com.chen.community.service;

import com.chen.community.entity.LoginTicket;
import com.chen.community.entity.User;

import java.util.Map;

public interface IUserService {
    //查询用户
    User findUserById(Integer userId);
    //注册
    Map<String,Object> register(User user);
    //激活邮件
    int activation(Integer userId,String code);
    //登录
    Map<String,Object> login(String username,String password,Integer expiredSeconds);
    //退出登录
    void logout(String ticket);
    //拦截时查询凭证
    LoginTicket findLoginTicket(String ticket);
    //上传头像
    int updateHeader(Integer userId,String headerUrl);

    User findUserByName(String username);

}
