package com.chen.community.controller;

import com.chen.community.Event.EventProducer;
import com.chen.community.entity.Event;
import com.chen.community.entity.User;
import com.chen.community.service.ILikeService;
import com.chen.community.util.CommunityConstant;
import com.chen.community.util.CommunityUtil;
import com.chen.community.util.HostHolder;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description 点赞的表现层
 * @date 2023-08-12 14:07
 **/
@Controller
public class LikeController implements CommunityConstant {
    @Autowired
    private ILikeService likeService;
    @Autowired
    private HostHolder hostHolder;
    @Autowired
    private EventProducer eventProducer;

    @PostMapping("/like")
    @ResponseBody
    public String like(Integer entityType,Integer entityId,Integer entityUserId,Integer postId){
        User user = hostHolder.getUsers();
        //点赞
        likeService.like(user.getId(),entityType,entityId,entityUserId);

        //点赞数
        long likeCount = likeService.findEntityLikeCount(entityType, entityId);
        //点赞状态
        int likeStatus = likeService.findEntityLikeStatus(user.getId(), entityType, entityId);

        Map<String,Object> map = new HashMap<>();
        map.put("likeCount",likeCount);
        map.put("likeStatus",likeStatus);

        //触发点赞事件
        if (likeStatus == 1){
            Event event = new Event()
                    .setTopic(TOPIC_LIKE)
                    .setUserId(hostHolder.getUsers().getId())
                    .setEntityType(entityType)
                    .setEntityId(entityId)
                    .setEntityUserId(entityUserId)
                    .setData("postId",postId);
            eventProducer.fireEvent(event);
        }
        return CommunityUtil.getJSONString(0,null,map);
    }
}
