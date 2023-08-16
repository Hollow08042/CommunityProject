package com.chen.community.service;

public interface ILikeService {
    //点赞
    void like(Integer userId,Integer entityType,Integer entityId,Integer entityUserId);

    //查询某实体点赞的数量
    long findEntityLikeCount(Integer entityType,Integer entityId);

    //查询某人对某实体的点赞状态(1-已赞 0-未赞)
    int findEntityLikeStatus(Integer userId,Integer entityType,Integer entityId);

    //查询某个用户获赞数
    int findUserLikeCount(Integer userId);
}
