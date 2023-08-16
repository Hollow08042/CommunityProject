package com.chen.community;

import com.chen.community.dao.DiscussPostMapper;
import com.chen.community.dao.LoginTicketMapper;
import com.chen.community.entity.DiscussPost;
import com.chen.community.service.IUserService;
import com.chen.community.service.Impl.UserServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;

/**
 * @Description mapper接口测试
 * @date 2023-07-27 21:34
 **/
@Slf4j
public class MapperTest extends CommunityApplicationTests{
    @Autowired
    private DiscussPostMapper discussPostMapper;
    @Autowired
    private LoginTicketMapper loginTicketMapper;
    @Autowired
    private IUserService userService;
    @Test
    public void testSelectPosts(){
        List<DiscussPost> discussPostList = discussPostMapper.selectDiscussPosts(149, 0, 10);
        for (DiscussPost post : discussPostList){
            log.info("result={}",post);
        }

        int row = discussPostMapper.selectDiscussPostRows(149);
        log.info("149号的帖子数量为：{}",row);
    }
    @Test
    public void textLogout(){
        loginTicketMapper.updateStatus("bc9dfbfd62574d4d83169aac37a99d06",1);
    }
//     private static final Logger logger = LoggerFactory.getLogger(MapperTest.class);
//
//     @Test
//     public void testLogger() {
//         System.out.println(logger.getName());
//
//         logger.debug("debug log");
//         logger.info("info log");
//         logger.warn("warn log");
//         logger.error("error log");
// }
    @Test
    public void testInsertDiscussPost(){
        DiscussPost post = new DiscussPost();
        post.setUserId(151);
        post.setTitle("title");
        post.setContent("content");
        post.setCreateTime(new Date());
        discussPostMapper.insertDiscussPost(post);

    }
}
