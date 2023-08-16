package com.chen.community.service;

import com.chen.community.entity.DiscussPost;

import java.util.List;

public interface IDiscussPostService {
    List<DiscussPost> findDiscussPosts(Integer userId,Integer offset,Integer limit);

    int findDiscussPostRows(Integer userId);

    int addDiscussPost(DiscussPost post);

    DiscussPost findDiscussPostById(Integer id);

    int updateCommentCount(Integer id,Integer commentCount);
}
