package com.chen.community.util;


/**
 * @Description 封装redis的key方便复用
 * @date 2023-08-11 23:00
 **/
public class RedisKeyUtil {
    private static final String SPLIT = ":";
    //某个实体的赞的前缀
    private static final String PREFIX_ENTITY_LIKE = "like:entity";
    //某个用户获得的赞的前缀
    private static final String PREFIX_USER_LIKE = "like:user";
    //关注目标
    private static final String PREFIX_FOLLOWEE = "followee";
    //粉丝
    private static final String PREFIX_FOLLOWER = "follower";
    //验证码
    private static final String PREFIX_KAPTCHA = "kaptcha";
    //登录凭证
    private static final String PREFIX_TICKET = "ticket";
    //用户信息
    private static final String PREFIX_USER = "user";

    //某个实体的赞
    //like:entity:entityType:entityId ->set(userId) 能知道是谁点的赞
    public static String getEntityLikeKey(Integer entityType,Integer entityId){
        return PREFIX_ENTITY_LIKE + SPLIT +entityType + SPLIT + entityId;
    }

    //某个用户的赞
    //like:user:userId -> int
    public static String getUserLikeKey(Integer userId){
        return PREFIX_USER_LIKE + SPLIT + userId;
    }

    //某个用户关注的实体
    //followee:userId:entityType -> zset(entityId,now) (userId指谁关注的)
    public static String getFolloweeKey(Integer userId,Integer entityType){
        return PREFIX_FOLLOWEE + SPLIT + userId + SPLIT + entityType;
    }

    //某个实体拥有的粉丝
    //follower:entityType:entityId -> zset(userId,now)
    public static String getFollowerKey(Integer entityType,Integer entityId){
        return PREFIX_FOLLOWER + SPLIT + entityType + SPLIT + entityId;
    }

    //登录验证码
    public static String getKaptchaKey(String owner){
        return PREFIX_KAPTCHA + SPLIT + owner;
    }

    //登录凭证
    public static String getTicketKey(String ticket){
        return PREFIX_TICKET +SPLIT + ticket;
    }

    //用户
    public static String getUserKey(Integer userId){
        return PREFIX_USER + SPLIT + userId;
    }
}
