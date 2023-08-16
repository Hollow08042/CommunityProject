package com.chen.community.service.Impl;

import com.chen.community.service.ILikeService;
import com.chen.community.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.stereotype.Service;

/**
 * @Description 点赞的业务层
 * @date 2023-08-11 23:08
 **/
@Service
public class LikeServiceImpl implements ILikeService {
    @Autowired
    private RedisTemplate redisTemplate;

    //点赞
    public void like(Integer userId,Integer entityType,Integer entityId,Integer entityUserId){
        //因为要进行两次更新，需要开启事务
        redisTemplate.execute(new SessionCallback() {
            @Override
            public Object execute(RedisOperations operations) throws DataAccessException {
                String entityLikeKey = RedisKeyUtil.getEntityLikeKey(entityType,entityId);
                String userLikeKey = RedisKeyUtil.getUserLikeKey(entityUserId);
                //点没点过赞，查询要在事务开启前查，否则不会立马查出,第一次点赞，第二次取消
                Boolean isMember = operations.opsForSet().isMember(entityLikeKey,userId);
                //开启事务
                operations.multi();
                if (isMember){
                    operations.opsForSet().remove(entityLikeKey,userId);
                    operations.opsForValue().decrement(userLikeKey);
                }else {
                    operations.opsForSet().add(entityLikeKey,userId);
                    operations.opsForValue().increment(userLikeKey);
                }
                return operations.exec();
            }
        });
    }

    //查询某实体点赞的数量
    public long findEntityLikeCount(Integer entityType,Integer entityId){
        String entityLikeKey = RedisKeyUtil.getEntityLikeKey(entityType,entityId);
        return redisTemplate.opsForSet().size(entityLikeKey);
    }

    //查询某人对某实体的点赞状态(1-已赞 0-未赞)
    public int findEntityLikeStatus(Integer userId,Integer entityType,Integer entityId){
        String entityLikeKey = RedisKeyUtil.getEntityLikeKey(entityType,entityId);
        return redisTemplate.opsForSet().isMember(entityLikeKey,userId) ? 1 : 0;
    }

    //查询某个用户获赞数
    public int findUserLikeCount(Integer userId){
        String userLikeKey = RedisKeyUtil.getUserLikeKey(userId);
        Integer count =  (Integer) redisTemplate.opsForValue().get(userLikeKey);
        return count == null ? 0 :count.intValue();
    }
}
