package com.chen.community.service;

import java.util.List;
import java.util.Map;

public interface IFollowService {
    //关注
    void follow(Integer userId,Integer entityType,Integer entityId);

    //取消关注
    void unfollow(Integer userId,Integer entityType,Integer entityId);

    //查询关注的实体的数量
    long findFolloweeCount(Integer userId,Integer entityType);

    //查询实体的粉丝数量
    long findFollowerCount(Integer entityType,Integer entityId);

    //查询当前用户是否已关注该实体
    boolean hasFollowed(Integer userId,Integer entityType,Integer entityId);

    //查询每个用户关注的人
    List<Map<String,Object>> findFollowees(Integer userId,Integer offset,Integer limit);

    //查询某用户的粉丝
    List<Map<String,Object>> findFollowers(Integer userId, Integer offset, Integer limit);
}
