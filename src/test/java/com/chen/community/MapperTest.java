package com.chen.community;

import com.chen.community.dao.DiscussPostMapper;
import com.chen.community.entity.DiscussPost;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @Description mapper接口测试
 * @date 2023-07-27 21:34
 **/
@Slf4j
public class MapperTest extends CommunityApplicationTests{
    @Autowired
    private DiscussPostMapper discussPostMapper;

    @Test
    public void testSelectPosts(){
        List<DiscussPost> discussPostList = discussPostMapper.selectDiscussPosts(149, 0, 10);
        for (DiscussPost post : discussPostList){
            log.info("result={}",post);
        }

        int row = discussPostMapper.selectDiscussPostRows(149);
        log.info("149号的帖子数量为：{}",row);
    }
}
