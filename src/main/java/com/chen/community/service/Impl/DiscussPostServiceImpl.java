package com.chen.community.service.Impl;

import com.chen.community.dao.DiscussPostMapper;
import com.chen.community.entity.DiscussPost;
import com.chen.community.service.IDiscussPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description 帖子
 * @date 2023-07-27 21:44
 **/
@Service
public class DiscussPostServiceImpl implements IDiscussPostService {
    @Autowired
    private DiscussPostMapper discussPostMapper;

    @Override
    public List<DiscussPost> findDiscussPosts(Integer userId, Integer offset, Integer limit) {
        return discussPostMapper.selectDiscussPosts(userId,offset,limit);
    }

    @Override
    public int findDiscussPostRows(Integer userId) {
        return discussPostMapper.selectDiscussPostRows(userId);
    }
}
