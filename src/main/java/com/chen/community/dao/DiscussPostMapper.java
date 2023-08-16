package com.chen.community.dao;

import com.chen.community.entity.DiscussPost;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Description 帖子
 * @date 2023-07-27 18:02
 **/
@Mapper
public interface DiscussPostMapper {
    //查帖子并分页，offset是页的起始行数，limit是一页多少行
    List<DiscussPost> selectDiscussPosts(@Param("userId") Integer userId,
                                         @Param("offset")Integer offset,
                                         @Param("limit")Integer limit);

    // @Param注解用于给参数取别名,
    // 如果只有一个参数,并且在<if>里使用,则必须加别名
    //这里userId可有可无，是变量
    //查帖子的总数
    int selectDiscussPostRows(@Param("userId") int userId);

    //插入帖子
    int insertDiscussPost(DiscussPost discussPost);

    //查询帖子详情
    DiscussPost selectDiscussPostById(Integer id);

    //更新帖子评论的数量
    int updateCommentCount(Integer id,Integer commentCount);
}

