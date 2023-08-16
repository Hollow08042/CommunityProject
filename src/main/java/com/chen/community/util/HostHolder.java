package com.chen.community.util;

import com.chen.community.entity.User;
import org.springframework.stereotype.Component;

/**
 * @Description 持有用户信息，用于代替session对象，线程隔离存储对象
 * @date 2023-08-05 16:18
 **/
@Component
public class HostHolder {
    private ThreadLocal<User> users = new ThreadLocal<>();

    public void setUsers(User user){
        users.set(user);
    }

    public User getUsers(){
        return users.get();
    }

    //清理
    public void clear(){
        users.remove();
    }
}
