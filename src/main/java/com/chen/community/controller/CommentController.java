package com.chen.community.controller;

import com.chen.community.Event.EventProducer;
import com.chen.community.dao.DiscussPostMapper;
import com.chen.community.entity.Comment;
import com.chen.community.entity.DiscussPost;
import com.chen.community.entity.Event;
import com.chen.community.service.ICommentService;
import com.chen.community.service.IDiscussPostService;
import com.chen.community.util.CommunityConstant;
import com.chen.community.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Date;

/**
 * @Description 评论的表现层
 * @date 2023-08-09 20:12
 **/
@Controller
@RequestMapping("/comment")
public class CommentController implements CommunityConstant {
    @Autowired
    private ICommentService commentService;
    @Autowired
    private HostHolder hostHolder;
    @Autowired
    private EventProducer eventProducer;
    @Autowired
    private IDiscussPostService discussPostService;

    @PostMapping("/add/{discussPostId}")
    public String addComment(@PathVariable("discussPostId") Integer discussPostId,
                             Comment comment){
        comment.setUserId(hostHolder.getUsers().getId());
        comment.setStatus(0);
        comment.setCreateTime(new Date());
        commentService.addComment(comment);

        //触发评论事件
        Event event = new Event()
                .setTopic(TOPIC_COMMENT)
                .setUserId(hostHolder.getUsers().getId())
                .setEntityType(comment.getEntityType())
                .setEntityId(comment.getEntityId())
                .setData("postId",discussPostId);
        if (comment.getEntityType() == ENTITY_TYPE_POST){
            DiscussPost target = discussPostService.findDiscussPostById(comment.getEntityId());
            event.setEntityUserId(target.getUserId());
        } else if (comment.getEntityType() == ENTITY_TYPE_COMMENT) {
            Comment target = commentService.findCommentById(comment.getEntityId());
            event.setEntityUserId(target.getUserId());
        }
        eventProducer.fireEvent(event);

        //触发发帖事件
        if (comment.getEntityType() == ENTITY_TYPE_POST){
            event = new Event()
                    .setTopic(TOPIC_PUBLISH)
                    .setUserId(comment.getUserId())
                    .setEntityType(ENTITY_TYPE_POST)
                    .setEntityId(discussPostId);
            eventProducer.fireEvent(event);
        }

        return "redirect:/discuss/detail/"+ discussPostId;
    }


}
